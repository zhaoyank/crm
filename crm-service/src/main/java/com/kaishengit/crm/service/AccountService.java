package com.kaishengit.crm.service;

import com.kaishengit.crm.entity.Account;
import com.kaishengit.crm.service.exception.ServiceException;

import java.util.List;
import java.util.Map;

/**
 * 用户AccountService接口
 * @author zhao
 */
public interface AccountService {

    /**
     * 用户登录的方法
     * @param mobile
     * @param password
     * @return 返回对应的用户
     */
    Account login(String mobile, String password);

    /**
     * 根据查询条件获得account集合
     * @param queryParam
     * @return 相应的Account对象集合
     */
    List<Account> findEmployeeListWithParam(Map<String, Object> queryParam);

    /**
     * 根据部门查询数据数量
     * @param deptId 部门Id(查询所有则null)
     * @return
     */
    Long countByParam(Integer deptId);

    /**
     * 保存新账号(账号,所在部门)
     * @param userName 用户名
     * @param mobile 手机号码
     * @param password 密码(未加密)
     * @param deptIds 所在部门Id数组
     * @throws ServiceException 当mobile重复时,抛出该异常
     */
    void saveNewEmployee(String userName, String mobile, String password, Integer[] deptIds) throws ServiceException;

    /**
     * 根据id删除员工
     * @param id
     * @throws ServiceException 当该员工下有业务时,抛出该异常
     */
    void deleteEmployeeById(Integer id) throws ServiceException;

    /**
     * 查找所有账号列表
     * @return
     */
    List<Account> findAllAccount();
}
