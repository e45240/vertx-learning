package com.e45240.wiki.database;

import com.e45240.wiki.DatabaseConstants;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.ext.jdbc.JDBCClient;
import io.vertx.serviceproxy.ServiceBinder;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Properties;

public class WikiDatabaseVerticle extends AbstractVerticle {

    public static final String CONFIG_WIKIDB_SQL_QUERIES_RESOURCE_FILE = "wikidb.sqlqueries.resource.file";
    public static final String CONFIG_WIKIDB_QUEUE = "wikidb.queue";

    @Override
    public void start(Future<Void> startFuture) throws Exception {
        HashMap<SqlQuery, String> sqlQueries = loadSqlQueries();
        JDBCClient jdbcClient = JDBCClient.createShared(vertx, new JsonObject()
                .put("url", config().getString(DatabaseConstants.CONFIG_WIKIDB_JDBC_URL, DatabaseConstants.DEFAULT_WIKIDB_JDBC_URL))
                .put("driver_class", config().getString(DatabaseConstants.CONFIG_WIKIDB_JDBC_DRIVER_CLASS, DatabaseConstants.DEFAULT_WIKIDB_JDBC_DRIVER_CLASS))
                .put("max_pool_size", config().getInteger(DatabaseConstants.CONFIG_WIKIDB_JDBC_MAX_POOL_SIZE, DatabaseConstants.DEFAULT_JDBC_MAX_POOL_SIZE)));
        WikiDatabaseService.create(jdbcClient, sqlQueries, ready -> {
            if (ready.succeeded()) {
                ServiceBinder binder = new ServiceBinder(vertx.getDelegate());
                binder.setAddress(CONFIG_WIKIDB_QUEUE).register(WikiDatabaseService.class, ready.result());
                startFuture.complete();
            } else {
                startFuture.fail(ready.cause());
            }
        });
    }

    /**
     * Note: this uses blocking APIs, but data is small...
     */
    private HashMap<SqlQuery, String> loadSqlQueries() throws IOException {

        String queriesFile = config().getString(CONFIG_WIKIDB_SQL_QUERIES_RESOURCE_FILE);
        InputStream queriesInputStream;
        if (queriesFile != null) {
            queriesInputStream = new FileInputStream(queriesFile);
        } else {
            queriesInputStream = getClass().getResourceAsStream("/db-queries.properties");
        }

        Properties queriesProps = new Properties();
        queriesProps.load(queriesInputStream);
        queriesInputStream.close();

        HashMap<SqlQuery, String> sqlQueries = new HashMap<>();
        sqlQueries.put(SqlQuery.CREATE_PAGES_TABLE, queriesProps.getProperty("create-pages-table"));
        sqlQueries.put(SqlQuery.ALL_PAGES, queriesProps.getProperty("all-pages"));
        sqlQueries.put(SqlQuery.ALL_PAGES_DATA, queriesProps.getProperty("all-pages-data"));
        sqlQueries.put(SqlQuery.GET_PAGE, queriesProps.getProperty("get-page"));
        sqlQueries.put(SqlQuery.CREATE_PAGE, queriesProps.getProperty("create-page"));
        sqlQueries.put(SqlQuery.SAVE_PAGE, queriesProps.getProperty("save-page"));
        sqlQueries.put(SqlQuery.DELETE_PAGE, queriesProps.getProperty("delete-page"));
        sqlQueries.put(SqlQuery.GET_PAGE_BY_ID, queriesProps.getProperty("get-page-by-id"));
        return sqlQueries;
    }
}
