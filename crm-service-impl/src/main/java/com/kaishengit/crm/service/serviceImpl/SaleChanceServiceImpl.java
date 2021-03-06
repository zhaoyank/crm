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
import java.util.Map;

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
        // 保存新的销售机会
        saleChance.setAccountId(account.getId());
        saleChance.setCreateTime(new Date());
        saleChance.setLastTime(new Date());
        saleChanceMapper.insertSelective(saleChance);

        // 第一次保存时, 跟进保存销售机会进度
        SaleChanceProgress saleChanceProgress = new SaleChanceProgress();
        saleChanceProgress.setContent("将当前进度修改为 [" + saleChance.getProgress() + "]");
        saleChanceProgress.setSaleId(saleChance.getId());
        saleChanceProgress.setCreateTime(new Date());
        saleChanceProgressMapper.insertSelective(saleChanceProgress);

        // 修改客户的最后跟进时间
        Customer customer = customerMapper.selectByPrimaryKey(saleChance.getCustId());
        customer.setLastContactTime(new Date());
        customerMapper.updateByPrimaryKeySelective(customer);
    }

    /**
     * 根据id查询对应销售机会
     * @param id
     * @return
     */
    @Override
    public SaleChance findSaleChanceById(Integer id) {
        return saleChanceMapper.selectByPrimaryKey(id);
    }

    /**
     * 更新销售机会的进度记录
     * @param saleChance
     * @param progress
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateSaleChanceProgress(SaleChance saleChance, String progress) {
        // 更新销售机会的更新时间
        saleChance.setLastTime(new Date());
        saleChance.setProgress(progress);
        saleChanceMapper.updateByPrimaryKeySelective(saleChance);

        // 更新客户最后跟进时间
        Customer customer = customerMapper.selectByPrimaryKey(saleChance.getCustId());
        customer.setLastContactTime(new Date());
        customerMapper.updateByPrimaryKeySelective(customer);

        // 更新销售机会进度
        SaleChanceProgress saleChanceProgress = new SaleChanceProgress();
        saleChanceProgress.setCreateTime(new Date());
        saleChanceProgress.setContent("将当前进度修改为: [" + progress + "]");
        saleChanceProgress.setSaleId(saleChance.getId());
        saleChanceProgressMapper.insert(saleChanceProgress);
    }

    /**
     * 根据主键删除销售机会
     * @param id
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteSalesChanceById(Integer id) {
        SaleChance saleChance = saleChanceMapper.selectByPrimaryKey(id);

        // 删除此销售机会的进度信息
        SaleChanceProgressExample recordExample = new SaleChanceProgressExample();
        recordExample.createCriteria().andSaleIdEqualTo(id);
        saleChanceProgressMapper.deleteByExample(recordExample);

        // 删除销售机会
        saleChanceMapper.deleteByPrimaryKey(id);

        // 更改此客户最后跟进时间
        // 获得之前最近一次的跟进时间
        SaleChanceExample example = new SaleChanceExample();
        example.createCriteria().andCustIdEqualTo(saleChance.getCustId());
        example.setOrderByClause("last_time desc");
        List<SaleChance> saleChanceList = saleChanceMapper.selectByExample(example);

        Customer customer = customerMapper.selectByPrimaryKey(saleChance.getCustId());
        if(saleChanceList.isEmpty()) {
            customer.setLastContactTime(null);
        } else {
            customer.setLastContactTime(saleChanceList.get(0).getLastTime());
        }
        customerMapper.updateByPrimaryKeySelective(customer);
    }

    /**
     * 根据当前账号和客户ID查询对应的销售计划
     * @param accountId
     * @param custId
     * @return
     */
    @Override
    public List<SaleChance> findByAccountIdAndCustId(Integer accountId, Integer custId) {
        SaleChanceExample saleChanceExample = new SaleChanceExample();
        saleChanceExample.createCriteria()
                .andAccountIdEqualTo(accountId)
                .andCustIdEqualTo(custId);
        return saleChanceMapper.selectByExample(saleChanceExample);
    }

    /**
     * 查询各进度下的数量
     * @return
     */
    @Override
    public List<Map<String, Object>> countByProgress() {
        return saleChanceMapper.countByProgress();
    }
}
