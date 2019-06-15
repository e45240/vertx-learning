package com.e45240.wiki.database;

/**
 * @author du.yang
 */
public enum SqlQuery {

    /**
     * 创建表
     */
    CREATE_PAGES_TABLE,

    /**
     * 查询所有条目
     */
    ALL_PAGES,

    /**
     * 获取条目
     */
    GET_PAGE,

    /**
     * 创建条目
     */
    CREATE_PAGE,

    /**
     * 更新条目
     */
    SAVE_PAGE,

    /**
     * 删除条目
     */
    DELETE_PAGE

}
