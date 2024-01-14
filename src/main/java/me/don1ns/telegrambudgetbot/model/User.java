package me.don1ns.telegrambudgetbot.model;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "users")
public class User {
    @Id
    @Column(name = "user_chat_id")
    private Long chatId;
    private String name;
    private String introducedBudgetName;
    private Category introducedCategory;
    @Enumerated
    private UserState userState;
    @ManyToOne
    @JoinColumn(name = "family_budget_id")
    private FamilyBudget familyBudget;

    public User() {
    }

    public User(Long chatId, String name, String introducedBudgetName, Category introducedCategory, UserState userState, FamilyBudget familyBudget) {
        this.chatId = chatId;
        this.name = name;
        this.introducedBudgetName = introducedBudgetName;
        this.introducedCategory = introducedCategory;
        this.userState = userState;
        this.familyBudget = familyBudget;
    }

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIntroducedBudgetName() {
        return introducedBudgetName;
    }

    public void setIntroducedBudgetName(String introducedBudgetName) {
        this.introducedBudgetName = introducedBudgetName;
    }

    public Category getIntroducedCategory() {
        return introducedCategory;
    }

    public void setIntroducedCategory(Category introducedCategory) {
        this.introducedCategory = introducedCategory;
    }

    public UserState getUserState() {
        return userState;
    }

    public void setUserState(UserState userState) {
        this.userState = userState;
    }

    public FamilyBudget getFamilyBudget() {
        return familyBudget;
    }

    public void setFamilyBudget(FamilyBudget familyBudget) {
        this.familyBudget = familyBudget;
    }

    @Override
    public String toString() {
        return "User{" +
                "chatId=" + chatId +
                ", name='" + name + '\'' +
                ", introducedBudgetName='" + introducedBudgetName + '\'' +
                ", introducedCategory=" + introducedCategory +
                ", userState=" + userState +
                ", familyBudget=" + familyBudget +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(chatId, user.chatId) && Objects.equals(name, user.name) && Objects.equals(introducedBudgetName, user.introducedBudgetName) && introducedCategory == user.introducedCategory && userState == user.userState && Objects.equals(familyBudget, user.familyBudget);
    }

    @Override
    public int hashCode() {
        return Objects.hash(chatId, name, introducedBudgetName, introducedCategory, userState, familyBudget);
    }
}
