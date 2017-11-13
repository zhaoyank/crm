package com.kaishengit.crm.controller;

import com.kaishengit.crm.entity.Account;
import com.kaishengit.crm.entity.Customer;
import com.kaishengit.crm.entity.SaleChance;
import com.kaishengit.crm.service.CustomerService;
import com.kaishengit.crm.service.SaleChanceService;
import com.kaishengit.crm.web.exception.ForbiddenException;
import com.kaishengit.crm.web.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @author zhao
 */
@Controller
@RequestMapping("sales")
public class SalesController extends BaseController {

    @Autowired
    private SaleChanceService saleChanceService;

    @Autowired
    private CustomerService customerService;

    @GetMapping("/my")
    public String mySales(Model model, HttpSession session) {
        Account account = getCurrentAccount(session);
        List<SaleChance> saleChanceList = saleChanceService.findAllMyChange(account.getId());
        List<Customer> customerList = customerService.findAllCustomerByAccountId(account.getId());

        model.addAttribute("sales",saleChanceList);
        model.addAttribute("customers", customerList);
        return "record/my";
    }

    @PostMapping("/my")
    public String saveChance(SaleChance saleChance,
                             HttpSession session,
                             RedirectAttributes redirectAttributes) {
        Account account = getCurrentAccount(session);
        saleChanceService.saveNewChance(saleChance,account);
        redirectAttributes.addFlashAttribute("message","添加成功");
        return "redirect:/sales/my";
    }

    @GetMapping("/my/{saleChanceId:\\d+}")
    public String showChance(@PathVariable Integer saleChanceId,
                             HttpSession session,
                             Model model) {
        SaleChance saleChance = permissions(session , saleChanceId);
        Customer customer = customerService.findCustomerById(saleChance.getCustId());

        model.addAttribute("saleChance", saleChance);
        model.addAttribute("customer", customer);
        return "record/show";
    }





    private SaleChance permissions(HttpSession session, Integer id) {
        Account account = getCurrentAccount(session);
        SaleChance saleChance = saleChanceService.findSaleChanceById(id);

        if (saleChance == null) {
            throw new NotFoundException("客户" + id + "不存在");
        }

        if(!saleChance.getAccountId().equals(account.getId())) {
            throw new ForbiddenException("权限不足");
        }
        return saleChance;
    }

}