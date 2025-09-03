package com.example.demo.controller;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.impl.JwtService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {
    
    private final JwtService jwtService;
    private final UserRepository userRepository;
    
    @Value("${jwt.cookie.secure:true}")
    private boolean secureCookie;
    
    @Value("${jwt.cookie.same-site:None}")
    private String sameSite;
    
    @PostMapping("/refresh")
    public ResponseEntity<Map<String, String>> refreshToken(
            HttpServletRequest request,
            HttpServletResponse response) {
        
        try {
            // Get refresh token from cookie
            String refreshToken = getTokenFromCookie(request, "refresh_token");
            
            if (refreshToken == null) {
                return ResponseEntity.status(401)
                        .body(Map.of("error", "Refresh token not found"));
            }
            
            // Extract user email from refresh token
            String userEmail = jwtService.extractUsername(refreshToken);
            Optional<User> userOpt = userRepository.findByEmail(userEmail);
            
            if (userOpt.isEmpty()) {
                return ResponseEntity.status(401)
                        .body(Map.of("error", "User not found"));
            }
            
            User user = userOpt.get();
            
            // Validate refresh token
            if (!jwtService.isTokenValid(refreshToken, user)) {
                return ResponseEntity.status(401)
                        .body(Map.of("error", "Invalid refresh token"));
            }
            
            // Generate new access token
            String newAccessToken = jwtService.generateToken(user);
            
            // Set new access token cookie
            setTokenCookie(response, newAccessToken); // 15 minutes
            
            Map<String, String> tokens = new HashMap<>();
            tokens.put("message", "Token refreshed successfully");
            
            return ResponseEntity.ok(tokens);
            
        } catch (Exception e) {
            log.error("Error refreshing token", e);
            return ResponseEntity.status(500)
                    .body(Map.of("error", "Token refresh failed"));
        }
    }
    
    @GetMapping("/verify")
    public ResponseEntity<Map<String, Object>> verifyToken(HttpServletRequest request) {
        try {
            String accessToken = getTokenFromCookie(request, "access_token");
            
            if (accessToken == null) {
                return ResponseEntity.status(401)
                        .body(Map.of("valid", false, "error", "No access token"));
            }
            
            String userEmail = jwtService.extractUsername(accessToken);
            Optional<User> userOpt = userRepository.findByEmail(userEmail);
            
            if (userOpt.isEmpty()) {
                return ResponseEntity.status(401)
                        .body(Map.of("valid", false, "error", "User not found"));
            }
            
            User user = userOpt.get();
            boolean isValid = jwtService.isTokenValid(accessToken, user);
            
            if (isValid) {
                Map<String, Object> response = new HashMap<>();
                response.put("valid", true);
                response.put("user", Map.of(
                        "id", user.getId(),
                        "email", user.getEmail(),
                        "firstName", user.getFirstName(),
                        "lastName", user.getLastName() != null ? user.getLastName() : "",
                        "picture", user.getPicture()
                ));
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(401)
                        .body(Map.of("valid", false, "error", "Invalid token"));
            }
            
        } catch (Exception e) {
            log.error("Error verifying token", e);
            return ResponseEntity.status(500)
                    .body(Map.of("valid", false, "error", "Token verification failed"));
        }
    }
    
    private String getTokenFromCookie(HttpServletRequest request, String cookieName) {
        if (request.getCookies() != null) {
            return Arrays.stream(request.getCookies())
                    .filter(cookie -> cookieName.equals(cookie.getName()))
                    .map(Cookie::getValue)
                    .findFirst()
                    .orElse(null);
        }
        return null;
    }
    
    private void setTokenCookie(HttpServletResponse response, String value) {
        String cookieValue = String.format("%s=%s; Path=/; Max-Age=%d; HttpOnly; %s SameSite=%s",
                "access_token", value, 900,
                secureCookie ? "Secure;" : "",
                sameSite);

        response.addHeader("Set-Cookie", cookieValue);
    }
}