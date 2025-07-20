package com.example.demo.controller;

import com.example.demo.dto.SummaryDTO;
import com.example.demo.entity.User;
import com.example.demo.service.SummaryService;
import com.example.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

@RestController
@RequestMapping("/api/summary")
@RequiredArgsConstructor
@SuppressWarnings("unused")
public class SummaryController {

    private final UserService userService;
    private final SummaryService summaryService;

    @GetMapping
    public ResponseEntity<SummaryDTO> getSummary(
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date from,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date to,
            @RequestParam(required = false) Long accountId,
            @AuthenticationPrincipal OAuth2User principal) {
        
        if (principal == null) {
            return ResponseEntity.status(401).build();
        }
        
        User user = userService.findOrCreateUser(principal);

        // Set default dates if not provided
        if (to == null) {
            to = new Date();
        }
        if (from == null) {
            LocalDate thirtyDaysAgo = LocalDate.now().minusDays(30);
            from = Date.from(thirtyDaysAgo.atStartOfDay(ZoneId.systemDefault()).toInstant());
        }
        
        SummaryDTO summary = summaryService.getSummary(user.getId(), from, to, accountId);
        return ResponseEntity.ok(summary);
    }
} 