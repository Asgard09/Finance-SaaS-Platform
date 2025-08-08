package com.example.demo.controller;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.impl.JwtService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
@Slf4j
@RequiredArgsConstructor
public class AuthController {
    private final JwtService jwtService;
    private final UserRepository userRepository;

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
                        "lastName", user.getLastName(),
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
}
