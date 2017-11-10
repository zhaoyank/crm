package com.kaishengit.crm.entity;

import java.io.Serializable;

/**
 * @author 
 */
public class Trade implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    /**
     * 行业
     */
    private String tradeName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTradeName() {
        return tradeName;
    }

    public void setTradeName(String tradeName) {
        this.tradeName = tradeName;
    }
}