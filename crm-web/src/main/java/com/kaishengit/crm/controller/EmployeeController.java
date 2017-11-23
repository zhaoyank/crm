package com.kaishengit.crm.controller;

import com.kaishengit.crm.entity.Account;
import com.kaishengit.crm.service.AccountService;
import com.kaishengit.crm.service.exception.ServiceException;
import com.kaishengit.util.DataTableResult;
import com.kaishengit.util.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zhao
 */
@Controller
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private AccountService accountService;

    @GetMapping()
    public String list(Model model) {
        return "employee/list";
    }

    @PostMapping("/new")
    @ResponseBody
    public JsonResult newEmployee(String userName, String mobile, String password, @RequestParam(name = "deptId") Integer[] deptIds) {
        try {
            accountService.saveNewEmployee(userName, mobile, password, deptIds);
            return JsonResult.success();
        } catch (ServiceException ex) {
            ex.printStackTrace();
            return JsonResult.error(ex.getMessage());
        }
    }

    @GetMapping("/{id:\\d+}/delete")
    @ResponseBody
    public JsonResult deleteEmployee(@PathVariable Integer id) {
        try {
            accountService.deleteEmployeeById(id);
        } catch (ServiceException ex) {
            ex.printStackTrace();
            return JsonResult.error(ex.getMessage());
        }
        return null;
    }

    @GetMapping("/load.json")
    @ResponseBody
    public DataTableResult<Account> loadEmployeeList(Integer draw, Integer start, Integer length,
                                                     Integer deptId,
                                                     HttpServletRequest request) {
        String keyword = request.getParameter("search[value]");

        Map<String,Object> queryParam = new HashMap<>();
        queryParam.put("start",start);
        queryParam.put("length",length);
        queryParam.put("keyword",keyword);
        queryParam.put("deptId",deptId);


        List<Account> list = accountService.findEmployeeListWithParam(queryParam);

        Long count = accountService.countByParam(deptId);

        return new DataTableResult<>(draw, count.intValue(),list.size(), list);
    }

}
