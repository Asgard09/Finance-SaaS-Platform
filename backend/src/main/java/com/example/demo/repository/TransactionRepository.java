package com.example.demo.repository;

import com.example.demo.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    
    @Query("SELECT t FROM transactions t WHERE t.account.user.id = :userId")
    List<Transaction> findByUserId(@Param("userId") Long userId);
    
    @Query("SELECT t FROM transactions t WHERE t.account.user.id = :userId AND t.date BETWEEN :from AND :to")
    List<Transaction> findByUserIdAndDateBetween(@Param("userId") Long userId, @Param("from") Date from, @Param("to") Date to);
    
    @Query("SELECT t FROM transactions t WHERE t.account.user.id = :userId AND t.account.id = :accountId")
    List<Transaction> findByUserIdAndAccountId(@Param("userId") Long userId, @Param("accountId") Long accountId);
    
    @Query("SELECT t FROM transactions t WHERE t.account.user.id = :userId AND t.account.id = :accountId AND t.date BETWEEN :from AND :to")
    List<Transaction> findByUserIdAndAccountIdAndDateBetween(
        @Param("userId") Long userId, 
        @Param("accountId") Long accountId, 
        @Param("from") Date from, 
        @Param("to") Date to
    );

    @Modifying
    @Query("DELETE FROM transactions t WHERE t.id = :transactionId AND t.account.user.id = :userId")
    int deleteByIdAndUserId(@Param("transactionId") Long transactionId, @Param("userId") Long userId);

    @Modifying
    @Query("DELETE FROM transactions t WHERE t.account.user.id = :userId")
    int deleteByUserId(@Param("userId") Long userId);
}
