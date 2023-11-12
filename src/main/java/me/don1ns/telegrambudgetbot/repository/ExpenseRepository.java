package me.don1ns.telegrambudgetbot.repository;

import me.don1ns.telegrambudgetbot.model.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {
}
