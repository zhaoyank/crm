package com.kaishengit.crm.service.serviceImpl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.kaishengit.crm.entity.Account;
import com.kaishengit.crm.entity.Customer;
import com.kaishengit.crm.entity.CustomerExample;
import com.kaishengit.crm.mapper.AccountMapper;
import com.kaishengit.crm.mapper.CustomerMapper;
import com.kaishengit.crm.service.CustomerService;
import org.apache.commons.io.IOUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author zhao
 */
@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerMapper customerMapper;

    @Autowired
    private AccountMapper accountMapper;

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
     * @param id
     * @return
     */
    @Override
    public Customer findCustomerById(Integer id) {
        return customerMapper.selectByPrimaryKey(id);
    }

    /**
     * 修改客户信息
     * @param customer
     */
    @Override
    public void editCustomer(Customer customer) {
        customer.setUpdateTime(new Date());
        customerMapper.updateByPrimaryKeySelective(customer);
    }

    /**
     * 删除客户
     * @param customer
     */
    @Override
    public void deleteCustomer(Customer customer) {
        // TODO: 判断该客户下是否有业务

        customerMapper.deleteByPrimaryKey(customer.getId());
    }

    /**
     * 把客户放入公海
     * @param customer
     */
    @Override
    public void publicCustomer(Customer customer) {
        Account account = accountMapper.selectByPrimaryKey(customer.getAccountId());

        customer.setAccountId(null);
        customer.setReminder(customer.getReminder() + "->" + account.getUserName() + "将该客户放入公海");
        customerMapper.updateByPrimaryKey(customer);
    }

    /**
     * 转交客户到其他账号
     * @param customer
     * @param toAccountId
     */
    @Override
    public void tranCustomer(Customer customer, Integer toAccountId) {
        Account account = accountMapper.selectByPrimaryKey(customer.getAccountId());
        customer.setAccountId(toAccountId);
        customer.setReminder(customer.getReminder() + " " + "从"+account.getUserName()+"转交过来");
        customerMapper.updateByPrimaryKeySelective(customer);
    }

    /**
     * 将客户列表导出为csv文件
     * @param outputStream
     * @param account
     */
    @Override
    public void exportCsvFileToOutputStream(OutputStream outputStream, Account account) throws IOException {
        List<Customer> customerList = findAllCustomerByAccountId(account.getId());

        StringBuilder sb = new StringBuilder();
        sb.append("姓名")
                .append(",")
                .append("联系电话")
                .append(",")
                .append("职位")
                .append(",")
                .append("地址")
                .append("\r\n");
        for(Customer customer : customerList) {
            sb.append(customer.getCustName())
                    .append(",")
                    .append(customer.getMobile())
                    .append(",")
                    .append(customer.getJob())
                    .append(",")
                    .append(customer.getAddress())
                    .append("\r\n");
        }
        IOUtils.write(sb.toString(),outputStream,"GBK");

        outputStream.flush();
        outputStream.close();
    }

    /**
     * 将客户列表导出为xls文件
     * @param outputStream
     * @param account
     */
    @Override
    public void exportXlsFileToOutputStream(OutputStream outputStream, Account account) throws IOException {
        List<Customer> customerList = findAllCustomerByAccountId(account.getId());

        Workbook workbook = new HSSFWorkbook();
        Sheet sheet = workbook.createSheet("我的客户");
        Row titleRow = sheet.createRow(0);
        Cell nameCell = titleRow.createCell(0);
        nameCell.setCellValue("姓名");
        titleRow.createCell(1).setCellValue("电话");
        titleRow.createCell(2).setCellValue("职位");
        titleRow.createCell(3).setCellValue("地址");

        for(int i = 0;i < customerList.size();i++) {
            Customer customer = customerList.get(i);

            Row dataRow = sheet.createRow(i+1);
            dataRow.createCell(0).setCellValue(customer.getCustName());
            dataRow.createCell(1).setCellValue(customer.getMobile());
            dataRow.createCell(2).setCellValue(customer.getJob());
            dataRow.createCell(3).setCellValue(customer.getAddress());
        }


        workbook.write(outputStream);
        outputStream.flush();
        outputStream.close();
    }
}
