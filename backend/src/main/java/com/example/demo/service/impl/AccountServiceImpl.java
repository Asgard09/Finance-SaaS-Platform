package com.example.demo.service.impl;

import com.example.demo.dto.AccountDTO;
import com.example.demo.entity.Account;
import com.example.demo.entity.User;
import com.example.demo.mapper.AccountMapper;
import com.example.demo.repository.AccountRepository;
import com.example.demo.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;

    @Override
    public Account createAccount(AccountDTO accountDTO, User user) {
        Account account = accountMapper.toEntity(accountDTO);
        account.setUser(user);
        return accountRepository.save(account);
    }

    @Override
    public List<AccountDTO> getAllAccount(Long userId) {
        List<Account> accounts = accountRepository.findByUserId(userId);
        return accountMapper.toDtoList(accounts);
    }
}
