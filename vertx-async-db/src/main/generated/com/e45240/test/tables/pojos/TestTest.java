/*
 * This file is generated by jOOQ.
 */
package com.e45240.test.tables.pojos;


import com.e45240.test.tables.interfaces.ITestTest;

import io.github.jklingsporn.vertx.jooq.shared.internal.VertxPojo;

import javax.annotation.Generated;


/**
 * This class is generated by jOOQ.
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.11.11"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class TestTest implements VertxPojo, ITestTest {

    private static final long serialVersionUID = -860347341;

    private Long   id;
    private String name;

    public TestTest() {}

    public TestTest(ITestTest value) {
        this.id = value.getId();
        this.name = value.getName();
    }

    public TestTest(
        Long   id,
        String name
    ) {
        this.id = id;
        this.name = name;
    }

    @Override
    public Long getId() {
        return this.id;
    }

    @Override
    public TestTest setId(Long id) {
        this.id = id;
        return this;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public TestTest setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("TestTest (");

        sb.append(id);
        sb.append(", ").append(name);

        sb.append(")");
        return sb.toString();
    }

    // -------------------------------------------------------------------------
    // FROM and INTO
    // -------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public void from(ITestTest from) {
        setId(from.getId());
        setName(from.getName());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <E extends ITestTest> E into(E into) {
        into.from(this);
        return into;
    }

    public TestTest(io.vertx.core.json.JsonObject json) {
        this();
        fromJson(json);
    }
}
