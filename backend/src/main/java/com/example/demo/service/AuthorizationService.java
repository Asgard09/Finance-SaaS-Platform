package com.example.demo.service;

import com.example.demo.entity.Account;
import com.example.demo.entity.Category;
import com.example.demo.entity.Transaction;
import org.springframework.stereotype.Service;

@Service
public interface AuthorizationService {
    void validateAccountOwnerShip(Account account, Long userId);
    void validateTransactionOwnerShip(Transaction transaction, Long userId);
    void validateCategoryOwnerShip(Category category, Long userId);
}
