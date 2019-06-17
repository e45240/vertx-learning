package com.e45240.wiki.database;

import io.reactivex.Flowable;
import io.reactivex.Single;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.sql.ResultSet;
import io.vertx.reactivex.CompletableHelper;
import io.vertx.reactivex.SingleHelper;
import io.vertx.reactivex.ext.jdbc.JDBCClient;
import io.vertx.reactivex.ext.sql.SQLClientHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;

/**
 * @author du.yang
 */
class WikiDatabaseServiceImpl implements WikiDatabaseService {

    private static final Logger LOGGER = LoggerFactory.getLogger(WikiDatabaseServiceImpl.class);

    private final HashMap<SqlQuery, String> sqlQueries;
    private final JDBCClient jdbcClient;

    WikiDatabaseServiceImpl(JDBCClient jdbcClient, HashMap<SqlQuery, String> sqlQueries, Handler<AsyncResult<WikiDatabaseService>> readyHandler) {
        this.jdbcClient = jdbcClient;
        this.sqlQueries = sqlQueries;

        SQLClientHelper.usingConnectionSingle(jdbcClient, conn -> conn
                .rxExecute(sqlQueries.get(SqlQuery.CREATE_PAGES_TABLE))
                .andThen(Single.just(this)))
                .subscribe(SingleHelper.toObserver(readyHandler));
    }

    @Override
    public WikiDatabaseService fetchAllPages(Handler<AsyncResult<JsonArray>> resultHandler) {
        jdbcClient.rxQuery(sqlQueries.get(SqlQuery.ALL_PAGES))
                .flatMapPublisher(res -> {
                    List<JsonArray> results = res.getResults();
                    return Flowable.fromIterable(results);
                })
                .map(json -> json.getString(0))
                .sorted()
                .collect(JsonArray::new, JsonArray::add)
                .subscribe(SingleHelper.toObserver(resultHandler));
        return this;
    }

    @Override
    public WikiDatabaseService fetchPage(String name, Handler<AsyncResult<JsonObject>> resultHandler) {
        jdbcClient.rxQueryWithParams(sqlQueries.get(SqlQuery.GET_PAGE), new JsonArray().add(name))
                .map(result -> {
                    if (result.getNumRows() > 0) {
                        JsonArray row = result.getResults().get(0);
                        return new JsonObject()
                                .put("found", true)
                                .put("id", row.getInteger(0))
                                .put("rawContent", row.getString(1));
                    } else {
                        return new JsonObject().put("found", false);
                    }
                })
                .subscribe(SingleHelper.toObserver(resultHandler));
        return this;
    }

    @Override
    public WikiDatabaseService createPage(String title, String markdown, Handler<AsyncResult<Void>> resultHandler) {
        jdbcClient.rxUpdateWithParams(sqlQueries.get(SqlQuery.CREATE_PAGE), new JsonArray().add(title).add(markdown))
                .ignoreElement().subscribe(CompletableHelper.toObserver(resultHandler));
        return this;
    }

    @Override
    public WikiDatabaseService savePage(int id, String markdown, Handler<AsyncResult<Void>> resultHandler) {
        jdbcClient.rxUpdateWithParams(sqlQueries.get(SqlQuery.SAVE_PAGE), new JsonArray().add(markdown).add(id))
                .ignoreElement().subscribe(CompletableHelper.toObserver(resultHandler));
        return this;
    }

    @Override
    public WikiDatabaseService deletePage(int id, Handler<AsyncResult<Void>> resultHandler) {
        jdbcClient.rxUpdateWithParams(sqlQueries.get(SqlQuery.DELETE_PAGE), new JsonArray().add(id))
                .ignoreElement().subscribe(CompletableHelper.toObserver(resultHandler));
        return this;
    }

    @Override
    public WikiDatabaseService fetchAllPagesData(Handler<AsyncResult<List<JsonObject>>> resultHandler) {
        jdbcClient.rxQuery(sqlQueries.get(SqlQuery.ALL_PAGES_DATA))
                .map(ResultSet::getRows)
                .subscribe(SingleHelper.toObserver(resultHandler));
        return this;
    }

    @Override
    public WikiDatabaseService fetchPageById(int id, Handler<AsyncResult<JsonObject>> resultHandler) {
        String query = sqlQueries.get(SqlQuery.GET_PAGE_BY_ID);
        JsonArray params = new JsonArray().add(id);
        Single<ResultSet> resultSet = jdbcClient.rxQueryWithParams(query, params);
        resultSet.map(result -> {
            if (result.getNumRows() > 0) {
                JsonObject row = result.getRows().get(0);
                return new JsonObject().put("found", true)
                        .put("id", row.getInteger("ID"))
                        .put("name", row.getString("NAME"))
                        .put("content", row.getString("CONTENT"));
            } else {
                return new JsonObject().put("found", false);
            }
        }).subscribe(SingleHelper.toObserver(resultHandler));
        return this;
    }
}
