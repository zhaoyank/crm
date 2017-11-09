package com.kaishengit.crm.controller;

import com.kaishengit.crm.entity.Dept;
import com.kaishengit.crm.service.exception.ServiceException;
import com.kaishengit.crm.service.DeptService;
import com.kaishengit.util.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @author zhao
 */
@Controller
@RequestMapping("/dept")
public class DeptController {

    @Autowired
    private DeptService deptService;

    @PostMapping("/list.json")
    @ResponseBody
    public List<Dept> list() {
        return deptService.findAllDept();
    }

    @PostMapping("/new.json")
    @ResponseBody
    public JsonResult newDept(Dept dept) {
        try {
            deptService.save(dept);
            return JsonResult.success();
        } catch (ServiceException ex) {
            ex.printStackTrace();
            return JsonResult.error(ex.getMessage());
        }
    }

    @PostMapping("/{id:\\d+}/delete")
    @ResponseBody
    public JsonResult deleteDept(@PathVariable Integer id) {
        try {
            deptService.deleteDeptById(id);
            return JsonResult.success();
        } catch (ServiceException ex) {
            ex.printStackTrace();
            return JsonResult.error(ex.getMessage());
        }
    }

}
