package com.example.demo.service;

import com.example.demo.dto.TransactionDTO;
import com.example.demo.entity.Transaction;
import com.example.demo.entity.User;
import org.springframework.stereotype.Service;

@Service
public interface TransactionCommandService {
    Transaction createTransaction(TransactionDTO transactionDTO, User user);
    Transaction updateTransaction(Long transactionId, TransactionDTO transactionDTO, User user);
    void deleteTransaction(Long transactionId, Long userId);
}
