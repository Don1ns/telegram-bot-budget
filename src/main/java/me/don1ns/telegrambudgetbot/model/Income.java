package me.don1ns.telegrambudgetbot.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@Entity
@Table(name = "incomes")
public class Income {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String userName;
    private int amount;
    @Enumerated
    private Category category;
    private LocalDate date = LocalDate.now();

    public Income() {
    }

    public Income(Long id, String userName, int amount, Category category, LocalDate date) {
        this.id = id;
        this.userName = userName;
        this.amount = amount;
        this.category = category;
        this.date = date;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yy");
        return date.format(formatter);
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Income income = (Income) o;
        return amount == income.amount && Objects.equals(id, income.id) && Objects.equals(userName, income.userName) && category == income.category && Objects.equals(date, income.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userName, amount, category, date);
    }
}
