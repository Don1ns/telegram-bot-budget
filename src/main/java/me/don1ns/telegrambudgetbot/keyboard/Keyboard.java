package me.don1ns.telegrambudgetbot.keyboard;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import me.don1ns.telegrambudgetbot.constant.ButtonCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class Keyboard {
    private final Logger logger = LoggerFactory.getLogger(Keyboard.class);
    private final TelegramBot telegramBot;
    public Keyboard(TelegramBot telegramBot) {
        this.telegramBot = telegramBot;
    }
    public void chooseMenu(long chatId) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup(
                ButtonCommand.CREATE_BUDGET.getCommand(), ButtonCommand.ADD_TO_BUDGET.getCommand());
        sendResponseMenu(chatId, replyKeyboardMarkup, "Выберите, хотите ли вы создать бюджет или присоединиться в существующий");
    }
    public void mainMenu(long chatId) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup(
                new String[]{ButtonCommand.CHECK_BUDGET.getCommand(), ButtonCommand.ADD_INCOME.getCommand()},
                new String[]{ButtonCommand.ADD_EXPENSE.getCommand(), ButtonCommand.HELP.getCommand()});
        sendResponseMenu(chatId, replyKeyboardMarkup, "Ниже представлено главное меню. ");
    }

    public void checkBudget(long chatId) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup(
                new String[]{ButtonCommand.GET_BALANCE.getCommand(), ButtonCommand.LAST_OPERATIONS.getCommand()},
                new String[]{ButtonCommand.INCOMES.getCommand(), ButtonCommand.EXPENSES.getCommand()},
                new String[]{ButtonCommand.MAIN_MENU.getCommand()});
        sendResponseMenu(chatId, replyKeyboardMarkup, "Ниже представлено меню просмотра бюджета. ");
    }
    public void addExpense(long chatId) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup(
                new String[]{ButtonCommand.TRANSPORT.getCommand(), ButtonCommand.SUPERMARKETS.getCommand(), ButtonCommand.RESTAURANTS.getCommand()},
                new String[]{ButtonCommand.HOUSE_AND_RENOVATION.getCommand(), ButtonCommand.INTERNET.getCommand(), ButtonCommand.BEAUTY.getCommand()},
                new String[]{ButtonCommand.ENTERTAINMENT.getCommand(), ButtonCommand.CLOTHES.getCommand(), ButtonCommand.ELECTRONICS.getCommand()},
                new String[]{ButtonCommand.BANK_SERVICES.getCommand(), ButtonCommand.PHARMACY.getCommand(), ButtonCommand.SPORT.getCommand()},
                new String[]{ButtonCommand.MONEY_TRANSFERS_EXPENSE.getCommand(), ButtonCommand.OTHER_EXPENSE.getCommand()},
                new String[]{ButtonCommand.MAIN_MENU.getCommand()});
        sendResponseMenu(chatId, replyKeyboardMarkup, "Выберите категорию. ");
    }
    public void addIncome(long chatId) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup(
                new String[]{ButtonCommand.SALARY.getCommand(), ButtonCommand.BONUSES.getCommand()},
                new String[]{ButtonCommand.SOCIAL_PAYMENTS.getCommand(), ButtonCommand.SHARES.getCommand()},
                new String[]{ButtonCommand.MONEY_TRANSFERS_INCOME.getCommand(), ButtonCommand.OTHER_INCOME.getCommand()},
                new String[]{ButtonCommand.MAIN_MENU.getCommand()});
        sendResponseMenu(chatId, replyKeyboardMarkup, "Выберите категорию. ");
    }

    public void sendResponseMenu(long chatId, ReplyKeyboardMarkup replyKeyboardMarkup, String text) {
        SendMessage sendMessage = new SendMessage(
                chatId, text).replyMarkup(replyKeyboardMarkup.resizeKeyboard(true));
        SendResponse sendResponse = telegramBot.execute(sendMessage);
        if (!sendResponse.isOk()) {
            logger.error("Error during sending message: {}", sendResponse.description());
        }
    }
}
