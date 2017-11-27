package com.kaishengit.crm.auth;

import com.kaishengit.crm.entity.Account;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

/**
 * @author zhao
 */
public class ShiroUtils {

    public static Account getCurrAccount() {
        return (Account) getSubject().getPrincipal();
    }

    public static Subject getSubject() {
        return SecurityUtils.getSubject();
    }
}
