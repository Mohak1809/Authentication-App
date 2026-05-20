package com.project.auth.auth_backend.entities;

import java.time.Instant;
import java.util.UUID;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "refresh_tokens", indexes = {
        @Index(name = "refresh_token_jti_index", columnList = "jti", unique = true),
        @Index(name = "refresh_token_user_id_index", columnList = "user_id")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(name = "jti", nullable = false, unique = true, updatable = false)
    private String jti;
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, updatable = false)
    private User user;
    @Column(updatable = false, nullable = false)
    private Instant createdAt;
    @Column(nullable = false)
    private Instant expiresAt;
    @Column(nullable = false)
    private boolean revoked;

    // private String refreshToken;
    private String replacedByToken;

}
