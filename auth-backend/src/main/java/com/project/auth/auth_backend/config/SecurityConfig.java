package com.project.auth.auth_backend.config;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.project.auth.auth_backend.Security.JwtAuthenticationFilter;
import com.project.auth.auth_backend.dtos.ApiError;

import lombok.experimental.var;
import tools.jackson.databind.ObjectMapper;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;
    private AuthenticationSuccessHandler successHandler;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter,
            AuthenticationSuccessHandler successHandler) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.successHandler = successHandler;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.csrf(AbstractHttpConfigurer::disable)
                // csrf is disabled for testing purposes, but in production, it should be
                // enabled and properly configured.
                // csrf is a security feature that helps protect against cross-site request
                // forgery attacks.
                // It works by generating a unique token for each user session and requiring
                // that token to be included in any state-changing requests (like POST, PUT,
                // DELETE).
                // This ensures that the request is coming from an authenticated user and not
                // from a malicious site.

                .cors(Customizer.withDefaults())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .authorizeHttpRequests(authorizeHttpRequests -> authorizeHttpRequests
                        .requestMatchers(AppConstants.AUTH_PUBLIC_URLS).permitAll()
                        .anyRequest().authenticated())

                .oauth2Login(oauth2 -> oauth2.successHandler(successHandler)
                        .failureHandler(null))
                .logout(AbstractHttpConfigurer::disable)
                .exceptionHandling(ex -> ex.authenticationEntryPoint((request, response, e) -> {
                    // error message in json format
                    e.printStackTrace();
                    response.setStatus(401);
                    response.setContentType("application/json");
                    String message = e.getMessage();

                    String error = (String) request.getAttribute("error");
                    if (error != null) {
                        message = error;
                    }

                    var apiError = ApiError.of(message, HttpStatus.UNAUTHORIZED.value(), "Unauthorized",
                            request.getRequestURI());
                    var objectMapper = new ObjectMapper();
                    response.getWriter().write(objectMapper.writeValueAsString(apiError));

                }))
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        // ADD JWT Authentication Filter before the UsernamePasswordAuthenticationFilter
        // in the filter chain.
        // This ensures that the JWT token is validated before any authentication
        // attempt is made using username and password
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();

    }

    // @Bean
    // public UserDetailsService users() {
    // User.UserBuilder userBuilder = User.withDefaultPasswordEncoder();
    // UserDetails user1 =
    // userBuilder.username("Mohak").password("abc").roles("ADMIN").build();
    // UserDetails user2 =
    // userBuilder.username("Mehul").password("abc").roles("USER").build();

    // return new InMemoryUserDetailsManager(user1, user2);
    // }

    @Bean
    public CorsConfigurationSource corsConfigurationSource(
        @Value("${app.cors.front-end-url}")  String corsurl
    ) {
        String[] urls = corsurl.trim().split(",");
        var config = new CorsConfiguration();
        config.setAllowedOrigins(Arrays.asList(urls));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH", "HEAD"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);
        var source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

}

// This configuration class defines a bean for UserDetailsService, which is used
// by Spring Security to load user-specific data. In this case, it creates an
// in-memory user store with two users: "Mohak" with the role "ADMIN" and
// "Mehul" with the role "USER". The passwords are encoded using the default
// password encoder provided by Spring Security.
// now we want users who signed up to be able to log in.