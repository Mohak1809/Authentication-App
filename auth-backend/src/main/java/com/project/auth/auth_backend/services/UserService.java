package com.project.auth.auth_backend.services;

import com.project.auth.auth_backend.dtos.UserDto;

public interface UserService {

    // create user
    UserDto createUser(UserDto userDto);
    
    UserDto getUserByEmail(String email);

    UserDto updateUser(UserDto userDto, String userId);

    void deleteUser(String userId);

    UserDto getUserById(String userId);

    Iterable<UserDto> getAllUsers();
}
