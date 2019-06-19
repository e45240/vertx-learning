package com.e45240.test.tables.mappers;

import io.reactiverse.pgclient.Row;
import java.util.function.Function;

public class RowMappers {

    private RowMappers(){}

    public static Function<Row,com.e45240.test.tables.pojos.SysUser> getSysUserMapper() {
        return row -> {
            com.e45240.test.tables.pojos.SysUser pojo = new com.e45240.test.tables.pojos.SysUser();
            pojo.setId(row.getInteger("id"));
            pojo.setUsername(row.getString("username"));
            pojo.setTelephone(row.getString("telephone"));
            pojo.setMail(row.getString("mail"));
            pojo.setPassword(row.getString("password"));
            pojo.setDeptId(row.getInteger("dept_id"));
            pojo.setStatus(row.getInteger("status"));
            pojo.setRemark(row.getString("remark"));
            pojo.setOperator(row.getString("operator"));
            // Omitting unrecognized type DataType [ t=datetime; p=0; s=0; u="nuwa"."sys_user_operate_time"; j=null ] (java.sql.Timestamp) for column operate_time!
            pojo.setOperateIp(row.getString("operate_ip"));
            return pojo;
        };
    }

    public static Function<Row,com.e45240.test.tables.pojos.TestTest> getTestTestMapper() {
        return row -> {
            com.e45240.test.tables.pojos.TestTest pojo = new com.e45240.test.tables.pojos.TestTest();
            pojo.setId(row.getLong("id"));
            pojo.setName(row.getString("name"));
            return pojo;
        };
    }

}
