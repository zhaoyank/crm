package com.kaishengit.crm.service.serviceImpl;

import com.kaishengit.crm.entity.Customer;
import com.kaishengit.crm.entity.SaleChance;
import com.kaishengit.crm.entity.SaleChanceProgress;
import com.kaishengit.crm.entity.SaleChanceProgressExample;
import com.kaishengit.crm.mapper.CustomerMapper;
import com.kaishengit.crm.mapper.SaleChanceMapper;
import com.kaishengit.crm.mapper.SaleChanceProgressMapper;
import com.kaishengit.crm.service.CustomerService;
import com.kaishengit.crm.service.SaleChanceProgressService;
import com.kaishengit.crm.service.SaleChanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @author zhao
 */
@Service
public class SaleChanceProgressServiceImpl implements SaleChanceProgressService {

    @Autowired
    private SaleChanceProgressMapper saleChanceProgressMapper;
    @Autowired
    private CustomerMapper customerMapper;
    @Autowired
    private SaleChanceMapper saleChanceMapper;

    /**
     * 根据客户id查询跟进记录
     * @return
     */
    @Override
    public List<SaleChanceProgress> findProgressBySaleId(Integer saleId) {
        SaleChanceProgressExample saleChanceProgressExample = new SaleChanceProgressExample();
        saleChanceProgressExample.createCriteria().andSaleIdEqualTo(saleId);
        return saleChanceProgressMapper.selectByExampleWithBLOBs(saleChanceProgressExample);
    }

    /**
     * 添加新的销售机会进度
     * @param saleChanceProgress
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveNewProgress(SaleChanceProgress saleChanceProgress) {
        saleChanceProgress.setCreateTime(new Date());
        saleChanceProgressMapper.insert(saleChanceProgress);

        SaleChance saleChance = saleChanceMapper.selectByPrimaryKey(saleChanceProgress.getSaleId());
        saleChance.setLastTime(new Date());
        saleChanceMapper.updateByPrimaryKeySelective(saleChance);

        Customer customer = customerMapper.selectByPrimaryKey(saleChance.getCustId());
        customer.setLastContactTime(new Date());
        customerMapper.updateByPrimaryKeySelective(customer);
    }
}
