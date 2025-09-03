package com.example.demo.config;

import com.example.demo.entity.User;
import com.example.demo.service.UserService;
import com.example.demo.service.impl.JwtService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Component
@Slf4j
@SuppressWarnings("unused")
public class OAuth2AuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    private final String frontendUrl;
    private final UserService userService;
    private final JwtService jwtService;
    
    @Value("${jwt.cookie.secure:true}")
    private boolean secureCookie;
    
    @Value("${jwt.cookie.same-site:None}")
    private String sameSite;

    /*Note*/
    public OAuth2AuthenticationSuccessHandler(@Value("${frontend.url}") String frontendUrl,
                                              UserService userService, JwtService jwtService) {
        this.frontendUrl = frontendUrl;
        this.userService = userService;
        this.jwtService = jwtService;
    }
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        try {
            OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();

            // Find or create user
            User user = userService.findOrCreateUser(oauth2User);

            // Generate JWT tokens
            String accessToken = jwtService.generateToken(user);
            String refreshToken = jwtService.generateRefreshToken(user);

            // Set tokens as HTTP-only cookies
            setTokenCookie(response, "access_token", accessToken, 15 * 60); // 15 minutes
            setTokenCookie(response, "refresh_token", refreshToken, 7 * 24 * 60 * 60); // 7 days

            String redirectUrl = UriComponentsBuilder.fromUriString(frontendUrl)
                    .queryParam("auth", "success")
                    .build()
                    .toUriString();

            log.info("OAuth2 authentication successful for user: {}", user.getEmail());
            response.sendRedirect(redirectUrl);

        } catch (Exception e) {
            log.error("Error during OAuth2 authentication success handling", e);
            String errorUrl = UriComponentsBuilder.fromUriString(frontendUrl)
                    .queryParam("error", URLEncoder.encode("Authentication processing failed", StandardCharsets.UTF_8))
                    .build()
                    .toUriString();
            response.sendRedirect(errorUrl);
        }
    }

    private void setTokenCookie(HttpServletResponse response, String name, String value, int maxAgeSeconds) {
        Cookie cookie = new Cookie(name, value);
        cookie.setHttpOnly(true);
        cookie.setSecure(secureCookie);
        cookie.setPath("/");
        cookie.setMaxAge(maxAgeSeconds);

        // Set SameSite attribute manually
        String cookieValue = String.format("%s=%s; Path=/; Max-Age=%d; HttpOnly; %s SameSite=%s",
                name, value, maxAgeSeconds,
                secureCookie ? "Secure;" : "",
                sameSite);

        response.addHeader("Set-Cookie", cookieValue);
    }


}
