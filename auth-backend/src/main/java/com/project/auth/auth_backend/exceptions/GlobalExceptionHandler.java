package com.project.auth.auth_backend.exceptions;

import javax.security.auth.login.CredentialException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.project.auth.auth_backend.dtos.ApiError;
import com.project.auth.auth_backend.dtos.ErrorResponse;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    // You can add methods here to handle specific exceptions and return appropriate
    // responses

    // resource not found exception handler
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(ResourceNotFoundException exception) {
        ErrorResponse errorResponse = new ErrorResponse(exception.getMessage(), HttpStatus.NOT_FOUND);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException exception) {
        ErrorResponse errorResponse = new ErrorResponse(exception.getMessage(), HttpStatus.BAD_REQUEST);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler({
            UsernameNotFoundException.class,
            BadCredentialsException.class,
            CredentialException.class,
            DisabledException.class
    })
    public ResponseEntity<ApiError> handleAuthException(Exception e, HttpServletRequest request) {
        logger.info("Exception : {}", e.getClass().getName());
        var apiError = ApiError.of(e.getMessage(), HttpStatus.BAD_REQUEST.value(), "Bad Request",
                request.getRequestURI());
        return ResponseEntity.badRequest().body(apiError);
    }
}
