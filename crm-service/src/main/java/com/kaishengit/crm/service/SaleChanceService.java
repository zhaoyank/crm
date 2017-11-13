package com.kaishengit.crm.service;

import com.kaishengit.crm.entity.Account;
import com.kaishengit.crm.entity.SaleChance;

import java.util.List;

/**
 * @author zhao
 */
public interface SaleChanceService {

    /**
     * 根据账号id查询销售机会集合
     * @param accountId
     * @return
     */
    List<SaleChance> findAllMyChange(Integer accountId);

    /**
     * 保存新销售机会记录
     * @param saleChance
     * @param account
     */
    void saveNewChance(SaleChance saleChance, Account account);

    /**
     * 根据id查询对应销售机会
     * @return
     */
    SaleChance findSaleChanceById(Integer id);

}