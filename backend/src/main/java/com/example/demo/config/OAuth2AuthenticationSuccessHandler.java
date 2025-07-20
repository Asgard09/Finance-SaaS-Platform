package com.example.demo.config;

import com.example.demo.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class OAuth2AuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    private final String frontendUrl;
    private final UserService userService;

    /*Note*/
    public OAuth2AuthenticationSuccessHandler(@Value("${frontend.url}") String frontendUrl,
                                              UserService userService) {
        this.frontendUrl = frontendUrl;
        this.userService = userService;
    }
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();

        userService.findOrCreateUser(oauth2User);

        response.sendRedirect(frontendUrl);
    }
}
