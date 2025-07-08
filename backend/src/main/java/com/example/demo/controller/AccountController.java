package com.example.demo.controller;

import com.example.demo.dto.AccountDTO;
import com.example.demo.entity.Account;
import com.example.demo.entity.User;
import com.example.demo.mapper.AccountMapper;
import com.example.demo.service.AccountService;
import com.example.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/account")
@CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor
public class AccountController {

    private final UserService userService;
    private final AccountService accountService;
    private final AccountMapper accountMapper;

    @PostMapping("/create")
    public ResponseEntity<AccountDTO> createAccount(
            @RequestBody AccountDTO accountDTO, @AuthenticationPrincipal OAuth2User principal){
        if (principal == null) {
            return ResponseEntity.status(401).build();
        }
        User user = userService.findOrCreateUser((principal));
        Account saved = accountService.createAccount(accountDTO, user);
        return ResponseEntity.ok(accountMapper.toDTO(saved));
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<AccountDTO>> getAllAccountOfCurrentUser(@AuthenticationPrincipal OAuth2User principal){
        if (principal == null) return ResponseEntity.status(401).build();
        User user = userService.findOrCreateUser(principal);
        return ResponseEntity.ok(accountService.getAllAccount(user.getId()));
    }
}
