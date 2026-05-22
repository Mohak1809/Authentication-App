package com.project.auth.auth_backend.auth.payload;

public record TokenResponse(
    String accessToken,
    String refreshToken,
    long exiresIn,
    String tokenType,
    UserDto user
) {

    public static TokenResponse of(String accessToken, String refreshToken, long expiresIn, UserDto user) {
        return new TokenResponse(accessToken, refreshToken, expiresIn, "Bearer", user);
    }

}
