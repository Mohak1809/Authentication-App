package com.project.auth.auth_backend.auth.services;

import com.project.auth.auth_backend.auth.payload.UserDto;

public interface UserService {

    // create user
    UserDto createUser(UserDto userDto);

    // create Admin
    UserDto createAdmin(UserDto userDto);
    
    UserDto getUserByEmail(String email);

    UserDto updateUser(UserDto userDto, String userId);

    void deleteUser(String userId);

    UserDto getUserById(String userId);

    Iterable<UserDto> getAllUsers();
}
