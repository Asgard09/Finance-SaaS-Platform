package com.example.demo.controller;

import com.example.demo.dto.UserDTO;
import com.example.demo.entity.User;
import com.example.demo.mapper.UserMapper;
import com.example.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@SuppressWarnings("unused")
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @GetMapping("/profile")
    public ResponseEntity<UserDTO> getCurrentUser(@AuthenticationPrincipal OAuth2User principal) {
        if (principal == null) {
            return ResponseEntity.status(401).body(null);
        }

        User user = userService.findOrCreateUser(principal);
        UserDTO userDTO = userMapper.toDTO(user);
        return ResponseEntity.ok(userDTO);
    }

    @GetMapping("/token-info")
    public ResponseEntity<Map<String, Object>> getTokenInfo(
            @RegisteredOAuth2AuthorizedClient OAuth2AuthorizedClient authorizedClient) {

        if (authorizedClient == null) {
            return ResponseEntity.status(401).body(null);
        }

        OAuth2AccessToken accessToken = authorizedClient.getAccessToken();

        Map<String, Object> tokenInfo = new HashMap<>();
        tokenInfo.put("tokenValue", accessToken.getTokenValue()); // THE ACTUAL TOKEN
        tokenInfo.put("tokenType", accessToken.getTokenType().getValue());
        tokenInfo.put("expiresAt", accessToken.getExpiresAt());
        tokenInfo.put("scopes", accessToken.getScopes());
        tokenInfo.put("issuedAt", accessToken.getIssuedAt());

        return ResponseEntity.ok(tokenInfo);
    }
}
