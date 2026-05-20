package com.project.auth.auth_backend.dtos;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

public record ApiError(
    int status,
    String error,
    String message,
    String path,
    OffsetDateTime timestamp
) {

    public static ApiError of(String message, int status, String error, String path) {
        return new ApiError(status, error, message, path, OffsetDateTime.now(ZoneOffset.UTC));
    }
    
    
}
