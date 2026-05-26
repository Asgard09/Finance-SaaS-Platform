package com.example.demo.service.impl;

import com.example.demo.dto.TransactionDTO;
import com.example.demo.entity.Account;
import com.example.demo.entity.Category;
import com.example.demo.entity.Transaction;
import com.example.demo.entity.User;
import com.example.demo.mapper.TransactionMapper;
import com.example.demo.repository.AccountRepository;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.repository.TransactionRepository;
import com.example.demo.service.TransactionCommandService;
import com.example.demo.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepository transactionRepository;
    private final TransactionCommandService transactionCommandService;

    @Override
    @Transactional
    public void deleteAllTransactions(Long userId) {
        transactionRepository.deleteByUserId(userId);
    }

    @Override
    @Transactional
    public List<Transaction> createTransactionsBulk(List<TransactionDTO> transactionDTOs, User user) {
        return transactionDTOs.stream().map(dto -> transactionCommandService.createTransaction(dto, user)).collect(Collectors.toList());
    }
}
