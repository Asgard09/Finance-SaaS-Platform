package com.example.demo.service.impl;

import com.example.demo.entity.Account;
import com.example.demo.entity.Category;
import com.example.demo.entity.Transaction;
import com.example.demo.service.AuthorizationService;

public class AuthorizationServiceImpl implements AuthorizationService {
    @Override
    public void validateAccountOwnerShip(Account account, Long userId) {
        if (!account.getUser().getId().equals(userId)) {
            throw new RuntimeException("Account does not belong to user");
        }
    }

    @Override
    public void validateTransactionOwnerShip(Transaction transaction, Long userId) {
        if (!transaction.getAccount().getUser().getId().equals(userId)) {
            throw new RuntimeException("Transaction does not belong to user");
        }
    }

    @Override
    public void validateCategoryOwnerShip(Category category, Long userId) {
        if (!category.getUser().getId().equals(userId)) {
            throw new RuntimeException("Category does not belong to user");
        }
    }
}
