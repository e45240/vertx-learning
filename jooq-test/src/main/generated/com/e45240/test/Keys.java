/*
 * This file is generated by jOOQ.
 */
package com.e45240.test;


import com.e45240.test.tables.SysUser;
import com.e45240.test.tables.TestTest;
import com.e45240.test.tables.records.SysUserRecord;
import com.e45240.test.tables.records.TestTestRecord;

import javax.annotation.Generated;

import org.jooq.Identity;
import org.jooq.UniqueKey;
import org.jooq.impl.Internal;


/**
 * A class modelling foreign key relationships and constraints of tables of 
 * the <code>nuwa</code> schema.
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.11.9"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Keys {

    // -------------------------------------------------------------------------
    // IDENTITY definitions
    // -------------------------------------------------------------------------

    public static final Identity<SysUserRecord, Integer> IDENTITY_SYS_USER = Identities0.IDENTITY_SYS_USER;

    // -------------------------------------------------------------------------
    // UNIQUE and PRIMARY KEY definitions
    // -------------------------------------------------------------------------

    public static final UniqueKey<SysUserRecord> KEY_SYS_USER_PRIMARY = UniqueKeys0.KEY_SYS_USER_PRIMARY;
    public static final UniqueKey<TestTestRecord> KEY_TEST_TEST_PRIMARY = UniqueKeys0.KEY_TEST_TEST_PRIMARY;

    // -------------------------------------------------------------------------
    // FOREIGN KEY definitions
    // -------------------------------------------------------------------------


    // -------------------------------------------------------------------------
    // [#1459] distribute members to avoid static initialisers > 64kb
    // -------------------------------------------------------------------------

    private static class Identities0 {
        public static Identity<SysUserRecord, Integer> IDENTITY_SYS_USER = Internal.createIdentity(SysUser.SYS_USER, SysUser.SYS_USER.ID);
    }

    private static class UniqueKeys0 {
        public static final UniqueKey<SysUserRecord> KEY_SYS_USER_PRIMARY = Internal.createUniqueKey(SysUser.SYS_USER, "KEY_sys_user_PRIMARY", SysUser.SYS_USER.ID);
        public static final UniqueKey<TestTestRecord> KEY_TEST_TEST_PRIMARY = Internal.createUniqueKey(TestTest.TEST_TEST, "KEY_test_test_PRIMARY", TestTest.TEST_TEST.ID);
    }
}
