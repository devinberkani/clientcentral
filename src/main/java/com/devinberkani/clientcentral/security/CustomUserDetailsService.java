package com.devinberkani.clientcentral.security;

import com.devinberkani.clientcentral.entity.User;
import com.devinberkani.clientcentral.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /*
    * This method is responsible for loading user details by username (in this case, by email) for the purpose of
    * authentication and authorization. It searches the user repository for a user with the given email. If a user is found,
    * it creates a new UserDetails object with the user's email, hashed password, and an empty list of authorities (i.e.
    * roles).*/

    /*
    * If the user is not found, it throws a UsernameNotFoundException with an error message indicating that the username and
    * password provided are invalid.
    * */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findUserByEmail(email);
        if (user != null) {
            return new org.springframework.security.core.userdetails.User(
                    user.getEmail(),
                    user.getPassword(),
                    new ArrayList<>()
            );
        } else {
            throw new UsernameNotFoundException("Invalid username and password");
        }
    }

}
