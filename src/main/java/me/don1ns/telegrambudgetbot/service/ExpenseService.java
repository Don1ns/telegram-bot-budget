package me.don1ns.telegrambudgetbot.service;

import me.don1ns.telegrambudgetbot.model.Expense;
import me.don1ns.telegrambudgetbot.model.FamilyBudget;
import me.don1ns.telegrambudgetbot.model.Income;
import me.don1ns.telegrambudgetbot.repository.ExpenseRepository;
import me.don1ns.telegrambudgetbot.repository.FamilyBudgetRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ExpenseService {
    private final ExpenseRepository expenseRepository;

    public ExpenseService(ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
    }
    public Expense save(Expense expense) {
        return expenseRepository.save(expense);
    }
    public Optional<Expense> getById(Long id) {
        return expenseRepository.findById(id);
    }
    public List<Expense> getAll() {
        return expenseRepository.findAll();
    }
}
