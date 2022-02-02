package com.davinci.web;

import com.davinci.domain.User;

import javax.servlet.http.HttpSession;

public class HttpSessionUtils {

    public static final String SESSION_USER_ID = "sessionUser";

    public static Boolean isLoginUser(HttpSession httpSession) {
        return httpSession.getAttribute(SESSION_USER_ID) != null;
    }

    public static User getUserFormSession(HttpSession httpSession) {
        return (User) httpSession.getAttribute(SESSION_USER_ID);
    }

}
