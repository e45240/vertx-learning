package com.e45240.jooq;

import com.e45240.test.tables.daos.SysUserDao;
import io.vertx.config.ConfigRetrieverOptions;
import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.reactivex.ObservableHelper;
import io.vertx.reactivex.SingleHelper;
import io.vertx.reactivex.config.ConfigRetriever;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.core.Vertx;
import io.vertx.reactivex.ext.asyncsql.AsyncSQLClient;
import io.vertx.reactivex.ext.asyncsql.MySQLClient;
import org.jooq.Configuration;
import org.jooq.SQLDialect;
import org.jooq.impl.DefaultConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DbTestVerticle extends AbstractVerticle {

    private static final Logger LOGGER = LoggerFactory.getLogger(DbTestVerticle.class);

    private SysUserDao sysUserDao;

    @Override
    public void start() throws Exception {
        ConfigStoreOptions fileStore = new ConfigStoreOptions().setType("file").setConfig(new JsonObject().put("path", "db-config.json"));
        ConfigRetrieverOptions configRetrieverOptions = new ConfigRetrieverOptions().addStore(fileStore);

        ConfigRetriever configRetriever = ConfigRetriever.create(vertx, configRetrieverOptions);
        configRetriever.rxGetConfig()
                .flatMap(config -> {
                    Configuration configuration = new DefaultConfiguration().set(SQLDialect.MYSQL);
                    AsyncSQLClient sqlClient = MySQLClient.createShared(vertx, config);
                    sysUserDao = new SysUserDao(configuration, sqlClient);
                    return sqlClient.rxGetConnection();
                })
                .subscribe(conn -> {
                    conn.close();
                    testQuery();
                    LOGGER.debug("Database init");
                }, err -> LOGGER.error("Database error", err));
    }

    private void testQuery() {
        sysUserDao.findAll().subscribe(res -> {
            LOGGER.info(res.toString());
        });
    }

    public static void main(String[] args) {
        Vertx.vertx().deployVerticle(DbTestVerticle.class.getName());
    }
}
