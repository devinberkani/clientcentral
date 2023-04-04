package com.devinberkani.clientcentral.util;

import com.devinberkani.clientcentral.entity.Client;
import com.devinberkani.clientcentral.entity.FileAttachment;
import com.devinberkani.clientcentral.entity.Note;
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

    public static boolean authenticateOwnership(Object object, Long currentUserId) {

        Long ownerId = Long.MIN_VALUE;

        if (object instanceof com.devinberkani.clientcentral.entity.User) {
            ownerId = ((com.devinberkani.clientcentral.entity.User) object).getId();
        } else if (object instanceof Client) {
            ownerId = ((Client) object).getUser().getId();
        } else if (object instanceof Note) {
            ownerId = ((Note) object).getClient().getUser().getId();
        } else if (object instanceof FileAttachment) {
            ownerId = ((FileAttachment) object).getNote().getClient().getUser().getId();
        }

        return ownerId.equals(currentUserId);
    }

}
