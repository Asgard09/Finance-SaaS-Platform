package com.example.demo.service;

import com.example.demo.dto.TransactionDTO;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public interface TransactionQueryService {
    List<TransactionDTO> getAllTransactions(Long userId, Date from, Date to, Long accountId);
    TransactionDTO getTransactionById(Long transactionId, Long userId);
}
