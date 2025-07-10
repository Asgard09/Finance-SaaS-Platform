package com.example.demo.mapper;

import com.example.demo.dto.TransactionDTO;
import com.example.demo.entity.Transaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TransactionMapper {
    @Mapping(source = "account.id", target = "accountId")
    @Mapping(source = "category.id", target = "categoryId")
    @Mapping(source = "account.name", target = "account")
    @Mapping(source = "category.name", target = "category")
    TransactionDTO toDTO(Transaction transaction);
    
    List<TransactionDTO> toDtoList(List<Transaction> transactions);
    
    @Mapping(target = "account", ignore = true)
    @Mapping(target = "category", ignore = true)
    Transaction toEntity(TransactionDTO transactionDTO);
} 