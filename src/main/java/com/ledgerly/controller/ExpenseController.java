package com.ledgerly.controller;

import com.ledgerly.dto.ExpenseDTO;
import com.ledgerly.entity.Category;
import com.ledgerly.entity.Expense;
import com.ledgerly.service.ExpenseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/expenses")
@RequiredArgsConstructor
public class ExpenseController {

    private final ExpenseService expenseService;

    /**
     * Display all expenses with filtering and pagination.
     */
    @GetMapping
    public String listExpenses(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Integer month,
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model) {

        // Default year to current if month is provided but year is not
        if (month != null && year == null) {
            year = LocalDate.now().getYear();
        }

        Page<Expense> expensePage = expenseService.getFilteredExpenses(
                category, month, year, keyword, page, size);

        model.addAttribute("expenses", expensePage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", expensePage.getTotalPages());
        model.addAttribute("totalItems", expensePage.getTotalElements());
        model.addAttribute("categories", Arrays.asList(Category.values()));

        // Build lookup maps for template (avoids T() static class access)
        Map<String, String> categoryDisplayNames = new HashMap<>();
        Map<String, String> categoryIcons = new HashMap<>();
        Map<String, String> categoryColors = new HashMap<>();
        for (Category cat : Category.values()) {
            categoryDisplayNames.put(cat.name(), cat.getDisplayName());
            categoryIcons.put(cat.name(), cat.getIcon());
            categoryColors.put(cat.name(), cat.getColor());
        }
        model.addAttribute("categoryDisplayNames", categoryDisplayNames);
        model.addAttribute("categoryIcons", categoryIcons);
        model.addAttribute("categoryColors", categoryColors);

        model.addAttribute("pageTitle", "Expenses");
        model.addAttribute("activePage", "expenses");

        // Preserve filter values
        model.addAttribute("selectedCategory", category);
        model.addAttribute("selectedMonth", month);
        model.addAttribute("selectedYear", year);
        model.addAttribute("searchKeyword", keyword);

        // New expense DTO for the add modal
        if (!model.containsAttribute("expenseDTO")) {
            model.addAttribute("expenseDTO", new ExpenseDTO());
        }

        return "expenses/expenses";
    }

    /**
     * Save a new expense.
     */
    @PostMapping("/save")
    public String saveExpense(@Valid @ModelAttribute("expenseDTO") ExpenseDTO dto,
                              BindingResult result,
                              RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.expenseDTO", result);
            redirectAttributes.addFlashAttribute("expenseDTO", dto);
            redirectAttributes.addFlashAttribute("showAddModal", true);
            redirectAttributes.addFlashAttribute("toastType", "error");
            redirectAttributes.addFlashAttribute("toastMessage", "Please fix the validation errors.");
            return "redirect:/expenses";
        }

        expenseService.saveExpense(dto);
        redirectAttributes.addFlashAttribute("toastType", "success");
        redirectAttributes.addFlashAttribute("toastMessage", "Expense added successfully!");
        return "redirect:/expenses";
    }

    /**
     * Get expense data for editing (returns to expense list with edit modal open).
     */
    @GetMapping("/edit/{id}")
    public String editExpense(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        return expenseService.getExpenseById(id)
                .map(expense -> {
                    ExpenseDTO dto = expenseService.toDTO(expense);
                    redirectAttributes.addFlashAttribute("editExpenseDTO", dto);
                    redirectAttributes.addFlashAttribute("showEditModal", true);
                    return "redirect:/expenses";
                })
                .orElseGet(() -> {
                    redirectAttributes.addFlashAttribute("toastType", "error");
                    redirectAttributes.addFlashAttribute("toastMessage", "Expense not found.");
                    return "redirect:/expenses";
                });
    }

    /**
     * Update an existing expense.
     */
    @PostMapping("/update/{id}")
    public String updateExpense(@PathVariable Long id,
                                @Valid @ModelAttribute("editExpenseDTO") ExpenseDTO dto,
                                BindingResult result,
                                RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.editExpenseDTO", result);
            redirectAttributes.addFlashAttribute("editExpenseDTO", dto);
            redirectAttributes.addFlashAttribute("showEditModal", true);
            redirectAttributes.addFlashAttribute("toastType", "error");
            redirectAttributes.addFlashAttribute("toastMessage", "Please fix the validation errors.");
            return "redirect:/expenses";
        }

        try {
            expenseService.updateExpense(id, dto);
            redirectAttributes.addFlashAttribute("toastType", "success");
            redirectAttributes.addFlashAttribute("toastMessage", "Expense updated successfully!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("toastType", "error");
            redirectAttributes.addFlashAttribute("toastMessage", e.getMessage());
        }
        return "redirect:/expenses";
    }

    /**
     * Delete an expense.
     */
    @PostMapping("/delete/{id}")
    public String deleteExpense(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            expenseService.deleteExpense(id);
            redirectAttributes.addFlashAttribute("toastType", "success");
            redirectAttributes.addFlashAttribute("toastMessage", "Expense deleted successfully!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("toastType", "error");
            redirectAttributes.addFlashAttribute("toastMessage", e.getMessage());
        }
        return "redirect:/expenses";
    }
}
