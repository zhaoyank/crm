package com.kaishengit.crm.controller;

import com.kaishengit.crm.entity.Account;

import javax.servlet.http.HttpSession;

/**
 * @author zhao
 */
public abstract class BaseController {

    public Account getCurrentAccount(HttpSession session) {
        return (Account) session.getAttribute("curr_account");
    }

}
