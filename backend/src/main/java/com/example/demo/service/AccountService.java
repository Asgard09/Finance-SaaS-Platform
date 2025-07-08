package com.example.demo.service;

import com.example.demo.dto.AccountDTO;
import com.example.demo.entity.Account;
import com.example.demo.entity.User;

import java.util.List;

public interface AccountService {
    Account createAccount(AccountDTO accountDTO, User user);
    List<AccountDTO> getAllAccount(Long userId);
}
