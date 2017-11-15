package com.kaishengit.crm.controller;

import com.github.pagehelper.PageInfo;
import com.kaishengit.crm.entity.*;
import com.kaishengit.crm.service.*;
import com.kaishengit.crm.web.exception.ForbiddenException;
import com.kaishengit.crm.web.exception.NotFoundException;
import com.kaishengit.util.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zhao
 */
@Controller
@RequestMapping("/customer")
public class CustomerController extends BaseController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private TradeService tradeService;
    @Autowired
    private SourceService sourceService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private SaleChanceService saleChanceService;

    @GetMapping("/my")
    public String list(Model model) {
        List<Trade> tradeList = tradeService.findAllTrade();
        List<Source> sourceList = sourceService.findAllSoure();

        model.addAttribute("tradeList",tradeList);
        model.addAttribute("sourceList",sourceList);
        return "customer/my";
    }

    @GetMapping("/list.json")
    @ResponseBody
    public PageInfo<Customer> custList(HttpSession session,
                                       @RequestParam(name = "p",required = false,defaultValue = "1") Integer start,
                                       @RequestParam(required = false,defaultValue = "") String keys) {

        if("undefined".equals(keys)) {
            keys = "";
        }

        Account account = getCurrentAccount(session);
        Map<String, Object> queryParam = new HashMap<>();
        queryParam.put("accountId",account.getId());
        queryParam.put("start", start);
        queryParam.put("keys", keys);

        return customerService.findAllCustomerByParams(queryParam);
    }

    @PostMapping("/new")
    @ResponseBody
    public JsonResult newCustomer(HttpSession session,Customer customer) {
        Account account = getCurrentAccount(session);
        customer.setAccountId(account.getId());
        customerService.saveNewCustomer(customer);
        return JsonResult.success();
    }

    @GetMapping("/my/{id:\\d+}")
    public String showMyCustomer(@PathVariable Integer id,
                                 HttpSession session,
                                 Model model) {
        List<Trade> tradeList = tradeService.findAllTrade();
        List<Source> sourceList = sourceService.findAllSoure();

        Customer customer = permissions(session, id);
        List<Account> accountList = accountService.findAllAccount();

        Integer accountId = getCurrentAccount(session).getId();
        List<SaleChance> saleChanceList = saleChanceService.findByAccountIdAndCustId(accountId, id);

        model.addAttribute("accountList",accountList);
        model.addAttribute("customer",customer);
        model.addAttribute("tradeList",tradeList);
        model.addAttribute("sourceList",sourceList);
        model.addAttribute("saleChanceList",saleChanceList);
        return "customer/show";
    }

    @GetMapping("/my/{id:\\d+}/delete")
    public String deleteCustomer(@PathVariable Integer id,
                                 HttpSession session,
                                 RedirectAttributes redirectAttributes) {
        Customer customer = permissions(session, id);
        customerService.deleteCustomer(customer);

        redirectAttributes.addFlashAttribute("message", "客户删除成功");
        return "redirect:/customer/my/";
    }

    @PostMapping("/my/{id:\\d+}")
    public String editCustomer(Customer customer,
                               HttpSession session,
                               @PathVariable Integer id,
                               RedirectAttributes redirectAttributes) {

        permissions(session, id);
        customerService.editCustomer(customer);
        redirectAttributes.addFlashAttribute("message", "修改成功");
        return "redirect:/customer/my/" + id;
    }

    @GetMapping("/my/{id:\\d+}/public")
    public String publicCustomer(@PathVariable Integer id,
                                 HttpSession session,
                                 RedirectAttributes redirectAttributes) {
        Customer customer = permissions(session, id);
        customerService.publicCustomer(customer);
        redirectAttributes.addFlashAttribute("message","将客户放入公海成功");
        return "redirect:/customer/my";

    }
    /**
     * 转交客户
     * @return
     */
    @GetMapping("/my/{customerId:\\d+}/tran/{toAccountId:\\d+}")
    public String tranCustomer(@PathVariable Integer customerId,
                               @PathVariable Integer toAccountId,
                               HttpSession session,
                               RedirectAttributes redirectAttributes) {
        Customer customer = permissions(session , customerId);
        customerService.tranCustomer(customer,toAccountId);

        redirectAttributes.addFlashAttribute("message","客户转交成功");
        return "redirect:/customer/my";
    }

    /**
     * 将数据导出为csv文件
     */
    @GetMapping("/my/export.csv")
    public void exportCsvData(HttpServletResponse response,
                              HttpSession session) throws IOException {
        Account account = getCurrentAccount(session);

        response.setContentType("text/csv;charset=GBK");
        String fileName = new String("我的客户.csv".getBytes("UTF-8"),"ISO8859-1");
        response.addHeader("Content-Disposition","attachment; filename=\""+fileName+"\"");

        OutputStream outputStream = response.getOutputStream();
        customerService.exportCsvFileToOutputStream(outputStream,account);
    }

    /**
     * 将数据导出为xls文件
     */
    @GetMapping("/my/export.xls")
    public void exportXlsData(HttpServletResponse response,
                              HttpSession session) throws IOException {
        Account account = getCurrentAccount(session);

        response.setContentType("application/vnd.ms-excel");
        String fileName = new String("我的客户.xls".getBytes("UTF-8"),"ISO8859-1");
        response.addHeader("Content-Disposition","attachment; filename=\""+fileName+"\"");

        OutputStream outputStream = response.getOutputStream();
        customerService.exportXlsFileToOutputStream(outputStream,account);
    }


    @GetMapping("/public")
    public String publicCustomer() {
        return "customer/public";
    }

    private Customer permissions(HttpSession session, Integer id) {
        Account account = getCurrentAccount(session);
        Customer customer = customerService.findCustomerById(id);

        if (customer == null) {
            throw new NotFoundException("客户" + id + "不存在");
        }

        if(!customer.getAccountId().equals(account.getId())) {
            throw new ForbiddenException("权限不足");
        }
        return customer;
    }
}
