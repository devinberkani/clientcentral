package com.devinberkani.clientcentral.repository;

import com.devinberkani.clientcentral.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);
    User findUserById(Long id);
    User findUserByEmail(String email);
}
