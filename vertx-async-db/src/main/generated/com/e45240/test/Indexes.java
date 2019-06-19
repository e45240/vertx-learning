/*
 * This file is generated by jOOQ.
 */
package com.e45240.test;


import com.e45240.test.tables.SysUser;
import com.e45240.test.tables.TestTest;

import javax.annotation.Generated;

import org.jooq.Index;
import org.jooq.OrderField;
import org.jooq.impl.Internal;


/**
 * A class modelling indexes of tables of the <code>nuwa</code> schema.
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.11.11"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Indexes {

    // -------------------------------------------------------------------------
    // INDEX definitions
    // -------------------------------------------------------------------------

    public static final Index SYS_USER_PRIMARY = Indexes0.SYS_USER_PRIMARY;
    public static final Index TEST_TEST_PRIMARY = Indexes0.TEST_TEST_PRIMARY;

    // -------------------------------------------------------------------------
    // [#1459] distribute members to avoid static initialisers > 64kb
    // -------------------------------------------------------------------------

    private static class Indexes0 {
        public static Index SYS_USER_PRIMARY = Internal.createIndex("PRIMARY", SysUser.SYS_USER, new OrderField[] { SysUser.SYS_USER.ID }, true);
        public static Index TEST_TEST_PRIMARY = Internal.createIndex("PRIMARY", TestTest.TEST_TEST, new OrderField[] { TestTest.TEST_TEST.ID }, true);
    }
}