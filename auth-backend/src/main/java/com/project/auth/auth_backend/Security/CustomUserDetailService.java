package com.project.auth.auth_backend.Security;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.project.auth.auth_backend.repositories.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username).orElseThrow(() -> new BadCredentialsException("Invalid email or password"));
    }
    

    
}
