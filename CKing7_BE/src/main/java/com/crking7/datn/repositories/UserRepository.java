package com.crking7.datn.repositories;

import com.crking7.datn.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {


    Page<User> findAll(Pageable pageable);
    Optional<User> findById(Long userId);
    User findByUsername(String username);
}
