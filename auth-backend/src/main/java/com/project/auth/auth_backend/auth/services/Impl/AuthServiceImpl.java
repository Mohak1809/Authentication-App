package com.project.auth.auth_backend.auth.services.Impl;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.project.auth.auth_backend.auth.config.AppConstants;
import com.project.auth.auth_backend.auth.payload.UserDto;
import com.project.auth.auth_backend.auth.repositories.RoleRepository;
import com.project.auth.auth_backend.auth.services.AuthService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserServiceImpl userService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDto registerUser(UserDto userDto) {

        // verify email and password
        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
        return userService.createUser(userDto);
    }
    
    public UserDto registerAdmin(UserDto userDto) {
        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
        return userService.createAdmin(userDto);
    }

}
