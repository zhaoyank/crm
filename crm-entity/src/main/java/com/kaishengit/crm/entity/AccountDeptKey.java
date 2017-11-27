package com.kaishengit.crm.entity;

import java.io.Serializable;

public class AccountDeptKey implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer userId;

    private Integer deptId;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getDeptId() {
        return deptId;
    }

    public void setDeptId(Integer deptId) {
        this.deptId = deptId;
    }
}