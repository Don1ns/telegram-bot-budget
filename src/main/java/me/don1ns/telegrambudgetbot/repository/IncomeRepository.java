package me.don1ns.telegrambudgetbot.repository;

import me.don1ns.telegrambudgetbot.model.Income;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IncomeRepository extends JpaRepository<Income, Long> {
}
