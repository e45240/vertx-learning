/*
 * This file is generated by jOOQ.
 */
package com.e45240.test.tables;


import com.e45240.test.Indexes;
import com.e45240.test.Keys;
import com.e45240.test.Nuwa;
import com.e45240.test.tables.records.TestTestRecord;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Generated;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Index;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Schema;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.TableImpl;


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
public class TestTest extends TableImpl<TestTestRecord> {

    private static final long serialVersionUID = 1473503853;

    /**
     * The reference instance of <code>nuwa.test_test</code>
     */
    public static final TestTest TEST_TEST = new TestTest();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<TestTestRecord> getRecordType() {
        return TestTestRecord.class;
    }

    /**
     * The column <code>nuwa.test_test.id</code>.
     */
    public final TableField<TestTestRecord, Long> ID = createField("id", org.jooq.impl.SQLDataType.BIGINT.nullable(false), this, "");

    /**
     * The column <code>nuwa.test_test.name</code>.
     */
    public final TableField<TestTestRecord, String> NAME = createField("name", org.jooq.impl.SQLDataType.VARCHAR(255), this, "");

    /**
     * Create a <code>nuwa.test_test</code> table reference
     */
    public TestTest() {
        this(DSL.name("test_test"), null);
    }

    /**
     * Create an aliased <code>nuwa.test_test</code> table reference
     */
    public TestTest(String alias) {
        this(DSL.name(alias), TEST_TEST);
    }

    /**
     * Create an aliased <code>nuwa.test_test</code> table reference
     */
    public TestTest(Name alias) {
        this(alias, TEST_TEST);
    }

    private TestTest(Name alias, Table<TestTestRecord> aliased) {
        this(alias, aliased, null);
    }

    private TestTest(Name alias, Table<TestTestRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""));
    }

    public <O extends Record> TestTest(Table<O> child, ForeignKey<O, TestTestRecord> key) {
        super(child, key, TEST_TEST);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Schema getSchema() {
        return Nuwa.NUWA;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Index> getIndexes() {
        return Arrays.<Index>asList(Indexes.TEST_TEST_PRIMARY);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UniqueKey<TestTestRecord> getPrimaryKey() {
        return Keys.KEY_TEST_TEST_PRIMARY;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<UniqueKey<TestTestRecord>> getKeys() {
        return Arrays.<UniqueKey<TestTestRecord>>asList(Keys.KEY_TEST_TEST_PRIMARY);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TestTest as(String alias) {
        return new TestTest(DSL.name(alias), this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TestTest as(Name alias) {
        return new TestTest(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public TestTest rename(String name) {
        return new TestTest(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public TestTest rename(Name name) {
        return new TestTest(name, null);
    }
}
