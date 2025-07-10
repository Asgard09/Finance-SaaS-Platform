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
import com.example.demo.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final CategoryRepository categoryRepository;
    private final TransactionMapper transactionMapper;

    @Override
    public Transaction createTransaction(TransactionDTO transactionDTO, User user) {
        Transaction transaction = transactionMapper.toEntity(transactionDTO);
        
        // Set account
        Account account = accountRepository.findById(transactionDTO.getAccountId())
            .orElseThrow(() -> new RuntimeException("Account not found"));
        
        if (!account.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Account does not belong to user");
        }
        transaction.setAccount(account);
        
        // Set category if provided
        if (transactionDTO.getCategoryId() != null) {
            Category category = categoryRepository.findById(transactionDTO.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));
            
            if (!category.getUser().getId().equals(user.getId())) {
                throw new RuntimeException("Category does not belong to user");
            }
            transaction.setCategory(category);
        }
        
        return transactionRepository.save(transaction);
    }

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
        
        if (!transaction.getAccount().getUser().getId().equals(userId)) {
            throw new RuntimeException("Transaction does not belong to user");
        }
        
        return transactionMapper.toDTO(transaction);
    }

    @Override
    @Transactional
    public Transaction updateTransaction(Long transactionId, TransactionDTO transactionDTO, User user) {
        Transaction existingTransaction = transactionRepository.findById(transactionId)
            .orElseThrow(() -> new RuntimeException("Transaction not found"));
        
        if (!existingTransaction.getAccount().getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Transaction does not belong to user");
        }
        
        // Update fields
        existingTransaction.setAmount(transactionDTO.getAmount());
        existingTransaction.setPayee(transactionDTO.getPayee());
        existingTransaction.setNotes(transactionDTO.getNotes());
        existingTransaction.setDate(transactionDTO.getDate());
        
        // Update account if changed
        if (transactionDTO.getAccountId() != null) {
            Account account = accountRepository.findById(transactionDTO.getAccountId())
                .orElseThrow(() -> new RuntimeException("Account not found"));
            
            if (!account.getUser().getId().equals(user.getId())) {
                throw new RuntimeException("Account does not belong to user");
            }
            existingTransaction.setAccount(account);
        }
        
        // Update category if changed
        if (transactionDTO.getCategoryId() != null) {
            Category category = categoryRepository.findById(transactionDTO.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));
            
            if (!category.getUser().getId().equals(user.getId())) {
                throw new RuntimeException("Category does not belong to user");
            }
            existingTransaction.setCategory(category);
        } else {
            existingTransaction.setCategory(null);
        }
        
        return transactionRepository.save(existingTransaction);
    }

    @Override
    @Transactional
    public void deleteTransaction(Long transactionId, Long userId) {
        int deletedCount = transactionRepository.deleteByIdAndUserId(transactionId, userId);
        if (deletedCount == 0) {
            throw new RuntimeException("Transaction not found or you don't have permission to delete this transaction");
        }
    }

    @Override
    @Transactional
    public void deleteAllTransactions(Long userId) {
        transactionRepository.deleteByUserId(userId);
    }

    @Override
    @Transactional
    public List<Transaction> createTransactionsBulk(List<TransactionDTO> transactionDTOs, User user) {
        return transactionDTOs.stream().map(dto -> createTransaction(dto, user)).collect(Collectors.toList());
    }
}
