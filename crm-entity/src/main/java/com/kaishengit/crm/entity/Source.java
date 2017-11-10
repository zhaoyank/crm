package com.kaishengit.crm.entity;

import java.io.Serializable;

/**
 * @author 
 */
public class Source implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    /**
     * 来源
     */
    private String sourceName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }
}