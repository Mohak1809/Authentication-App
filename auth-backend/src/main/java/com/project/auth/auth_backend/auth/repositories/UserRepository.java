package com.project.auth.auth_backend.auth.repositories;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

import com.project.auth.auth_backend.auth.entities.User;

public interface UserRepository extends JpaRepository<User, UUID> {

    // Custom finder method to find a user by email. Custom finder methods are automatically implemented by Spring Data JPA based on the method name.
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);

}
