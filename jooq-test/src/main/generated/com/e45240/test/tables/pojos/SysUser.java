/*
 * This file is generated by jOOQ.
 */
package com.e45240.test.tables.pojos;


import com.e45240.test.tables.interfaces.ISysUser;

import io.github.jklingsporn.vertx.jooq.shared.internal.VertxPojo;

import java.sql.Timestamp;

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
public class SysUser implements VertxPojo, ISysUser {

    private static final long serialVersionUID = 965764170;

    private Integer   id;
    private String    username;
    private String    telephone;
    private String    mail;
    private String    password;
    private Integer   deptId;
    private Integer   status;
    private String    remark;
    private String    operator;
    private Timestamp operateTime;
    private String    operateIp;

    public SysUser() {}

    public SysUser(ISysUser value) {
        this.id = value.getId();
        this.username = value.getUsername();
        this.telephone = value.getTelephone();
        this.mail = value.getMail();
        this.password = value.getPassword();
        this.deptId = value.getDeptId();
        this.status = value.getStatus();
        this.remark = value.getRemark();
        this.operator = value.getOperator();
        this.operateTime = value.getOperateTime();
        this.operateIp = value.getOperateIp();
    }

    public SysUser(
        Integer   id,
        String    username,
        String    telephone,
        String    mail,
        String    password,
        Integer   deptId,
        Integer   status,
        String    remark,
        String    operator,
        Timestamp operateTime,
        String    operateIp
    ) {
        this.id = id;
        this.username = username;
        this.telephone = telephone;
        this.mail = mail;
        this.password = password;
        this.deptId = deptId;
        this.status = status;
        this.remark = remark;
        this.operator = operator;
        this.operateTime = operateTime;
        this.operateIp = operateIp;
    }

    @Override
    public Integer getId() {
        return this.id;
    }

    @Override
    public SysUser setId(Integer id) {
        this.id = id;
        return this;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public SysUser setUsername(String username) {
        this.username = username;
        return this;
    }

    @Override
    public String getTelephone() {
        return this.telephone;
    }

    @Override
    public SysUser setTelephone(String telephone) {
        this.telephone = telephone;
        return this;
    }

    @Override
    public String getMail() {
        return this.mail;
    }

    @Override
    public SysUser setMail(String mail) {
        this.mail = mail;
        return this;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public SysUser setPassword(String password) {
        this.password = password;
        return this;
    }

    @Override
    public Integer getDeptId() {
        return this.deptId;
    }

    @Override
    public SysUser setDeptId(Integer deptId) {
        this.deptId = deptId;
        return this;
    }

    @Override
    public Integer getStatus() {
        return this.status;
    }

    @Override
    public SysUser setStatus(Integer status) {
        this.status = status;
        return this;
    }

    @Override
    public String getRemark() {
        return this.remark;
    }

    @Override
    public SysUser setRemark(String remark) {
        this.remark = remark;
        return this;
    }

    @Override
    public String getOperator() {
        return this.operator;
    }

    @Override
    public SysUser setOperator(String operator) {
        this.operator = operator;
        return this;
    }

    @Override
    public Timestamp getOperateTime() {
        return this.operateTime;
    }

    @Override
    public SysUser setOperateTime(Timestamp operateTime) {
        this.operateTime = operateTime;
        return this;
    }

    @Override
    public String getOperateIp() {
        return this.operateIp;
    }

    @Override
    public SysUser setOperateIp(String operateIp) {
        this.operateIp = operateIp;
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("SysUser (");

        sb.append(id);
        sb.append(", ").append(username);
        sb.append(", ").append(telephone);
        sb.append(", ").append(mail);
        sb.append(", ").append(password);
        sb.append(", ").append(deptId);
        sb.append(", ").append(status);
        sb.append(", ").append(remark);
        sb.append(", ").append(operator);
        sb.append(", ").append(operateTime);
        sb.append(", ").append(operateIp);

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
    public void from(ISysUser from) {
        setId(from.getId());
        setUsername(from.getUsername());
        setTelephone(from.getTelephone());
        setMail(from.getMail());
        setPassword(from.getPassword());
        setDeptId(from.getDeptId());
        setStatus(from.getStatus());
        setRemark(from.getRemark());
        setOperator(from.getOperator());
        setOperateTime(from.getOperateTime());
        setOperateIp(from.getOperateIp());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <E extends ISysUser> E into(E into) {
        into.from(this);
        return into;
    }

    public SysUser(io.vertx.core.json.JsonObject json) {
        this();
        fromJson(json);
    }
}
