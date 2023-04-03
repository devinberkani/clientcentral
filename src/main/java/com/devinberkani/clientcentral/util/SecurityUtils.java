package com.devinberkani.clientcentral.util;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

public class SecurityUtils {

    public static User getCurrentUser() {
        // from spring security - contains username, password and roles
        Object principle = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principle instanceof User) {
            return (User) principle; // if the principle is an instance of User, cast principle to User and return it, otherwise return null
        }
        return null;
    }

}
