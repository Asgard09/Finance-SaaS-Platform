package com.example.demo.service.impl;

import com.example.demo.dto.TransactionDTO;
import com.example.demo.entity.Transaction;
import com.example.demo.mapper.TransactionMapper;
import com.example.demo.repository.TransactionRepository;
import com.example.demo.service.AuthorizationService;
import com.example.demo.service.TransactionQueryService;
import lombok.RequiredArgsConstructor;

import java.util.Date;
import java.util.List;

@RequiredArgsConstructor
public class TransactionQueryServiceImpl implements TransactionQueryService {
    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;
    private final AuthorizationService authorizationService;

    @Override
    public List<TransactionDTO> getAllTransactions(Long userId, Date from, Date to, Long accountId) {
        List<Transaction> transactions;

        if (from != null && to != null && accountId != null) {
            transactions = transactionRepository.findByUserIdAndAccountIdAndDateBetween(userId, accountId, from, to);
        } else if (from != null && to != null) {
            transactions = transactionRepository.findByUserIdAndDateBetween(userId, from, to);
        } else if (accountId != null) {
            transactions = transactionRepository.findByUserIdAndAccountId(userId, accountId);
        } else {
            transactions = transactionRepository.findByUserId(userId);
        }

        return transactionMapper.toDtoList(transactions);
    }


    @Override
    public TransactionDTO getTransactionById(Long transactionId, Long userId) {
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));
        authorizationService.validateTransactionOwnerShip(transaction, userId);
        return transactionMapper.toDTO(transaction);
    }
}
