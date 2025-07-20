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
@RequiredArgsConstructor
@SuppressWarnings("unused")
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

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAccount(
            @PathVariable Long id, @AuthenticationPrincipal OAuth2User principal) {
        if (principal == null) {
            return ResponseEntity.status(401).build();
        }
        
        try {
            User user = userService.findOrCreateUser(principal);
            accountService.deleteAccount(id, user.getId());
            return ResponseEntity.ok("Account deleted successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    @DeleteMapping("/deleteAll")
    public ResponseEntity<String> deleteAllAccounts(@AuthenticationPrincipal OAuth2User principal) {
        if (principal == null) {
            return ResponseEntity.status(401).build();
        }
        
        User user = userService.findOrCreateUser(principal);
        accountService.deleteAllAccounts(user.getId());
        return ResponseEntity.ok("All accounts deleted successfully");
    }
}
