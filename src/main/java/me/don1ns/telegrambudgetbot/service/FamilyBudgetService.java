package me.don1ns.telegrambudgetbot.service;

import me.don1ns.telegrambudgetbot.model.FamilyBudget;
import me.don1ns.telegrambudgetbot.repository.FamilyBudgetRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FamilyBudgetService {
    private final FamilyBudgetRepository familyBudgetRepository;

    public FamilyBudgetService(FamilyBudgetRepository familyBudgetRepository) {
        this.familyBudgetRepository = familyBudgetRepository;
    }
    public FamilyBudget save(FamilyBudget familyBudget) {
        return familyBudgetRepository.save(familyBudget);
    }
    public Optional<FamilyBudget> getById(Long id) {
        return familyBudgetRepository.findById(id);
    }
//    public Optional<FamilyBudget> getByUserChatId(Long chatId) {
//        return familyBudgetRepository.findByUserChatId(chatId);
//    }
    public Optional<FamilyBudget> getByBudgetName(String budgetName) {
        return familyBudgetRepository.findByBudgetName(budgetName);
    }
    public List<FamilyBudget> getAll() {
        return familyBudgetRepository.findAll();
    }
}
