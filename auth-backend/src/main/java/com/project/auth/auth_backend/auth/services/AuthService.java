package com.project.auth.auth_backend.auth.services;

import com.project.auth.auth_backend.auth.payload.UserDto;

public interface AuthService {
    UserDto registerUser(UserDto userDto);
    UserDto registerAdmin(UserDto userDto);
    // login user
}
