package com.e45240.wiki;

import com.e45240.wiki.tables.daos.PagesDao;
import com.e45240.wiki.tables.pojos.Pages;
import io.vertx.config.ConfigRetrieverOptions;
import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.reactivex.config.ConfigRetriever;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.core.eventbus.Message;
import io.vertx.reactivex.ext.asyncsql.MySQLClient;
import io.vertx.reactivex.ext.sql.SQLClient;
import org.jooq.Configuration;
import org.jooq.SQLDialect;
import org.jooq.impl.DefaultConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class WikiDatabaseVerticle extends AbstractVerticle {

    public static final String CONFIG_WIKIDB_QUEUE = "wikidb.queue";

    private static final Logger LOGGER = LoggerFactory.getLogger(WikiDatabaseVerticle.class);

    private PagesDao pagesDao;

    @Override
    public void start(Future<Void> startFuture) throws Exception {
        // this uses blocking APIs, but data is small...

        ConfigStoreOptions fileStore = new ConfigStoreOptions().setType("file").setConfig(new JsonObject().put("path", "db-config.json"));
        ConfigRetrieverOptions configRetrieverOptions = new ConfigRetrieverOptions().addStore(fileStore);

        ConfigRetriever configRetriever = ConfigRetriever.create(vertx, configRetrieverOptions);

        configRetriever.rxGetConfig()
                .map(config -> MySQLClient.createShared(vertx, config))
                .doAfterSuccess(client -> {
                    Configuration daoConfig = new DefaultConfiguration().set(SQLDialect.MYSQL);
                    pagesDao = new PagesDao(daoConfig, client);
                })
                .flatMap(SQLClient::rxGetConnection)
                .subscribe(conn -> {
                    vertx.eventBus().consumer(CONFIG_WIKIDB_QUEUE, this::onMessage);
                    startFuture.complete();
                }, startFuture::fail);

    }

    public void onMessage(Message<JsonObject> message) {
        if (!message.headers().contains("action")) {
            LOGGER.error("No action header specified for message with headers {} and body {}",
                    message.headers(), message.body().encodePrettily());
            message.fail(ErrorCodes.NO_ACTION_SPECIFIED.ordinal(), "No action header specified");
            return;
        }
        String action = message.headers().get("action");

        switch (action) {
            case "all-pages":
                fetchAllPage(message);
                break;
            case "get-page":
                fetchPage(message);
                break;
            case "create-page":
                createPage(message);
                break;
            case "save-page":
                savePage(message);
                break;
            case "delete-page":
                deletePage(message);
                break;
            default:
                message.fail(ErrorCodes.BAD_ACTION.ordinal(), "Bad action: " + action);
        }
    }

    private void fetchPage(Message<JsonObject> message) {
        String requestPage = message.body().getString("page");

        pagesDao.findOneByName(requestPage)
                .subscribe(res -> {
                    JsonObject response = new JsonObject();
                    this.optionalHandler(res, page -> {
                        response.put("found", true);
                        response.put("id", page.getId());
                        response.put("rawContent", page.getContent());
                        message.reply(response);
                    }, () -> {
                        response.put("found", false);
                        message.reply(response);
                    });
                }, err -> reportQueryError(message, err));
    }

    private void deletePage(Message<JsonObject> message) {
        pagesDao.deleteById(message.body().getInteger("id"))
                .subscribe(res -> message.reply("ok"), err -> reportQueryError(message, err));
    }

    private void savePage(Message<JsonObject> message) {
        JsonObject request = message.body();
        pagesDao.update(new Pages().setId(request.getInteger("id")).setContent(request.getString("markdown")))
                .subscribe(res -> message.reply("ok"), err -> reportQueryError(message, err));
    }

    private void createPage(Message<JsonObject> message) {
        JsonObject request = message.body();
        pagesDao.insert(new Pages().setName(request.getString("title")).setContent(request.getString("markdown")))
                .subscribe(res -> message.reply("ok"), err -> reportQueryError(message, err));
    }

    private void fetchAllPage(Message<JsonObject> message) {
        pagesDao.findAll()
                .map(pages -> pages.stream().map(Pages::getName).sorted().collect(Collectors.toList()))
                .map(JsonArray::new)
                .map(pages -> new JsonObject().put("pages", pages))
                .subscribe(message::reply, err -> reportQueryError(message, err));
    }

    private <E> void optionalHandler(Optional<E> optional, Consumer<E> success, Runnable error) {
        if (optional.isPresent()) {
            success.accept(optional.get());
        } else {
            error.run();
        }
    }

    private void reportQueryError(Message<JsonObject> message, Throwable cause) {
        LOGGER.error("Database query error", cause);
        message.fail(ErrorCodes.DB_ERROR.ordinal(), cause.getMessage());
    }

    private enum ErrorCodes {

        /**
         * 未指定请求event标识
         */
        NO_ACTION_SPECIFIED,

        /**
         * 请求event标识不合法
         */
        BAD_ACTION,

        /**
         * 数据库异常
         */
        DB_ERROR
    }

}
