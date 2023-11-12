package me.don1ns.telegrambudgetbot.model;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "family_budgets")
public class FamilyBudget {
    @Id
    private Long id;
    private String budgetName;
    private String keyword;
    @OneToMany
    @JoinColumn(name = "user_chat_id")
    private Set<User> users = new HashSet<>();
    @OneToMany
    @JoinColumn(name = "income_id")
    private Set<Income> incomes = new HashSet<>();
    @OneToMany
    @JoinColumn(name = "expense_id")
    private Set<Expense> expenses = new HashSet<>();

    public FamilyBudget() {
    }

    public FamilyBudget(Long id, String budgetName, String keyword, Set<User> users, Set<Income> incomes, Set<Expense> expenses) {
        this.id = id;
        this.budgetName = budgetName;
        this.keyword = keyword;
        this.users = users;
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

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
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
    public String toString() {
        return "FamilyBudget{" +
                "id=" + id +
                ", budgetName='" + budgetName + '\'' +
                ", keyword='" + keyword + '\'' +
                ", users=" + users +
                ", incomes=" + incomes +
                ", expenses=" + expenses +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FamilyBudget that = (FamilyBudget) o;
        return Objects.equals(id, that.id) && Objects.equals(budgetName, that.budgetName) && Objects.equals(keyword, that.keyword) && Objects.equals(users, that.users) && Objects.equals(incomes, that.incomes) && Objects.equals(expenses, that.expenses);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, budgetName, keyword, users, incomes, expenses);
    }
}
