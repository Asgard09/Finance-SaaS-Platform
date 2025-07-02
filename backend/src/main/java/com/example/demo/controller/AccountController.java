package com.example.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/account")
@CrossOrigin(origins = "http://localhost:3000")
public class AccountController {

    @GetMapping("/me")
    public ResponseEntity<Map<String, Object>> getAccountInfo(@AuthenticationPrincipal OAuth2User principal){
        if (principal == null) return ResponseEntity.status(401).body(null);

        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("id", principal.getAttribute("sub"));
        userInfo.put("email", principal.getAttribute("email"));
        userInfo.put("name", principal.getAttribute("name"));
        userInfo.put("firstName", principal.getAttribute("given_name"));
        userInfo.put("lastName", principal.getAttribute("family_name"));
        userInfo.put("picture", principal.getAttribute("picture"));
        userInfo.put("authenticated", true);

        return ResponseEntity.ok(userInfo);
    }
}
