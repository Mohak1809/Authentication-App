package com.project.auth.auth_backend.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.auth.auth_backend.entities.RefreshToken;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, UUID> {

    Optional<RefreshToken> findByJti(String jti);
    
}
