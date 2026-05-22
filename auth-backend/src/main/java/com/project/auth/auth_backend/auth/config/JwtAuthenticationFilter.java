package com.project.auth.auth_backend.auth.config;

import java.io.IOException;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.project.auth.auth_backend.auth.helpers.Userhelper;
import com.project.auth.auth_backend.auth.repositories.UserRepository;
import com.project.auth.auth_backend.auth.services.Impl.JwtService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String header = request.getHeader("Authorization");
        logger.info("Authorization header: " + header);

        if (header != null && header.startsWith("Bearer")) {
            // token extract and validate then authenticate the user
            // then security context holder set authentication
            String token = header.substring(7);
            // check token type

            try {

                if (!jwtService.isAccessToken(token)) {
                    filterChain.doFilter(request, response);
                    return;
                }

                Jws<Claims> parse = jwtService.parse(token);

                Claims payload = parse.getPayload();

                String userId = payload.getSubject();
                UUID userUuid = Userhelper.parseUUID(userId);

                userRepository.findById(userUuid)
                        .ifPresent(user -> {

                            // check if user is enabled or not, if not enabled then skip authentication and
                            // continue filter chain
                            // if (!user.isEnabled()) {
                            // try {
                            // filterChain.doFilter(request, response);
                            // } catch (IOException e) {
                            // e.printStackTrace();
                            // } catch (ServletException e) {
                            // e.printStackTrace();
                            // }
                            // return;
                            // }

                            if (user.isEnabled()) {
                                // if user is not enabled then skip authentication and continue filter chain
                                // authorities will have roles of the user and we will set them in the
                                // authentication token
                                List<GrantedAuthority> authorities = user.getRoles() == null ? List.of()
                                        : user.getRoles().stream()
                                                .map(role -> new SimpleGrantedAuthority(role.getName()))
                                                .collect(Collectors.toList());
                                // create authentication token
                                // we can use email as principal and null as credentials because we don't need
                                // them for authentication
                                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                                        user.getEmail(), null, authorities);

                                // set details
                                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                                // Set the authentication in the SecurityContext

                                if (SecurityContextHolder.getContext().getAuthentication() == null)
                                    SecurityContextHolder.getContext().setAuthentication(authentication);
                            }
                        });

            } catch (ExpiredJwtException e) {
                request.setAttribute("error", "Token Expired");
                // e.printStackTrace();

            } catch (JwtException e) {
                request.setAttribute("error", "Token Invalid !!");
                // e.printStackTrace();

            } catch (Exception e) {
                request.setAttribute("error", "Token Invalid");
                // e.printStackTrace();

            }

        }
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return request.getRequestURI().startsWith("/api/v1/auth/");
        // we don't want to filter authentication requests because they don't have token
        // in the header and we will authenticate them in the controller
        // it will not run for /api/v1/auth/register and /api/v1/auth/login but will run
        // for all other requests
    }

}
