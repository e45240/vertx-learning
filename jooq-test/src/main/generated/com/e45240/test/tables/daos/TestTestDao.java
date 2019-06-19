/*
 * This file is generated by jOOQ.
 */
package com.e45240.test.tables.daos;


import com.e45240.test.tables.TestTest;
import com.e45240.test.tables.records.TestTestRecord;

import io.github.jklingsporn.vertx.jooq.shared.async.AbstractAsyncVertxDAO;

import java.util.List;

import javax.annotation.Generated;

import org.jooq.Configuration;


import io.reactivex.Single;
import java.util.Optional;
import io.github.jklingsporn.vertx.jooq.rx.async.AsyncRXQueryExecutor;
/**
 * This class is generated by jOOQ.
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.11.9"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class TestTestDao extends AbstractAsyncVertxDAO<TestTestRecord, com.e45240.test.tables.pojos.TestTest, Long, Single<List<com.e45240.test.tables.pojos.TestTest>>, Single<Optional<com.e45240.test.tables.pojos.TestTest>>, Single<Integer>, Single<Long>> implements io.github.jklingsporn.vertx.jooq.rx.VertxDAO<TestTestRecord,com.e45240.test.tables.pojos.TestTest,Long> {

    /**
     * @param configuration Used for rendering, so only SQLDialect must be set and must be one of the MYSQL types or POSTGRES.
     * @param delegate A configured AsyncSQLClient that is used for query execution
     */
    public TestTestDao(Configuration configuration,io.vertx.reactivex.ext.asyncsql.AsyncSQLClient delegate) {
        super(TestTest.TEST_TEST, com.e45240.test.tables.pojos.TestTest.class, new AsyncRXQueryExecutor<TestTestRecord,com.e45240.test.tables.pojos.TestTest,Long>(configuration,delegate,com.e45240.test.tables.pojos.TestTest::new, TestTest.TEST_TEST));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Long getId(com.e45240.test.tables.pojos.TestTest object) {
        return object.getId();
    }

    /**
     * Find records that have <code>name IN (values)</code> asynchronously
     */
    public Single<List<com.e45240.test.tables.pojos.TestTest>> findManyByName(List<String> values) {
        return findManyByCondition(TestTest.TEST_TEST.NAME.in(values));
    }

    @Override
    public AsyncRXQueryExecutor<TestTestRecord,com.e45240.test.tables.pojos.TestTest,Long> queryExecutor(){
        return (AsyncRXQueryExecutor<TestTestRecord,com.e45240.test.tables.pojos.TestTest,Long>) super.queryExecutor();
    }

    @Override
    protected java.util.function.Function<Object,Long> keyConverter(){
        return lastId -> Long.valueOf(((io.vertx.core.json.JsonArray)lastId).getLong(0).longValue());
    }
}
