package com.e45240.wiki;

import com.e45240.wiki.tables.daos.PagesDao;
import com.e45240.wiki.tables.pojos.Pages;
import io.vertx.config.ConfigRetriever;
import io.vertx.config.ConfigRetrieverOptions;
import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.asyncsql.AsyncSQLClient;
import io.vertx.ext.asyncsql.MySQLClient;
import org.jooq.Configuration;
import org.jooq.SQLDialect;
import org.jooq.impl.DefaultConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
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

        configRetriever.getConfig(ar -> {
            if (ar.succeeded()) {
                AsyncSQLClient jdbcClient = MySQLClient.createShared(vertx, ar.result());
                Configuration daoConfig = new DefaultConfiguration().set(SQLDialect.MYSQL);
                pagesDao = new PagesDao(daoConfig, jdbcClient);
                jdbcClient.getConnection(conn -> {
                    if (conn.failed()) {
                        LOGGER.error("Could not open a database connection", ar.cause());
                        startFuture.fail(ar.cause());
                    } else {
                        vertx.eventBus().consumer(CONFIG_WIKIDB_QUEUE, this::onMessage);
                        conn.result().close();
                        startFuture.complete();
                    }
                });
            } else {
                LOGGER.error("config error", ar.cause());
                startFuture.fail(ar.cause());
            }
        });
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
                .setHandler(res -> {
                    if (res.succeeded()) {
                        JsonObject response = new JsonObject();
                        Pages page = res.result();
                        if (page == null) {
                            response.put("found", false);
                        } else {
                            response.put("found", true);
                            response.put("id", page.getId());
                            response.put("rawContent", page.getContent());
                        }
                        message.reply(response);
                    } else {
                        reportQueryError(message, res.cause());
                    }
                });
    }

    private void deletePage(Message<JsonObject> message) {
        pagesDao.deleteById(message.body().getInteger("id"))
                .setHandler(res -> {
                    if (res.succeeded()) {
                        message.reply("ok");
                    } else {
                        reportQueryError(message, res.cause());
                    }
                });
    }

    private void savePage(Message<JsonObject> message) {
        JsonObject request = message.body();
        pagesDao.update(new Pages().setId(request.getInteger("id")).setContent(request.getString("markdown")))
                .setHandler(res -> {
                    if (res.succeeded()) {
                        message.reply("ok");
                    } else {
                        reportQueryError(message, res.cause());
                    }
                });
    }

    private void createPage(Message<JsonObject> message) {
        JsonObject request = message.body();
        pagesDao.insert(new Pages().setName(request.getString("title")).setContent(request.getString("markdown")))
                .setHandler(res -> {
                    if (res.succeeded()) {
                        message.reply("ok");
                    } else {
                        reportQueryError(message, res.cause());
                    }
                });
    }

    private void fetchAllPage(Message<JsonObject> message) {
        pagesDao.findAll()
                .setHandler(res -> {
                    if (res.succeeded()) {
                        List<String> pages = res.result().stream()
                                .map(Pages::getName)
                                .sorted()
                                .collect(Collectors.toList());
                        message.reply(new JsonObject().put("pages", new JsonArray(pages)));
                    } else {
                        reportQueryError(message, res.cause());
                    }
                });
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
