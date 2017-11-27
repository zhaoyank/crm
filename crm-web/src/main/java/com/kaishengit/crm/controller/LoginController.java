package com.kaishengit.crm.controller;

import com.kaishengit.crm.entity.Account;
import com.kaishengit.crm.entity.Dept;
import com.kaishengit.crm.service.AccountService;
import com.kaishengit.crm.service.DeptService;
import com.kaishengit.util.JsonResult;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.util.SavedRequest;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * 登录的Controller层
 * @author zhao
 */
@Controller
public class LoginController extends BaseController {

    @Autowired
    private DeptService deptService;

    @GetMapping("/")
    public String login() {
        Subject subject = SecurityUtils.getSubject();
        // 只有退出当前用户,才能进入登录页
        if(subject.isAuthenticated()) {
            return "redirect:/home";
        }
        if(!subject.isAuthenticated() && subject.isRemembered()) {
            return "redirect:/home";
        }
        return "login";
    }

    @PostMapping("/")
    public String login(String mobile, String password, boolean rememberMe,
                        HttpServletRequest request,
                        HttpSession httpSession,
                        RedirectAttributes redirectAttributes) {
        try {
            Subject subject = SecurityUtils.getSubject();
            UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(mobile,new Md5Hash(password).toString(), rememberMe);
            subject.login(usernamePasswordToken);



            // 回到登录之前要访问url
            String url = "/home";
            SavedRequest savedRequest = WebUtils.getSavedRequest(request);
            if(savedRequest != null) {
                url = savedRequest.getRequestUrl();
            }
            return "redirect:" + url;
        } catch (AuthenticationException ex) {
            redirectAttributes.addFlashAttribute("message", "账号或密码错误");
            return "redirect:/";
        }
    }

    @GetMapping("/deptNames.json")
    @ResponseBody
    public JsonResult getDeptNames() {
        Account account = getCurrentAccount();
        List<Dept> deptList = deptService.findByUserId(account.getId());

        String deptName = "";
        for (Dept dept : deptList) {
            deptName += (dept.getDeptName() + ",");
        }
        deptName = deptName.substring(0, deptName.lastIndexOf(","));
        return JsonResult.success(deptName);
    }

    @GetMapping("/logout")
    public String logout() {
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        return "redirect:/";
    }

    @GetMapping("/home")
    public String home() {
        return "home";
    }

}
