package com.kaishengit.crm.controller;

import com.kaishengit.crm.entity.Account;
import com.kaishengit.crm.service.exception.ServiceException;
import com.kaishengit.crm.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

/**
 * 登录的Controller层
 * @author zhao
 */
@Controller
public class LoginController {

    @Autowired
    private AccountService accountService;

    @GetMapping("/")
    public String login() {
        return "login";
    }

    @PostMapping("/")
    public String login(String mobile, String password,
                        RedirectAttributes redirectAttributes,
                        HttpSession session) {
        try {
            Account account = accountService.login(mobile, password);
            session.setAttribute("curr_account",account);
            return "redirect:home";
        } catch (ServiceException ex) {
            redirectAttributes.addFlashAttribute("message", "账号或密码错误");
            return "redirect:/";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

    @GetMapping("/home")
    public String home() {
        return "home";
    }

}
