package com.e45240.test;

import io.vertx.config.ConfigRetriever;
import io.vertx.config.ConfigRetrieverOptions;
import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.asyncsql.AsyncSQLClient;
import io.vertx.ext.asyncsql.MySQLClient;
import io.vertx.ext.sql.ResultSet;
import io.vertx.ext.sql.SQLConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author du.yang
 */
public class AsyncDbVerticle extends AbstractVerticle {

    private static final Logger LOGGER = LoggerFactory.getLogger(AsyncDbVerticle.class);

    @Override
    public void start() throws Exception {
        ConfigStoreOptions fileStore = new ConfigStoreOptions()
                .setType("file")
                .setConfig(new JsonObject().put("path", "db-config.json"));

        ConfigRetrieverOptions options = new ConfigRetrieverOptions()
                .addStore(fileStore);

        ConfigRetriever retriever = ConfigRetriever.create(vertx, options);
        // Fluent-style programming easily produces Callback Hell
        // Too deep callback results in full CPU utilization when using Intellij Idea
        retriever.getConfig(config -> {
            if (config.succeeded()) {
                AsyncSQLClient sqlClient = MySQLClient.createShared(vertx, config.result());
                sqlClient.getConnection(conn -> {
                    if (conn.succeeded()) {
                        SQLConnection connection = conn.result();
                        Future<Void> dropFuture = Future.future();
                        connection.execute("drop table if exists test_test", dropFuture);
                        if (dropFuture.succeeded()) {
                            connection.execute("create table if not exists test_test(id bigint not null, name varchar(255), primary key(id))", create -> {
                                if (create.succeeded()) {
                                    Future<Void> insertFuture = Future.future();
                                    connection.execute("insert into test_test values(1, 'duyang')", insertFuture);
                                    if (insertFuture.succeeded()) {
                                        Future<ResultSet> selectFuture = Future.future();
                                        connection.query("select * from test_test", selectFuture);
                                        connection.close();
                                        if (selectFuture.succeeded()) {
                                            LOGGER.debug(selectFuture.result().getResults().toString());
                                        } else {
                                            LOGGER.error("query data errror", selectFuture.cause());
                                        }
                                    } else {
                                        LOGGER.error("insert data error", insertFuture.cause());
                                    }
                                } else {
                                    LOGGER.error("create table error", create.cause());
                                }
                            });
                        } else {
                            LOGGER.error("drop table error", dropFuture.cause());
                        }
                    } else {
                        LOGGER.error("Database error", config.cause());
                    }
                });
            } else {
                LOGGER.error("Config error", config.cause());
            }
        });

    }

    public static void main(String[] args) {
        Vertx.vertx().deployVerticle(AsyncDbVerticle.class.getName());
    }

}
