package com.example.demo.service;

import com.example.demo.dto.TransactionDTO;
import com.example.demo.entity.Transaction;
import com.example.demo.entity.User;

import java.util.Date;
import java.util.List;

public interface TransactionService {
    Transaction createTransaction(TransactionDTO transactionDTO, User user);
    List<TransactionDTO> getAllTransactions(Long userId, Date from, Date to, Long accountId);
    TransactionDTO getTransactionById(Long transactionId, Long userId);
    Transaction updateTransaction(Long transactionId, TransactionDTO transactionDTO, User user);
    void deleteTransaction(Long transactionId, Long userId);
    void deleteAllTransactions(Long userId);
    List<Transaction> createTransactionsBulk(List<TransactionDTO> transactionDTOs, User user);
}
