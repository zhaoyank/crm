package com.kaishengit.crm.controller;

import com.kaishengit.crm.entity.*;
import com.kaishengit.crm.service.CustomerService;
import com.kaishengit.crm.service.SaleChanceProgressService;
import com.kaishengit.crm.service.SaleChanceService;
import com.kaishengit.crm.service.TaskService;
import com.kaishengit.crm.web.exception.ForbiddenException;
import com.kaishengit.crm.web.exception.NotFoundException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
    private SaleChanceProgressService saleChanceProgressService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private TaskService taskService;

    @GetMapping("/my")
    public String mySales(Model model, HttpSession session) {
        Account account = getCurrentAccount();
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
        Account account = getCurrentAccount();
        saleChanceService.saveNewChance(saleChance,account);
        redirectAttributes.addFlashAttribute("message","添加成功");
        return "redirect:/sales/my";
    }

    @GetMapping("/my/{saleChanceId:\\d+}")
    public String showChance(@PathVariable Integer saleChanceId,
                             HttpSession session,
                             Model model) {
        Account account = getCurrentAccount();
        SaleChance saleChance = permissions(saleChanceId);

        Customer customer = customerService.findCustomerById(saleChance.getCustId());
        List<SaleChanceProgress> progressList = saleChanceProgressService.findProgressBySaleId(saleChanceId);
        List<Task> taskList = taskService.findTaskByAccountAndSaleId(account.getId(),saleChanceId);

        model.addAttribute("saleChance", saleChance);
        model.addAttribute("customer", customer);
        model.addAttribute("progressList",progressList);
        model.addAttribute("taskList", taskList);
        model.addAttribute("accountId", account.getId());
        return "record/show";
    }

    @PostMapping("/my/new/record")
    public String newChanceProgress(SaleChanceProgress saleChanceProgress,
                                    HttpSession session) {
        permissions(saleChanceProgress.getSaleId());
        saleChanceProgressService.saveNewProgress(saleChanceProgress);
        return "redirect:/sales/my/" + saleChanceProgress.getSaleId();
    }

    @PostMapping("/my/{id:\\d+}/progress/update")
    public String changeSaleChanceProgress(@PathVariable Integer id,
                                           String progress,
                                           HttpSession session) {
        SaleChance saleChance = permissions(id);
        saleChanceService.updateSaleChanceProgress(saleChance, progress);
        return "redirect:/sales/my/" + id;
    }

    @GetMapping("/my/{id:\\d+}/delete")
    public String deleteSaleChance(@PathVariable Integer id,
                                   HttpSession session) {
        permissions(id);
        saleChanceService.deleteSalesChanceById(id);
        return "redirect:/sales/my";
    }

    @PostMapping("/new/task")
    public String newTask(Integer accountId,
                          String title,
                          String finishTime,
                          String remindTime,
                          Integer saleId) throws ParseException {

        taskService.saveNewTask(accountId,title,finishTime, remindTime, saleId, null);
        return "redirect:/sales/my/" + saleId;
    }

    /**
     * 判断客户是否存在 && 判断用户权限
     * @param id
     * @return
     */
    private SaleChance permissions(Integer id) {
        Account account = getCurrentAccount();
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
