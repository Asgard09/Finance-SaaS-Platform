package com.example.demo.config;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.impl.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull  FilterChain filterChain
    )throws ServletException, IOException {
        final String userEmail;

        // Skip JWT processing for OAuth2 endpoints
        if (request.getRequestURI().startsWith("/oauth2/") ||
                request.getRequestURI().startsWith("/login/oauth2/")) {
            filterChain.doFilter(request, response);
            return;
        }
        
        // First, try to get token from cookie
        String jwt = getTokenFromCookie(request);
        log.info("Path: {}, JWT from cookie: {}", request.getRequestURI(), jwt != null ? "found" : "not found");

        try {
            userEmail = jwtService.extractUsername(jwt);

            if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                User user = userRepository.findByEmail(userEmail).orElse(null);

                if (user != null && jwtService.isTokenValid(jwt, user)) {
                    // Create a simple OAuth2User implementation that matches what controllers expect
                    UsernamePasswordAuthenticationToken authToken = getUsernamePasswordAuthenticationToken(user);
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        } catch (Exception e) {
            log.error("Cannot set user authentication: {}", e.getMessage());
        }

        filterChain.doFilter(request, response);
    }

    private static UsernamePasswordAuthenticationToken getUsernamePasswordAuthenticationToken(User user) {
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("sub", user.getEmail());
        attributes.put("name", user.getFirstName() + " " + user.getLastName());
        attributes.put("given_name", user.getFirstName());
        attributes.put("family_name", user.getLastName());
        attributes.put("email", user.getEmail());
        attributes.put("picture", user.getPicture());

        // This is the key part - create an OAuth2User that the controllers can use
        return getUsernamePasswordAuthenticationToken(attributes);
    }

    private static UsernamePasswordAuthenticationToken getUsernamePasswordAuthenticationToken(Map<String, Object> attributes) {
        org.springframework.security.oauth2.core.user.DefaultOAuth2User oauth2User =
            new org.springframework.security.oauth2.core.user.DefaultOAuth2User(
                Collections.emptyList(),
                    attributes,
                "email"  // The key for the name attribute
            );

        return new UsernamePasswordAuthenticationToken(
                oauth2User,
                null,
                oauth2User.getAuthorities()
        );
    }

    private String getTokenFromCookie(HttpServletRequest request) {
        if (request.getCookies() != null) {
            return Arrays.stream(request.getCookies())
                    .filter(cookie -> "access_token".equals(cookie.getName()))
                    .map(Cookie::getValue)
                    .findFirst()
                    .orElse(null);
        }
        return null;
    }
}
