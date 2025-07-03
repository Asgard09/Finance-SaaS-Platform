package com.example.demo.service;

import com.example.demo.dto.AccountDTO;
import com.example.demo.entity.Account;

public interface AccountService {
    Account createAccount(AccountDTO accountDTO);
}
