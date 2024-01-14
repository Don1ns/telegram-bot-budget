package me.don1ns.telegrambudgetbot.model;

public enum Category {
    TRANSPORT("Транспорт"),
    HOUSE_AND_RENOVATION("Дом и ремонт"),
    BEAUTY("Красота"),
    CLOTHES("Одежда"),
    ELECTRONICS("Электроника"),
    SUPERMARKETS("Супермаркеты"),
    RESTAURANTS("Рестораны"),
    BANK_SERVICES("Услуги банка/кредит"),
    PHARMACY("Аптека"),
    SPORT("Спорт"),
    INTERNET("Связь/Интернет"),
    ENTERTAINMENT("Развлечения"),
    MONEY_TRANSFERS("Переводы"),
    OTHER("Другое"),
    SHARES("Ценные бумаги"),
    SOCIAL_PAYMENTS("Социальные выплаты"),
    BONUSES("Бонусы"),
    SALARY("Зарплата");
    private final String text;

    Category(String text){
        this.text = text;
    }

    public String getText(){
        return text;
    }
}
