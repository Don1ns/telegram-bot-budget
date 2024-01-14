package me.don1ns.telegrambudgetbot.repository;

import me.don1ns.telegrambudgetbot.model.FamilyBudget;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FamilyBudgetRepository extends JpaRepository<FamilyBudget, Long> {
//    @Query("SELECT b FROM FamilyBudget b JOIN b.users u WHERE u.chatId = :chatId")
//    Optional<FamilyBudget> findByUserChatId(@Param("chatId") Long chatId);

    Optional<FamilyBudget> findByBudgetName(String budgetName);
}
