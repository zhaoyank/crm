package com.kaishengit.crm.controller.interceptor;

import com.kaishengit.crm.entity.Account;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author zhao
 */
public class LoginInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String url = request.getRequestURI();

        if (url.startsWith("/static")) {
            return true;
        }
        if ("".equals(url) || "/".equals(url)) {
            return true;
        }

        Account account = (Account) request.getSession().getAttribute("curr_account");
        if(account != null) {
            return true;
        }
        response.sendRedirect("/");

        return false;
    }
}
