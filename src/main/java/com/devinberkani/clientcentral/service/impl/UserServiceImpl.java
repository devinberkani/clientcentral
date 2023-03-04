package com.devinberkani.clientcentral.service.impl;

import com.devinberkani.clientcentral.dto.UserDto;
import com.devinberkani.clientcentral.entity.User;
import com.devinberkani.clientcentral.repository.UserRepository;
import com.devinberkani.clientcentral.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    // still need password encoder

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    // returns true if user already exists, false otherwise
    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public void saveNewUser(UserDto userDto) {
        User user = new User();
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword());
        userRepository.save(user);
    }
}
