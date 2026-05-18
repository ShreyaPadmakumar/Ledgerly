package com.ledgerly.repository;

import com.ledgerly.entity.Expense;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {

    // ---- Filtering Queries ----

    Page<Expense> findByCategory(String category, Pageable pageable);

    Page<Expense> findByExpenseDateBetween(LocalDate start, LocalDate end, Pageable pageable);

    Page<Expense> findByCategoryAndExpenseDateBetween(String category, LocalDate start, LocalDate end, Pageable pageable);

    @Query("SELECT e FROM Expense e WHERE " +
           "(:category IS NULL OR e.category = :category) AND " +
           "(:startDate IS NULL OR e.expenseDate >= :startDate) AND " +
           "(:endDate IS NULL OR e.expenseDate <= :endDate) AND " +
           "(:keyword IS NULL OR LOWER(e.note) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
           "OR LOWER(e.category) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<Expense> findFiltered(
            @Param("category") String category,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("keyword") String keyword,
            Pageable pageable
    );

    // ---- Dashboard Queries ----

    @Query("SELECT COALESCE(SUM(e.amount), 0) FROM Expense e")
    BigDecimal getTotalSpending();

    @Query("SELECT COALESCE(SUM(e.amount), 0) FROM Expense e " +
           "WHERE MONTH(e.expenseDate) = :month AND YEAR(e.expenseDate) = :year")
    BigDecimal getMonthlySpending(@Param("month") int month, @Param("year") int year);

    @Query("SELECT e.category, SUM(e.amount) FROM Expense e " +
           "GROUP BY e.category ORDER BY SUM(e.amount) DESC")
    List<Object[]> getCategoryBreakdown();

    @Query("SELECT MONTH(e.expenseDate) as month, SUM(e.amount) as total " +
           "FROM Expense e WHERE YEAR(e.expenseDate) = :year " +
           "GROUP BY MONTH(e.expenseDate) ORDER BY MONTH(e.expenseDate)")
    List<Object[]> getMonthlyTrend(@Param("year") int year);

    @Query("SELECT e.category FROM Expense e " +
           "GROUP BY e.category ORDER BY SUM(e.amount) DESC")
    List<String> getTopCategories(Pageable pageable);

    List<Expense> findTop5ByOrderByExpenseDateDescCreatedAtDesc();

    @Query("SELECT COUNT(e) FROM Expense e")
    long getTotalCount();

    @Query("SELECT COALESCE(AVG(e.amount), 0) FROM Expense e")
    BigDecimal getAverageExpense();
}
