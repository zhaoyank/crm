package com.kaishengit.crm.controller;

import com.github.pagehelper.PageInfo;
import com.kaishengit.crm.entity.Account;
import com.kaishengit.crm.entity.Customer;
import com.kaishengit.crm.entity.Source;
import com.kaishengit.crm.entity.Trade;
import com.kaishengit.crm.service.CustomerService;
import com.kaishengit.crm.service.SourceService;
import com.kaishengit.crm.service.TradeService;
import com.kaishengit.crm.web.exception.ForbiddenException;
import com.kaishengit.crm.web.exception.NotFoundException;
import com.kaishengit.util.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
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
        Account account = getCurrentAccount(session);
        Customer customer = customerService.findCustomerById(id);

        if (customer == null) {
            throw new NotFoundException("客户" + id + "不存在");
        }

        if(!customer.getAccountId().equals(account.getId())) {
            throw new ForbiddenException("权限不足");
        }

        model.addAttribute("customer",customer);
        return "customer/show";
    }
}
