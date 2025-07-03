package com.example.demo.mapper;

import com.example.demo.dto.AccountDTO;
import com.example.demo.entity.Account;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AccountMapper {
    AccountDTO toDTO(Account account);
    Account toEntity(AccountDTO accountDTO);
}
