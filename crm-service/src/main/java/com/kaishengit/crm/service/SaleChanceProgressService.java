package com.kaishengit.crm.service;

import com.kaishengit.crm.entity.SaleChanceProgress;

import java.util.List;

/**
 * @author zhao
 */
public interface SaleChanceProgressService {

    /**
     * 根据客户id查询跟进记录
     * @return
     */
    List<SaleChanceProgress> findProgressBySaleId(Integer saleId);

    /**
     * 添加新的销售机会进度
     * @param saleChanceProgress
     */
    void saveNewProgress(SaleChanceProgress saleChanceProgress);
}
