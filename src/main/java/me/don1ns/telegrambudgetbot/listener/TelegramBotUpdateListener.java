package me.don1ns.telegrambudgetbot.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import jakarta.annotation.PostConstruct;
import me.don1ns.telegrambudgetbot.constant.ButtonCommand;
import me.don1ns.telegrambudgetbot.model.Category;
import me.don1ns.telegrambudgetbot.model.FamilyBudget;
import me.don1ns.telegrambudgetbot.model.User;
import me.don1ns.telegrambudgetbot.keyboard.Keyboard;
import me.don1ns.telegrambudgetbot.model.UserState;
import me.don1ns.telegrambudgetbot.service.ExpenseService;
import me.don1ns.telegrambudgetbot.service.FamilyBudgetService;
import me.don1ns.telegrambudgetbot.service.IncomeService;
import me.don1ns.telegrambudgetbot.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;


@Component
public class TelegramBotUpdateListener implements UpdatesListener {
    private final Logger logger = LoggerFactory.getLogger(TelegramBotUpdateListener.class);
    private final TelegramBot telegramBot;
    private final Keyboard keyboard;
    private final UserService userService;
    private final FamilyBudgetService familyBudgetService;
    private final IncomeService incomeService;
    private final ExpenseService expenseService;

    public TelegramBotUpdateListener(TelegramBot telegramBot, Keyboard keyboard, UserService userService, FamilyBudgetService familyBudgetService, IncomeService incomeService, ExpenseService expenseService) {
        this.telegramBot = telegramBot;
        this.keyboard = keyboard;
        this.userService = userService;
        this.familyBudgetService = familyBudgetService;
        this.incomeService = incomeService;
        this.expenseService = expenseService;
    }

    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }

    public static ButtonCommand parse(String buttonCommand) {
        ButtonCommand[] values = ButtonCommand.values();
        for (ButtonCommand command : values) {
            if (command.getCommand().equals(buttonCommand)) {
                return command;
            }
        }
        return ButtonCommand.NOTHING;
    }

    @Override
    public int process(List<Update> updates) {
        try {
            updates.forEach(update -> {
                logger.info("Handles update: {}", update);
                Message message = update.message();
                long chatId = message.chat().id();
                String firsName = message.chat().firstName();
                String text = message.text();
                int messageId = message.messageId();
                if (text != null && (userService.getByChatId(chatId).isEmpty()) || userService.getByChatId(chatId).get().getUserState().equals(UserState.IDLE)) {
                    switch (parse(text)) {
                        case START -> {
                            if(userService.getByChatId(chatId).isEmpty()) {
                                sendResponseMessage(chatId, "Привет! Я помогу тебе планировать семейный бюджет, создай или добавься в существующий бюджет");
                                User user = new User();
                                user.setChatId(chatId);
                                user.setName(firsName);
                                user.setUserState(UserState.IDLE);
                                userService.save(user);
                                keyboard.chooseMenu(chatId);
                            }
                            else {
                                keyboard.mainMenu(chatId);
                            }
                        }
                        case CREATE_BUDGET -> {
                            if(familyBudgetService.getByUserChatId(chatId).isEmpty() && (userService.getByChatId(chatId).isPresent())) {
                                User user = userService.getByChatId(chatId).get();
                                user.setUserState(UserState.CREATING_BUDGET_1);
                                userService.save(user);
                                sendResponseMessage(chatId, "Введите название бюджета:");
                            }
                        }
                        case ADD_TO_BUDGET -> {
                            if(familyBudgetService.getByUserChatId(chatId).isEmpty() && (userService.getByChatId(chatId).isPresent())) {
                                User user = userService.getByChatId(chatId).get();
                                user.setUserState(UserState.ADD_TO_BUDGET_1);
                                userService.save(user);
                                sendResponseMessage(chatId, "Введите название бюджета:");
                            }
                        }
                        case MAIN_MENU -> {
                            keyboard.mainMenu(chatId);
                        }
                        case CHECK_BUDGET -> {
//                            Optional<FamilyBudget> optionalBudget = familyBudgetService.getByUserChatId(chatId);
//                            if(optionalBudget.isPresent()) {
//                                FamilyBudget budget = optionalBudget.get();
//                            }
                            keyboard.checkBudget(chatId);
                        }
                        case GET_BALANCE-> {

                        }
                        case LAST_OPERATIONS-> {

                        }
                        case ADD_INCOME-> {
                            keyboard.addIncome(chatId);
                        }
                        case ADD_EXPENSE-> {
                            keyboard.addExpense(chatId);
                        }
                        case TRANSPORT -> {
                            addExpense(chatId, Category.TRANSPORT);
                        }
                        case SUPERMARKETS -> {
                            addExpense(chatId, Category.SUPERMARKETS);
                        }
                        case RESTAURANTS -> {
                            addExpense(chatId, Category.RESTAURANTS);
                        }
                        case HOUSE_AND_RENOVATION -> {
                            addExpense(chatId, Category.HOUSE_AND_RENOVATION);
                        }
                        case INTERNET -> {
                            addExpense(chatId, Category.INTERNET);
                        }
                        case BEAUTY -> {
                            addExpense(chatId, Category.BEAUTY);
                        }
                        case ENTERTAINMENT -> {
                            addExpense(chatId, Category.ENTERTAINMENT);
                        }
                        case CLOTHES -> {
                            addExpense(chatId, Category.CLOTHES);
                        }
                        case ELECTRONICS -> {
                            addExpense(chatId, Category.ELECTRONICS);
                        }
                        case BANK_SERVICES -> {
                            addExpense(chatId, Category.BANK_SERVICES);
                        }
                        case PHARMACY -> {
                            addExpense(chatId, Category.PHARMACY);
                        }
                        case SPORT -> {
                            addExpense(chatId, Category.SPORT);
                        }
                        case MONEY_TRANSFERS_EXPENSE -> {
                            addExpense(chatId, Category.MONEY_TRANSFERS);
                        }
                        case OTHER_EXPENSE -> {
                            addExpense(chatId, Category.OTHER);
                        }
                        default -> {
                            if (!userService.getByChatId(chatId).get().getUserState().equals(UserState.IDLE)){
                                User user = userService.getByChatId(chatId).get();
                                switch(user.getUserState()){
                                    case CREATING_BUDGET_1 -> {
                                        FamilyBudget budget = new FamilyBudget();
                                        budget.setBudgetName(text);
                                        user.setUserState(UserState.CREATING_BUDGET_2);
                                        userService.save(user);
                                        Set<User> users = new HashSet<>();
                                        users.add(user);
                                        budget.setUsers(users);
                                        familyBudgetService.save(budget);
                                        sendResponseMessage(chatId, "Введите ключевое слово:");
                                    }
                                    case CREATING_BUDGET_2 -> {
                                        FamilyBudget budget = familyBudgetService.getByUserChatId(chatId).get();
                                        budget.setKeyword(text);
                                        familyBudgetService.save(budget);
                                        user.setUserState(UserState.IDLE);
                                        userService.save(user);
                                        keyboard.mainMenu(chatId);
                                    }
                                    case ADD_TO_BUDGET_1 -> {
                                        user.setIntroducedBudgetName(text);
                                        user.setUserState(UserState.ADD_TO_BUDGET_2);
                                        userService.save(user);
                                        sendResponseMessage(chatId, "Введите ключевое слово:");
                                    }
                                    case ADD_TO_BUDGET_2 -> {
                                        Optional<FamilyBudget> optionalBudget = familyBudgetService.getByBudgetName(user.getIntroducedBudgetName());
                                        if(optionalBudget.isPresent() && optionalBudget.get().getKeyword().equals(text)) {
                                            FamilyBudget budget = optionalBudget.get();
                                            user.setUserState(UserState.IDLE);
                                            Set<User> users = budget.getUsers();
                                            users.add(user);
                                            budget.setUsers(users);
                                            familyBudgetService.save(budget);
                                            keyboard.mainMenu(chatId);
                                        }
                                        else{

                                            sendResponseMessage(chatId, "Некорректно введены название бюджета или ключевое слово, повторите попытку!");
                                        }
                                    }
                                }
                            }
                            else {
                                sendResponseMessage(chatId, "Я тебя не понимаю :(");
                            }
                        }
                    }
                }
            });
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    private void sendResponseMessage(long chatId, String text) {
        SendMessage sendMessage = new SendMessage(chatId, text);
        SendResponse sendResponse = telegramBot.execute(sendMessage);
        if (!sendResponse.isOk()) {
            logger.error("Error during sending message: {}", sendResponse.description());
        }
    }
    private void addExpense(Long chatId, Category category) {
        User user = userService.getByChatId(chatId).get();
        user.setUserState(UserState.ADD_EXPENSE);
        user.setIntroducedCategory(category);
        userService.save(user);
        sendResponseMessage(chatId, "Введите сумму:");
    }
}