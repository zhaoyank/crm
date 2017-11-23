package com.kaishengit.crm.controller;

import com.kaishengit.crm.entity.Account;
import com.kaishengit.crm.entity.Task;
import com.kaishengit.crm.service.TaskService;
import com.kaishengit.crm.web.exception.ForbiddenException;
import com.kaishengit.crm.web.exception.NotFoundException;
import com.kaishengit.util.JsonResult;
import com.qiniu.util.Json;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.lang.reflect.AccessibleObject;
import java.text.ParseException;
import java.util.List;

/**
 * @author zhao
 */
@Controller
@RequestMapping("/task")
public class TaskController extends BaseController {

    @Autowired
    private TaskService taskService;

    @GetMapping
    public String myTask(Model model,HttpSession session) {
        /*Account account = getCurrentAccount(session);
        List<Task> taskList = taskService.findAllTaskByAccountId(account.getId());

        model.addAttribute("taskList", taskList);*/
        return "task/task";
    }

    @GetMapping("/list.json")
    @ResponseBody
    public JsonResult list(HttpSession session) {
        Account account = getCurrentAccount(session);
        List<Task> taskList = taskService.findAllTaskByAccountId(account.getId());
        return JsonResult.success(taskList);
    }

    @PostMapping("/new")
    public String saveNewTask(Integer accountId,
                              String title,
                              String finishTime,
                              String remindTime,
                              RedirectAttributes redirectAttributes) {

        try {
            taskService.saveNewTask(accountId, title, finishTime, remindTime, null, null);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        redirectAttributes.addFlashAttribute("message", "新建计划任务成功");
        return "redirect:/task";
    }

    @GetMapping("/{id:\\d+}/delete")
    @ResponseBody
    public JsonResult deleteTask(@PathVariable Integer id, HttpSession session) {
        permissions(session, id);
        taskService.deleteTaskById(id);
        return JsonResult.success();
    }

    @GetMapping("/{id:\\d+}/state/done")
    @ResponseBody
    public JsonResult done(@PathVariable Integer id, HttpSession session) {
        permissions(session, id);
        taskService.updateTaskState(id);
        return JsonResult.success();
    }

    @GetMapping("/{id:\\d+}/state/undone")
    @ResponseBody
    public JsonResult undone(@PathVariable Integer id, HttpSession session) {
        permissions(session, id);
        taskService.updateTaskState(id);
        return JsonResult.success();
    }

    @GetMapping("/{id:\\d+}/task.json")
    @ResponseBody
    public JsonResult findTaskById(@PathVariable Integer id) {
        Task task = taskService.findTaskById(id);
        return JsonResult.success(task);
    }

    @PostMapping("/edit")
    public String editTask(Integer id,
                           Integer accountId,
                           String title,
                           String finishTime,
                           String remindTime) {

        try {
            taskService.editTask(id,accountId, title, finishTime, remindTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "redirect:/task";
    }


    private Task permissions(HttpSession session, Integer id) {
        Account account = getCurrentAccount(session);
        Task task = taskService.findTaskById(id);
        if(task == null) {
            throw new NotFoundException();
        }
        if(!task.getAccountId().equals(account.getId())) {
            throw new ForbiddenException();
        }
        return task;
    }

}
