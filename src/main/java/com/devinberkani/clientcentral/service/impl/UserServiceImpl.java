package com.devinberkani.clientcentral.service.impl;

import com.devinberkani.clientcentral.dto.UserDto;
import com.devinberkani.clientcentral.entity.User;
import com.devinberkani.clientcentral.repository.UserRepository;
import com.devinberkani.clientcentral.service.UserService;
import com.devinberkani.clientcentral.util.SecurityUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // returns true if user already exists, false otherwise
    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    // handle find user by email
    @Override
    public User findUserByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }

    // handling registering new user
    @Override
    public void saveNewUser(UserDto userDto) {
        User user = new User();
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setEmail(userDto.getEmail());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        userRepository.save(user);
    }

    @Override
    public User getCurrentUser() {
        // Get the current user from SecurityUtils
        org.springframework.security.core.userdetails.User currentUser = SecurityUtils.getCurrentUser();

        // Check if the current user is not null and has a non-null username
        return currentUser != null && currentUser.getUsername() != null ? findUserByEmail(currentUser.getUsername()) : null;
    }
}
