package me.don1ns.telegrambudgetbot.service;

import me.don1ns.telegrambudgetbot.model.FamilyBudget;
import me.don1ns.telegrambudgetbot.model.Income;
import me.don1ns.telegrambudgetbot.repository.IncomeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class IncomeService {
    private final IncomeRepository incomeRepository;

    public IncomeService(IncomeRepository incomeRepository) {
        this.incomeRepository = incomeRepository;
    }
    public Income save(Income income) {
        return incomeRepository.save(income);
    }
    public Optional<Income> getById(Long id) {
        return incomeRepository.findById(id);
    }
    public List<Income> getAll() {
        return incomeRepository.findAll();
    }
}
