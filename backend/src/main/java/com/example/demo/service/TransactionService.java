package com.example.demo.service;

import com.example.demo.dto.TransactionDTO;
import com.example.demo.entity.Transaction;
import com.example.demo.entity.User;

import java.util.Date;
import java.util.List;

public interface TransactionService {
    void deleteAllTransactions(Long userId);
    List<Transaction> createTransactionsBulk(List<TransactionDTO> transactionDTOs, User user);
}
