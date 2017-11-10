package com.kaishengit.crm.service.serviceImpl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.kaishengit.crm.entity.Customer;
import com.kaishengit.crm.entity.CustomerExample;
import com.kaishengit.crm.mapper.CustomerMapper;
import com.kaishengit.crm.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

/**
 * @author zhao
 */
@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerMapper customerMapper;

    /**
     * 根据账号Id查询相应的客户列表
     * @param accountId
     * @return 相应的客户列表
     */
    @Override
    public List<Customer> findAllCustomerByAccountId(Integer accountId) {
        CustomerExample customerExample = new CustomerExample();
        customerExample.createCriteria().andAccountIdEqualTo(accountId);
        return customerMapper.selectByExample(customerExample);
    }

    /**
     * 保存新客户
     * @param customer
     */
    @Override
    public void saveNewCustomer(Customer customer) {
        customer.setCreateTime(new Timestamp(System.currentTimeMillis()));
        customer.setLastContactTime(new Timestamp(System.currentTimeMillis()));
        customerMapper.insertSelective(customer);
    }

    /**
     * 根据条件查询Customer列表
     *
     * @param queryParam
     * @return
     */
    @Override
    public PageInfo<Customer> findAllCustomerByParams(Map<String, Object> queryParam) {
        Integer start = (Integer) queryParam.get("start");
        Integer accountId = (Integer) queryParam.get("accountId");
        String keys = "%" + (String) queryParam.get("keys") + "%";

        PageHelper.startPage(start, 10);
        CustomerExample customerExample = new CustomerExample();
        CustomerExample.Criteria criteria = customerExample.createCriteria();
        criteria.andAccountIdEqualTo(accountId);
        criteria.andCustNameLike(keys);
        List<Customer> list = customerMapper.selectByExample(customerExample);

        return new PageInfo<>(list);
    }

    /**
     * 根据id查找客户
     *
     * @param id
     * @return
     */
    @Override
    public Customer findCustomerById(Integer id) {
        return customerMapper.selectByPrimaryKey(id);
    }
}
