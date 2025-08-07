package com.example.demo.config;

import com.example.demo.entity.User;
import com.example.demo.service.UserService;
import com.example.demo.service.impl.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@Component
public class OAuth2AuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    private final String frontendUrl;
    private final UserService userService;
    private final JwtService jwtService;

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
        OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();

        User user = userService.findOrCreateUser(oauth2User);

        //Generate Jwt tokens
        String accessToken = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        String  redirectUrl = UriComponentsBuilder.fromUriString(frontendUrl)
                        .queryParam("auth", "success")
                        .build()
                        .toUriString();
        response.sendRedirect(redirectUrl);
    }


}
