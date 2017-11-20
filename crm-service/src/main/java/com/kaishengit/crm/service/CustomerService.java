package com.kaishengit.crm.service;

import com.github.pagehelper.PageInfo;
import com.kaishengit.crm.entity.Account;
import com.kaishengit.crm.entity.Customer;

import java.io.IOException;
import java.io.OutputStream;
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

    /**
     * 修改客户信息
     * @param customer
     */
    void editCustomer(Customer customer);

    /**
     * 删除客户
     * @param customer
     */
    void deleteCustomer(Customer customer);

    /**
     * 把客户放入公海
     * @param customer
     */
    void publicCustomer(Customer customer);

    /**
     * 转交客户到其他账号
     * @param customer
     * @param toAccountId
     */
    void tranCustomer(Customer customer, Integer toAccountId);

    /**
     * 将客户列表导出为csv文件
     * @param outputStream
     * @param account
     * @throws IOException
     */
    void exportCsvFileToOutputStream(OutputStream outputStream, Account account) throws IOException;

    /**
     * 将客户列表导出为xls文件
     * @param outputStream
     * @param account
     * @throws IOException
     */
    void exportXlsFileToOutputStream(OutputStream outputStream, Account account) throws IOException;

    /**
     * 查询所有的公海客户
     * @return
     */
    List<Customer> findPublicCustomer();

    /**
     * 将公海客户转为account的客户
     * @param id 公海客户id
     * @param account
     */
    void tranCustomerToMy(Integer id, Account account);

    /**
     * 查询各级别客户数量
     * @return
     */
    List<Map<String, Object>> countCustomerByLevel();

    /**
     * 查询各来源的客户数量
     * @return
     */
    List<Map<String,Object>> countCustomerBySource();

    /**
     * 根据月份查询每月的新增客户数量
     * @return
     */
    List<Map<String,Object>> countByMonth();
}
