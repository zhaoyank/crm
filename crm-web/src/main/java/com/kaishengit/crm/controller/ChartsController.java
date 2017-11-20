package com.kaishengit.crm.controller;

import com.kaishengit.crm.service.CustomerService;
import com.kaishengit.crm.service.SaleChanceService;
import com.kaishengit.util.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * @author zhao
 */
@Controller
@RequestMapping("/charts")
public class ChartsController {

    @Autowired
    private CustomerService customerService;
    @Autowired
    private SaleChanceService saleChanceService;

    @GetMapping
    public String charts() {
        return "charts/customer";
    }

    @GetMapping("/customer/level")
    @ResponseBody
    public JsonResult customerLevelChart() {
        List<Map<String, Object>> result = customerService.countCustomerByLevel();
        return JsonResult.success(result);
    }

    @GetMapping("/sale/progress")
    @ResponseBody
    public JsonResult saleChanceProgressChart() {
        List<Map<String, Object>> result = saleChanceService.countByProgress();
        return JsonResult.success(result);
    }

    @GetMapping("/customer/source")
    @ResponseBody
    public JsonResult customerSourceChart() {
        List<Map<String, Object>> result = customerService.countCustomerBySource();
        return JsonResult.success(result);
    }

    @GetMapping("/customer/time")
    @ResponseBody
    public JsonResult customerCountByMonth() {
        List<Map<String, Object>> result = customerService.countByMonth();
        return JsonResult.success(result);
    }
}
