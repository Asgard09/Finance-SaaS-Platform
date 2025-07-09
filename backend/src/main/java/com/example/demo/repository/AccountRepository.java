package com.example.demo.repository;

import com.example.demo.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface AccountRepository extends JpaRepository<Account, Long> {
    List<Account> findByUserId(Long userId);

    @Modifying
    @Query("DELETE FROM accounts a WHERE a.id = :accountId AND a.user.id = :userId")
    int deleteByIdAndUserId(@Param("accountId") Long accountId, @Param("userId") Long userId);

    @Modifying
    @Query("DELETE FROM accounts a WHERE a.user.id = :userId")
    int deleteByUserId(@Param("userId") Long userId);
}
