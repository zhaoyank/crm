package com.kaishengit.crm.service.serviceImpl;

import com.kaishengit.crm.entity.Trade;
import com.kaishengit.crm.entity.TradeExample;
import com.kaishengit.crm.mapper.TradeMapper;
import com.kaishengit.crm.service.TradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author zhao
 */
@Service
public class TradeServiceImpl implements TradeService {

    @Autowired
    private TradeMapper tradeMapper;

    /**
     * 查询所有trade
     * @return trade的List集合
     */
    @Override
    public List<Trade> findAllTrade() {
        return tradeMapper.selectByExample(new TradeExample());
    }
}
