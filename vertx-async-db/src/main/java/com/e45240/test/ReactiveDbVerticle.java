package com.e45240.test;

import io.vertx.config.ConfigRetrieverOptions;
import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.reactivex.config.ConfigRetriever;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.core.Vertx;
import io.vertx.reactivex.ext.asyncsql.MySQLClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.stream.Collectors;

public class ReactiveDbVerticle extends AbstractVerticle {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReactiveDbVerticle.class);

    @Override
    public void start() throws Exception {
        ConfigStoreOptions fileStore = new ConfigStoreOptions()
                .setType("file")
                .setConfig(new JsonObject().put("path", "db-config.json"));

        ConfigRetrieverOptions options = new ConfigRetrieverOptions()
                .addStore(fileStore);

        ConfigRetriever retriever = ConfigRetriever.create(vertx, options);
        retriever.rxGetConfig()
                .flatMap(conf -> MySQLClient.createShared(vertx, conf).rxGetConnection())
                .flatMap(conn -> conn.rxExecute("drop table if exists test_test")
                        .andThen(conn.rxExecute("create table if not exists test_test(id bigint not null, name varchar(255), primary key(id))"))
                        .andThen(conn.rxExecute("insert into test_test values(1, 'duyang')"))
                        .andThen(conn.rxQuery("select * from test_test"))
                        .doAfterTerminate(conn::rxClose)
                )
                .map(res -> res.getResults().stream()
                        .map(json -> json.getString(1))
                        .collect(Collectors.toList())
                )
                .subscribe(data -> {
                    LOGGER.debug(data.toString());
                }, err -> {
                    LOGGER.error("Database error", err);
                });
    }

    public static void main(String[] args) {
        Vertx.vertx().deployVerticle(ReactiveDbVerticle.class.getName());
    }

}
