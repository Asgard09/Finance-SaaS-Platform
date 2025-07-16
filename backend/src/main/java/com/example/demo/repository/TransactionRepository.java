package com.example.demo.repository;

import com.example.demo.entity.Transaction;
import com.example.demo.projection.CategorySummaryProjection;
import com.example.demo.projection.DailySummaryProjection;
import com.example.demo.projection.FinancialSummaryProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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

    /*Note*/
    /*Find income, expense and remaining*/
    @Query(value = """
        SELECT 
            SUM(CASE WHEN t.amount >= 0 THEN t.amount ELSE 0 END) as income,
            SUM(CASE WHEN t.amount < 0 THEN t.amount ELSE 0 END) as expense,
            SUM(t.amount) as remaining
        FROM transactions t 
        INNER JOIN accounts a ON t.account_id = a.id 
        WHERE a.user_id = :userId 
        AND t.date BETWEEN :fromDate AND :toDate
        AND (:accountId IS NULL OR t.account_id = :accountId)
        """, nativeQuery = true)
    FinancialSummaryProjection getFinancialSummary(
        @Param("userId") Long userId,
        @Param("fromDate") Date fromDate, 
        @Param("toDate") Date toDate,
        @Param("accountId") Long accountId
    );

    /*Get total expenses for Category*/
    @Query(value = """
        SELECT 
            c.name as name,
            SUM(ABS(t.amount)) as value
        FROM transactions t 
        INNER JOIN accounts a ON t.account_id = a.id 
        INNER JOIN categories c ON t.category_id = c.id
        WHERE a.user_id = :userId 
        AND t.amount < 0
        AND t.date BETWEEN :fromDate AND :toDate
        AND (:accountId IS NULL OR t.account_id = :accountId)
        GROUP BY c.name
        ORDER BY SUM(ABS(t.amount)) DESC
        """, nativeQuery = true)
    List<CategorySummaryProjection> getEachCategoryExpenses(
        @Param("userId") Long userId,
        @Param("fromDate") Date fromDate, 
        @Param("toDate") Date toDate,
        @Param("accountId") Long accountId
    );
    
    @Query(value = """
        SELECT 
            t.date as date,
            SUM(CASE WHEN t.amount >= 0 THEN t.amount ELSE 0 END) as income,
            SUM(CASE WHEN t.amount < 0 THEN ABS(t.amount) ELSE 0 END) as expense
        FROM transactions t 
        INNER JOIN accounts a ON t.account_id = a.id 
        WHERE a.user_id = :userId 
        AND t.date BETWEEN :fromDate AND :toDate
        AND (:accountId IS NULL OR t.account_id = :accountId)
        GROUP BY t.date
        ORDER BY t.date
        """, nativeQuery = true)
    List<DailySummaryProjection> getDailyIncomeAndExpense(
        @Param("userId") Long userId,
        @Param("fromDate") Date fromDate, 
        @Param("toDate") Date toDate,
        @Param("accountId") Long accountId
    );
}
