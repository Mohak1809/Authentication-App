package com.project.auth.auth_backend.auth.config;

public class AppConstants {

    public static final String[] AUTH_PUBLIC_URLS = {
            "/api/v1/auth/**",
            "/v3/api-docs/**",
            "/swagger-ui.html",
            "/swagger-ui/**",
            "/error"
    };

    public static final String[] AUTH_ADMIN_URLS = {
        "/api/v1/users/**", "/api/v1/auth/register/admin"
    };

    public static final String Role_ADMIN = "ADMIN";
    public static final String Role_GUEST = "GUEST";
}
