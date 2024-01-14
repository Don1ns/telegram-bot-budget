package me.don1ns.telegrambudgetbot.constant;

public enum ButtonCommand {
    START("/start"),
    CREATE_BUDGET("Создать бюджет"),
    ADD_TO_BUDGET("Добавиться в существующий бюджет"),
    CHECK_BUDGET("Посмотреть бюджет"),
    ADD_INCOME("Добавить доход"),
    ADD_EXPENSE("Добавить расход"),
    MAIN_MENU("Главное меню"),
    GET_BALANCE("Баланс"),
    INCOMES("Все доходы"),
    EXPENSES("Все расходы"),
    LAST_OPERATIONS("Последние операции"),
    HELP("Советы"),
    TRANSPORT("Транспорт"),
    SUPERMARKETS("Супермаркеты"),
    RESTAURANTS("Рестораны"),
    HOUSE_AND_RENOVATION("Дом и ремонт"),
    INTERNET("Связь/Интернет"),
    BEAUTY("Красота"),
    ENTERTAINMENT("Развлечения"),
    CLOTHES("Одежда"),
    ELECTRONICS("Электроника"),
    BANK_SERVICES("Услуги банка/кредит"),
    PHARMACY("Аптека"),
    SPORT("Спорт"),
    MONEY_TRANSFERS_INCOME("Перевод"),
    OTHER_INCOME("Другой доход"),
    MONEY_TRANSFERS_EXPENSE("Перевод"),
    OTHER_EXPENSE("Другой расход"),
    SHARES("Ценные бумаги"),
    SOCIAL_PAYMENTS("Социальные выплаты"),
    BONUSES("Бонусы"),
    SALARY("Зарплата"),
    NOTHING("");
    private final String command;

    ButtonCommand(String command){
        this.command = command;
    }

    public String getCommand(){
        return command;
    }
}
