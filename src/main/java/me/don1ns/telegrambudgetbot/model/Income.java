package me.don1ns.telegrambudgetbot.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "incomes")
public class Income {
    @Id
    @Column(name = "income_id")
    private Long id;
    @ManyToOne
    @JoinColumn(name = "user_chat_id")
    private User user;
    private int amount;
    @Enumerated
    private Category category;
    private LocalDateTime date = LocalDateTime.now();

    public Income() {
    }

    public Income(Long id, User user, int amount, Category category, LocalDateTime date) {
        this.id = id;
        this.user = user;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Income{" +
                "id=" + id +
                ", user=" + user +
                ", amount=" + amount +
                ", category='" + category + '\'' +
                ", date=" + date +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Income income = (Income) o;
        return amount == income.amount && Objects.equals(id, income.id) && Objects.equals(user, income.user) && Objects.equals(category, income.category) && Objects.equals(date, income.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user, amount, category, date);
    }
}
