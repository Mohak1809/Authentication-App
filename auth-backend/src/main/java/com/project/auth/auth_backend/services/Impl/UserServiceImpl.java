package com.project.auth.auth_backend.services.Impl;

import java.time.Instant;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.project.auth.auth_backend.dtos.UserDto;
import com.project.auth.auth_backend.entities.Provider;
import com.project.auth.auth_backend.entities.User;
import com.project.auth.auth_backend.exceptions.ResourceNotFoundException;
import com.project.auth.auth_backend.helpers.Userhelper;
import com.project.auth.auth_backend.repositories.UserRepository;
import com.project.auth.auth_backend.services.UserService;

import jakarta.transaction.Transactional;
import lombok.*;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public UserDto createUser(UserDto userDto) {
        if (userDto.getEmail() == null || userDto.getEmail().isBlank()) {
            throw new IllegalArgumentException("Email is required");
        }
        if (userRepository.existsByEmail(userDto.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }

        // model mapper lib is used to convert dto to entity and vice versa
        User user = modelMapper.map(userDto, User.class);
        user.setEnable(true);

        user.setProvider(userDto.getProvider() != null ? userDto.getProvider() : Provider.LOCAL);
        // role assign to user for authorization

        User savedUser = userRepository.save(user);

        return modelMapper.map(savedUser, UserDto.class);
    }

    @Override
    public UserDto getUserByEmail(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return modelMapper.map(user, UserDto.class);
    }

    @Override
    public UserDto updateUser(UserDto userDto, String userId) {
        UUID uId = Userhelper.parseUUID(userId);
        User existUser = userRepository.findById(uId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with given id"));
        if (userDto.getName() != null) {
            existUser.setName(userDto.getName());
        }

        // TODO: change password update logic
        if (userDto.getPassword() != null) {
            existUser.setPassword(userDto.getPassword());
        }
        if (userDto.getImage() != null) {
            existUser.setImage(userDto.getImage());
        }
        if (userDto.getProvider() != null) {
            existUser.setProvider(userDto.getProvider());
        }
        existUser.setEnable(userDto.getEnable());
        existUser.setUpdatedAt(Instant.now());
        User updatedUser = userRepository.save(existUser);
        return modelMapper.map(updatedUser, UserDto.class);
    }

    @Override
    public void deleteUser(String userId) {
        UUID uId = Userhelper.parseUUID(userId);
        User user = userRepository.findById(uId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with given id"));
        userRepository.delete(user);

    }

    @Override
    public UserDto getUserById(String userId) {
        User user = userRepository.findById(Userhelper.parseUUID(userId))
                .orElseThrow(() -> new ResourceNotFoundException("User not found with given id"));
        return modelMapper.map(user, UserDto.class);
    }

    @Override
    @Transactional
    public Iterable<UserDto> getAllUsers() {

        return userRepository
                .findAll()
                .stream()
                .map(user -> modelMapper.map(user, UserDto.class))
                .toList();
    }

}
