package com.example.demo.mapper;

import com.example.demo.dto.AccountDTO;
import com.example.demo.entity.Account;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AccountMapper {
    /*Note
    * AccountMapper doesn't know how to map between:
    * Account.user (a User object) ↔ AccountDTO.userId (long)
    */
    @Mapping(source = "user.id", target = "userId")
    AccountDTO toDTO(Account account);
    List<AccountDTO> toDtoList(List<Account> accounts);
    Account toEntity(AccountDTO accountDTO);
}
