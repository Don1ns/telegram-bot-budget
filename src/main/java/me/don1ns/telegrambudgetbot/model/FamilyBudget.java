package me.don1ns.telegrambudgetbot.model;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "family_budgets")
public class FamilyBudget {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String budgetName;
    private String keyword;
    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "budget_id")
    private Set<Income> incomes = new HashSet<>();
    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "budget_id")
    private Set<Expense> expenses = new HashSet<>();

    public FamilyBudget() {
    }

    public FamilyBudget(Long id, String budgetName, String keyword, Set<Income> incomes, Set<Expense> expenses) {
        this.id = id;
        this.budgetName = budgetName;
        this.keyword = keyword;
        this.incomes = incomes;
        this.expenses = expenses;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBudgetName() {
        return budgetName;
    }

    public void setBudgetName(String budgetName) {
        this.budgetName = budgetName;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public Set<Income> getIncomes() {
        return incomes;
    }

    public void setIncomes(Set<Income> incomes) {
        this.incomes = incomes;
    }

    public Set<Expense> getExpenses() {
        return expenses;
    }

    public void setExpenses(Set<Expense> expenses) {
        this.expenses = expenses;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FamilyBudget budget = (FamilyBudget) o;
        return Objects.equals(id, budget.id) && Objects.equals(budgetName, budget.budgetName) && Objects.equals(keyword, budget.keyword) && Objects.equals(incomes, budget.incomes) && Objects.equals(expenses, budget.expenses);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, budgetName, keyword, incomes, expenses);
    }
}
