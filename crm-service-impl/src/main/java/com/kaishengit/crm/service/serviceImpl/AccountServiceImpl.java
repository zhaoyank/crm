package com.kaishengit.crm.service.serviceImpl;

import com.kaishengit.crm.entity.Account;
import com.kaishengit.crm.entity.AccountDeptExample;
import com.kaishengit.crm.entity.AccountDeptKey;
import com.kaishengit.crm.entity.AccountExample;
import com.kaishengit.crm.service.exception.ServiceException;
import com.kaishengit.crm.mapper.AccountDeptMapper;
import com.kaishengit.crm.mapper.AccountMapper;
import com.kaishengit.crm.service.AccountService;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * AccountService的实现类
 * @author zhao
 */
@Service
public class AccountServiceImpl implements AccountService {

    private Logger logger = LoggerFactory.getLogger(AccountServiceImpl.class);

    public static final Integer COMPANY_ID = 1;

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private AccountDeptMapper accountDeptMapper;

    /**
     * 用户登录系统的方法
     * @param mobile 账号:手机号码
     * @param password  密码
     * @return  对应的账号
     * @throws ServiceException 当查询的对应账号不存在时,抛出该异常
     */
    @Override
    public Account login(String mobile, String password) throws ServiceException {
        Account account = accountMapper.selectByMobile(mobile);

        if (account != null && account.getPassword().equals(DigestUtils.md5Hex(password))) {
            logger.info("{}(账号:{}) 于 {} 登录系统",account.getUserName(),account.getMobile(),new Date());
            return account;
        } else {
            throw new ServiceException("账号或密码错误");
        }
    }

    /**
     * 保存新账号和该账号所在部门
     * @param account 新用户
     * @param deptId  该用户所在部门id
     */
    @Override
    public void save(Account account, Integer deptId) {
        account.setCreateTime(new Timestamp(System.currentTimeMillis()));
        account.setPassword(DigestUtils.md5Hex(account.getPassword()));

        accountMapper.insertSelective(account);

        AccountDeptKey accountDeptKey = new AccountDeptKey();
        accountDeptKey.setUserId(account.getId());
        accountDeptKey.setDeptId(deptId);
        accountDeptMapper.insertSelective(accountDeptKey);

    }

    /**
     * 根据查询条件获得account集合
     * @param queryParam
     * @return 相应的Account对象集合
     */
    @Override
    public List<Account> findEmployeeListWithParam(Map<String, Object> queryParam) {
        Integer deptId = (Integer) queryParam.get("deptId");
        Integer start = (Integer) queryParam.get("start");
        Integer length = (Integer) queryParam.get("length");
        String keyword = (String) queryParam.get("keyword");
        if(deptId == null || COMPANY_ID.equals(deptId)) {
            deptId = null;
        }

        List<Account> accountList = accountMapper.selectByQueryParam(keyword,deptId,start,length);
        return accountList;
    }

    /**
     * 根据部门查询数据账号数量
     * @param deptId 部门Id(查询所有则null)
     * @return
     */
    @Override
    public Long countByParam(Integer deptId) {
        if(deptId == null || COMPANY_ID.equals(deptId)) {
            deptId = null;
        }
        return accountMapper.countByDeptId(deptId);
    }

    /**
     * 保存新账号(账号,所在部门)
     * @param userName 用户名
     * @param mobile 手机号码
     * @param password 密码(未加密)
     * @param deptIds 所在部门Id数组
     * @throws ServiceException 当mobile重复时,抛出该异常
     */
    @Override
    @Transactional(rollbackFor = ServiceException.class)
    public void saveNewEmployee(String userName, String mobile, String password, Integer[] deptIds) throws ServiceException {
        AccountExample accountExample = new AccountExample();
        accountExample.createCriteria().andMobileEqualTo(mobile);

        List<Account> accountList = accountMapper.selectByExample(accountExample);
        if (accountList != null && !accountList.isEmpty()) {
            throw new ServiceException("该手机号已存在");
        }

        Account account = new Account();
        account.setUserName(userName);
        account.setMobile(mobile);
        account.setPassword(DigestUtils.md5Hex(password));
        account.setCreateTime(new Timestamp(System.currentTimeMillis()));
        account.setUpdateTime(new Timestamp(System.currentTimeMillis()));

        accountMapper.insertSelective(account);

        for (Integer deptId : deptIds) {
            AccountDeptKey accountDeptKey = new AccountDeptKey();
            accountDeptKey.setUserId(account.getId());
            accountDeptKey.setDeptId(deptId);
            accountDeptMapper.insert(accountDeptKey);
        }
    }

    /**
     * 根据id删除员工
     * @param id
     * @throws ServiceException 当该员工下有业务时,抛出该异常
     */
    @Override
    @Transactional(rollbackFor = ServiceException.class)
    public void deleteEmployeeById(Integer id) throws ServiceException {
        // TODO : 其他业务判断

        AccountDeptExample accountDeptExample = new AccountDeptExample();
        accountDeptExample.createCriteria().andUserIdEqualTo(id);
        accountDeptMapper.deleteByExample(accountDeptExample);

        accountMapper.deleteByPrimaryKey(id);
    }

    /**
     * 查找所有账号列表
     *
     * @return
     */
    @Override
    public List<Account> findAllAccount() {
        return accountMapper.selectByExample(new AccountExample());
    }


}
