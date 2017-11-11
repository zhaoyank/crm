package com.kaishengit.crm.service;

import com.kaishengit.crm.entity.Source;

import java.util.List;

/**
 * @author zhao
 */
public interface SourceService {

    /**
     * 查询所有source
     * @return source的List集合
     */
    List<Source> findAllSoure();

}
