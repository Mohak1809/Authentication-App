package com.project.auth.auth_backend.services.Impl;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.project.auth.auth_backend.dtos.UserDto;
import com.project.auth.auth_backend.services.AuthService;

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

}
