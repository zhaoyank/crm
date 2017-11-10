package com.kaishengit.crm.service;

import com.github.pagehelper.PageInfo;
import com.kaishengit.crm.entity.Customer;

import java.util.List;
import java.util.Map;

/**
 * @author zhao
 */
public interface CustomerService {

    /**
     * 根据账号Id查询相应的客户列表
     * @return
     */
    List<Customer> findAllCustomerByAccountId(Integer accountId);

    /**
     * 保存新客户
     * @param customer
     */
    void saveNewCustomer(Customer customer);

    /**
     * 根据条件查询Customer列表
     * @param queryParam
     * @return
     */
    PageInfo<Customer> findAllCustomerByParams(Map<String, Object> queryParam);

    /**
     * 根据id查找客户
     * @param id
     * @return
     */
    Customer findCustomerById(Integer id);
}
