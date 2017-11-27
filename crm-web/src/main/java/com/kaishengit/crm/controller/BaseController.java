package com.kaishengit.crm.controller;

import com.kaishengit.crm.auth.ShiroUtils;
import com.kaishengit.crm.entity.Account;

import javax.servlet.http.HttpSession;

/**
 * @author zhao
 */
public abstract class BaseController {

    public Account getCurrentAccount() {
        return ShiroUtils.getCurrAccount();
    }

}
