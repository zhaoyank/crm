package com.kaishengit.crm.service.serviceImpl;

import com.kaishengit.crm.entity.Account;
import com.kaishengit.crm.entity.Dept;
import com.kaishengit.crm.entity.DeptExample;
import com.kaishengit.crm.service.exception.ServiceException;
import com.kaishengit.crm.mapper.DeptMapper;
import com.kaishengit.crm.service.DeptService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author zhao
 */
@Service
public class DeptServiceImpl implements DeptService {

    private Logger logger = LoggerFactory.getLogger(DeptServiceImpl.class);

    @Autowired
    private DeptMapper deptMapper;

    /**
     * 查询部门列表
     * @return 所有dept的List集合
     */
    @Override
    public List<Dept> findAllDept() {
        return deptMapper.selectByExample(new DeptExample());
    }

    /**
     * 保存部门
     * @param dept
     * @throws ServiceException
     */
    @Override
    public void save(Dept dept) throws ServiceException {
        DeptExample deptExample = new DeptExample();
        deptExample.createCriteria().andDeptNameEqualTo(dept.getDeptName());
        List<Dept> deptList = deptMapper.selectByExample(deptExample);

        if(deptList != null && !deptList.isEmpty()) {
            throw new ServiceException("该部门名称已存在");
        }
        deptMapper.insertSelective(dept);
        logger.info("添加新部门:{}",dept.getDeptName());
    }

    /**
     * 根据Id删除部门
     * @param id 部门id
     * @throws ServiceException 如果该部门下有员工,则抛出该异常
     */
    @Override
    public void deleteDeptById(Integer deptId) throws ServiceException {
        List<Account> accountList = deptMapper.selectAccountByDeptId(deptId);
        if (accountList != null && !accountList.isEmpty()) {
            throw new ServiceException("该部门有员工,无法删除");
        }

        deptMapper.deleteByPrimaryKey(deptId);
    }
}
