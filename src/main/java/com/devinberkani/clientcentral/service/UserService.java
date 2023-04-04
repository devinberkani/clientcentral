package com.devinberkani.clientcentral.service;

import com.devinberkani.clientcentral.dto.UserDto;
import com.devinberkani.clientcentral.entity.User;

public interface UserService {

    boolean existsByEmail(String email);
    User findUserByEmail(String email);
    void saveNewUser(UserDto userDto);
    User getCurrentUser();
}
