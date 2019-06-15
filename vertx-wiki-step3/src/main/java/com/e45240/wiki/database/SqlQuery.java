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
     * 查询所有条目数据
     */
    ALL_PAGES_DATA,

    /**
     * 获取条目
     */
    GET_PAGE,

    /**
     * 根据id获取条目
     */
    GET_PAGE_BY_ID,

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
