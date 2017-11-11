package com.kaishengit.crm.service;

import com.kaishengit.crm.entity.Trade;

import java.util.List;

/**
 * @author zhao
 */
public interface TradeService {

    /**
     * 查询所有trade
     * @return trade的List集合
     */
    List<Trade> findAllTrade();


}
