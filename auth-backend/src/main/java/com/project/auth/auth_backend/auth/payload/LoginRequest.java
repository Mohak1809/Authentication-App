package com.project.auth.auth_backend.auth.payload;

public record LoginRequest(
    String email,
    String password
) {

}
