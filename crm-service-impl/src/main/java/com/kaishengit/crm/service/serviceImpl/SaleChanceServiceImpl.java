package com.kaishengit.crm.service.serviceImpl;

import com.kaishengit.crm.entity.*;
import com.kaishengit.crm.mapper.CustomerMapper;
import com.kaishengit.crm.mapper.SaleChanceMapper;
import com.kaishengit.crm.mapper.SaleChanceProgressMapper;
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
public class SaleChanceServiceImpl implements SaleChanceService {

    @Autowired
    private SaleChanceMapper saleChanceMapper;

    @Autowired
    private SaleChanceProgressMapper saleChanceProgressMapper;

    @Autowired
    private CustomerMapper customerMapper;

    /**
     * 根据账号id查询销售机会集合
     * @param accountId
     * @return
     */
    @Override
    public List<SaleChance> findAllMyChange(Integer accountId) {
        return saleChanceMapper.selectSaleChangeByAccountIdWithCustName(accountId);
    }

    /**
     * 保存新销售机会记录
     * @param saleChance
     * @param account
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveNewChance(SaleChance saleChance, Account account) {
        saleChance.setAccountId(account.getId());
        saleChance.setCreateTime(new Date());
        saleChance.setLastTime(new Date());
        saleChanceMapper.insertSelective(saleChance);

        SaleChanceProgress saleChanceProgress = new SaleChanceProgress();
        saleChanceProgress.setContent("将当前进度修改为 [" + saleChance.getProgress() + "]");
        saleChanceProgress.setSaleId(saleChance.getId());
        saleChanceProgress.setCreateTime(new Date());
        saleChanceProgressMapper.insertSelective(saleChanceProgress);

        Customer customer = customerMapper.selectByPrimaryKey(saleChance.getCustId());
        customer.setLastContactTime(new Date());
        customerMapper.updateByPrimaryKeySelective(customer);
    }

    /**
     * 根据id查询对应销售机会
     *
     * @param id
     * @return
     */
    @Override
    public SaleChance findSaleChanceById(Integer id) {
        return saleChanceMapper.selectByPrimaryKey(id);
    }
}
