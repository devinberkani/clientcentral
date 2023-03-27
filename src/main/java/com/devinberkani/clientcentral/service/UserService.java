package com.devinberkani.clientcentral.service;

import com.devinberkani.clientcentral.dto.UserDto;
import com.devinberkani.clientcentral.entity.User;

public interface UserService {

    User findUserById(Long userId);
    boolean existsByEmail(String email);
    void saveNewUser(UserDto userDto);
}
