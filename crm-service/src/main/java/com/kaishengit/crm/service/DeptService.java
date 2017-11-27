package com.kaishengit.crm.service;

import com.kaishengit.crm.entity.Dept;
import com.kaishengit.crm.service.exception.ServiceException;

import java.util.List;

/**
 * DeptService接口
 * @author zhao
 */
public interface DeptService {

    /**
     * 查询部门列表
     * @return 所有dept的List集合
     */
    List<Dept> findAllDept();

    /**
     * 保存部门
     * @param dept
     */
    void save(Dept dept);

    /**
     * 根据Id删除部门
     * @param deptId 部门id
     * @throws ServiceException 如果该部门下有员工,则抛出该异常
     */
    void deleteDeptById(Integer deptId) throws ServiceException;

    List<Dept> findByUserId(Integer userId);
}
