package com.project.auth.auth_backend.Security;

import java.io.IOException;
import java.time.Instant;
import java.util.HashSet;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.project.auth.auth_backend.config.AppConstants;
import com.project.auth.auth_backend.entities.Provider;
import com.project.auth.auth_backend.entities.RefreshToken;
import com.project.auth.auth_backend.entities.Role;
import com.project.auth.auth_backend.entities.User;
import com.project.auth.auth_backend.repositories.RefreshTokenRepository;
import com.project.auth.auth_backend.repositories.RoleRepository;
import com.project.auth.auth_backend.repositories.UserRepository;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final JwtService jwtService;
    private final CookieService cookieService;
    private final RefreshTokenRepository refreshTokenRepository;

    @Value("${app.auth.frontend.success-redirect}")
    private String frontEndSuccessUrl;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {

        logger.info("Successful authentication");
        logger.info(authentication.toString());
        // new username
        // jwt token__ token ke sath frontend

        // refresh:
        // user refresh token ko revoke krdo jb logout kre

        OAuth2User oAuthuser = (OAuth2User) authentication.getPrincipal();

        // identify the user
        String registrationId = "unknown";
        if (authentication instanceof OAuth2AuthenticationToken token) {
            registrationId = token.getAuthorizedClientRegistrationId();
        }

        logger.info("Registration Id: " + registrationId);
        logger.info("User: " + oAuthuser.getAttributes().toString());

        User user;
        switch (registrationId) {
            case "google":
                String googleId = oAuthuser.getAttributes().getOrDefault("sub", "").toString();
                String googleEmail = oAuthuser.getAttributes().getOrDefault("email", "").toString();
                String googleName = oAuthuser.getAttributes().getOrDefault("name", "").toString();
                String googlePicture = oAuthuser.getAttributes().getOrDefault("picture", "").toString();

                user = userRepository.findByEmail(googleEmail)
                        .orElseGet(() -> {

                            User newUser = User.builder()
                                    .email(googleEmail)
                                    .name(googleName)
                                    .image(googlePicture)
                                    .enable(true)
                                    .provider(Provider.GOOGLE)
                                    .providerId(googleId)
                                    .build();
                            if (newUser.getRoles() == null) {
                                newUser.setRoles(new HashSet<>());
                            }
                            Role role = roleRepository.findByName("ROLE_" + AppConstants.Role_GUEST).orElse(null);
                            newUser.getRoles().add(role);

                            logger.info("Saving new user");

                            return userRepository.save(newUser);
                        });
                break;

            case "github":
                String name = oAuthuser.getAttributes().getOrDefault("login", "").toString();
                String email = (String) oAuthuser.getAttributes().get("email");
                String image = oAuthuser.getAttributes().getOrDefault("avatar_url", "").toString();
                String githubId = oAuthuser.getAttributes().getOrDefault("id", "").toString();
                if (email == null) {
                    email = name + "@github.com";
                }

                User newUser = User.builder()
                        .email(email)
                        .name(name)
                        .image(image)
                        .enable(true)
                        .provider(Provider.GITHUB)
                        .providerId(githubId)
                        .build();
                if (newUser.getRoles() == null) {
                    newUser.setRoles(new HashSet<>());
                }
                Role role = roleRepository.findByName("ROLE_" + AppConstants.Role_GUEST).orElse(null);
                newUser.getRoles().add(role);
                user = userRepository.findByEmail(email).orElseGet(() -> userRepository.save(newUser));

                break;

            default:
                throw new RuntimeException("Invalid registration id");
        }

        // jwt token__ token ke sath frontend
        // refresh token bana ke dunga

        String jti = UUID.randomUUID().toString();
        RefreshToken refreshToken = RefreshToken.builder()
                .jti(jti)
                .user(user)
                .revoked(false)
                .createdAt(Instant.now())
                .expiresAt(Instant.now().plusSeconds(jwtService.getRefreshTtlSeconds()))
                .build();

        refreshTokenRepository.save(refreshToken);
        String accessToken = jwtService.generateAccessToken(user);
        String refreshTokenValue = jwtService.generateRefreshToken(user, refreshToken.getJti());
        cookieService.attachRefreshCookie(response, refreshTokenValue, (int) jwtService.getRefreshTtlSeconds());

        // response.getWriter().write("Login successful");

        // redirescting to frontend
        response.sendRedirect(frontEndSuccessUrl);
    }

}
