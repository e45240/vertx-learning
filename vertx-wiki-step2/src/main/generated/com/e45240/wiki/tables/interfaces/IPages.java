/*
 * This file is generated by jOOQ.
 */
package com.e45240.wiki.tables.interfaces;


import io.github.jklingsporn.vertx.jooq.shared.internal.VertxPojo;

import java.io.Serializable;

import javax.annotation.Generated;


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
public interface IPages extends VertxPojo, Serializable {

    /**
     * Setter for <code>nuwa.pages.Id</code>.
     */
    public IPages setId(Integer value);

    /**
     * Getter for <code>nuwa.pages.Id</code>.
     */
    public Integer getId();

    /**
     * Setter for <code>nuwa.pages.Name</code>.
     */
    public IPages setName(String value);

    /**
     * Getter for <code>nuwa.pages.Name</code>.
     */
    public String getName();

    /**
     * Setter for <code>nuwa.pages.Content</code>.
     */
    public IPages setContent(String value);

    /**
     * Getter for <code>nuwa.pages.Content</code>.
     */
    public String getContent();

    // -------------------------------------------------------------------------
    // FROM and INTO
    // -------------------------------------------------------------------------

    /**
     * Load data from another generated Record/POJO implementing the common interface IPages
     */
    public void from(com.e45240.wiki.tables.interfaces.IPages from);

    /**
     * Copy data into another generated Record/POJO implementing the common interface IPages
     */
    public <E extends com.e45240.wiki.tables.interfaces.IPages> E into(E into);

    @Override
    public default IPages fromJson(io.vertx.core.json.JsonObject json) {
        setId(json.getInteger("Id"));
        setName(json.getString("Name"));
        setContent(json.getString("Content"));
        return this;
    }


    @Override
    public default io.vertx.core.json.JsonObject toJson() {
        io.vertx.core.json.JsonObject json = new io.vertx.core.json.JsonObject();
        json.put("Id",getId());
        json.put("Name",getName());
        json.put("Content",getContent());
        return json;
    }

}
