package com.ledgerly.service;

import com.ledgerly.dto.ExpenseDTO;
import com.ledgerly.entity.Category;
import com.ledgerly.entity.Expense;
import com.ledgerly.repository.ExpenseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ExpenseService {

    private final ExpenseRepository expenseRepository;

    /**
     * Get paginated and filtered expenses.
     */
    public Page<Expense> getFilteredExpenses(String category, Integer month, Integer year,
                                              String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "expenseDate", "createdAt"));

        java.time.LocalDate startDate = null;
        java.time.LocalDate endDate = null;

        if (month != null && year != null) {
            startDate = java.time.LocalDate.of(year, month, 1);
            endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());
        }

        // Normalize empty strings to null
        String cat = (category != null && !category.isBlank()) ? category : null;
        String kw = (keyword != null && !keyword.isBlank()) ? keyword : null;

        return expenseRepository.findFiltered(cat, startDate, endDate, kw, pageable);
    }

    /**
     * Get all expenses with pagination.
     */
    public Page<Expense> getAllExpenses(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "expenseDate", "createdAt"));
        return expenseRepository.findAll(pageable);
    }

    /**
     * Find expense by ID.
     */
    public Optional<Expense> getExpenseById(Long id) {
        return expenseRepository.findById(id);
    }

    /**
     * Save a new expense from DTO.
     */
    @Transactional
    public Expense saveExpense(ExpenseDTO dto) {
        Expense expense = Expense.builder()
                .amount(dto.getAmount())
                .category(dto.getCategory())
                .note(dto.getNote())
                .expenseDate(dto.getExpenseDate())
                .build();
        return expenseRepository.save(expense);
    }

    /**
     * Update an existing expense from DTO.
     */
    @Transactional
    public Expense updateExpense(Long id, ExpenseDTO dto) {
        Expense expense = expenseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Expense not found with id: " + id));

        expense.setAmount(dto.getAmount());
        expense.setCategory(dto.getCategory());
        expense.setNote(dto.getNote());
        expense.setExpenseDate(dto.getExpenseDate());

        return expenseRepository.save(expense);
    }

    /**
     * Delete an expense by ID.
     */
    @Transactional
    public void deleteExpense(Long id) {
        if (!expenseRepository.existsById(id)) {
            throw new RuntimeException("Expense not found with id: " + id);
        }
        expenseRepository.deleteById(id);
    }

    /**
     * Get all available categories.
     */
    public List<Category> getAllCategories() {
        return Arrays.asList(Category.values());
    }

    /**
     * Convert Expense entity to DTO.
     */
    public ExpenseDTO toDTO(Expense expense) {
        return ExpenseDTO.builder()
                .id(expense.getId())
                .amount(expense.getAmount())
                .category(expense.getCategory())
                .note(expense.getNote())
                .expenseDate(expense.getExpenseDate())
                .build();
    }
}
