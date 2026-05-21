package com.project.auth.auth_backend.services;

import com.project.auth.auth_backend.dtos.UserDto;

public interface AuthService {
    UserDto registerUser(UserDto userDto);
    UserDto registerAdmin(UserDto userDto);
    // login user
}
