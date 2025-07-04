package com.example.demo.controller;

import com.example.demo.dto.AccountDTO;
import com.example.demo.entity.Account;
import com.example.demo.mapper.AccountMapper;
import com.example.demo.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/account")
@CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;
    private final AccountMapper accountMapper;

    @PostMapping("/create")
    public ResponseEntity<AccountDTO> createAccount(
            @RequestBody AccountDTO accountDTO, @AuthenticationPrincipal OAuth2User principal){
        if (principal == null) {
            return ResponseEntity.status(401).build();
        }
        Account saved = accountService.createAccount(accountDTO);
        return ResponseEntity.ok(accountMapper.toDTO(saved));
    }
}
