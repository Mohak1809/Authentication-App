package com.project.auth.auth_backend.dtos;

import java.time.*;
import java.util.*;

import com.project.auth.auth_backend.entities.Provider;
import com.project.auth.auth_backend.entities.Role;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {
    private UUID id;
    private String email;
    private String name;
    private String password;
    private String image;
    private Boolean enable = true;
    private Instant createdAt = Instant.now();
    private Instant updatedAt = Instant.now();
    private Provider provider = Provider.LOCAL;
    private Set<Role> roles = new HashSet<>();
}


// Difference between UserDto and User entity:
// 1. UserDto is a Data Transfer Object (DTO) used for transferring data between layers of the application, while User is an entity that represents a table in the database.
// 2. UserDto typically contains only the fields that are needed for a specific operation (e.g., registration, login), while User may contain additional fields such as id, createdAt, updatedAt, etc.