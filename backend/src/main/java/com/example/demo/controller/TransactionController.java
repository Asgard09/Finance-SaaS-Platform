package com.example.demo.controller;

import com.example.demo.dto.TransactionDTO;
import com.example.demo.entity.Transaction;
import com.example.demo.entity.User;
import com.example.demo.mapper.TransactionMapper;
import com.example.demo.service.TransactionService;
import com.example.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/transaction")
@RequiredArgsConstructor
public class TransactionController {

    private final UserService userService;
    private final TransactionService transactionService;
    private final TransactionMapper transactionMapper;

    @PostMapping("/create")
    public ResponseEntity<TransactionDTO> createTransaction(
            @RequestBody TransactionDTO transactionDTO, @AuthenticationPrincipal OAuth2User principal) {
        if (principal == null) {
            return ResponseEntity.status(401).build();
        }
        User user = userService.findOrCreateUser(principal);
        Transaction saved = transactionService.createTransaction(transactionDTO, user);
        return ResponseEntity.ok(transactionMapper.toDTO(saved));
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<TransactionDTO>> getAllTransactions(
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date from,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date to,
            @RequestParam(required = false) Long accountId,
            @AuthenticationPrincipal OAuth2User principal) {
        if (principal == null) return ResponseEntity.status(401).build();
        User user = userService.findOrCreateUser(principal);
        return ResponseEntity.ok(transactionService.getAllTransactions(user.getId(), from, to, accountId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransactionDTO> getTransaction(
            @PathVariable Long id, @AuthenticationPrincipal OAuth2User principal) {
        if (principal == null) {
            return ResponseEntity.status(401).build();
        }
        
        try {
            User user = userService.findOrCreateUser(principal);
            TransactionDTO transaction = transactionService.getTransactionById(id, user.getId());
            return ResponseEntity.ok(transaction);
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<TransactionDTO> updateTransaction(
            @PathVariable Long id,
            @RequestBody TransactionDTO transactionDTO,
            @AuthenticationPrincipal OAuth2User principal) {
        if (principal == null) {
            return ResponseEntity.status(401).build();
        }
        
        try {
            User user = userService.findOrCreateUser(principal);
            Transaction updated = transactionService.updateTransaction(id, transactionDTO, user);
            return ResponseEntity.ok(transactionMapper.toDTO(updated));
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTransaction(
            @PathVariable Long id, @AuthenticationPrincipal OAuth2User principal) {
        if (principal == null) {
            return ResponseEntity.status(401).build();
        }
        
        try {
            User user = userService.findOrCreateUser(principal);
            transactionService.deleteTransaction(id, user.getId());
            return ResponseEntity.ok("Transaction deleted successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    @DeleteMapping("/deleteAll")
    public ResponseEntity<String> deleteAllTransactions(@AuthenticationPrincipal OAuth2User principal) {
        if (principal == null) {
            return ResponseEntity.status(401).build();
        }
        
        User user = userService.findOrCreateUser(principal);
        transactionService.deleteAllTransactions(user.getId());
        return ResponseEntity.ok("All transactions deleted successfully");
    }

    @PostMapping("/bulk-create")
    public ResponseEntity<List<TransactionDTO>> createTransactionsBulk(
            @RequestBody List<TransactionDTO> transactionDTOs, @AuthenticationPrincipal OAuth2User principal) {
        if (principal == null) {
            return ResponseEntity.status(401).build();
        }
        
        User user = userService.findOrCreateUser(principal);
        List<Transaction> savedTransactions = transactionService.createTransactionsBulk(transactionDTOs, user);
        return ResponseEntity.ok(transactionMapper.toDtoList(savedTransactions));
    }
}
