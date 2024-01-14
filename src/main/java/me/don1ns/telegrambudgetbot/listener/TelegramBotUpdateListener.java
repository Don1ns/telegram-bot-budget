package me.don1ns.telegrambudgetbot.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendDocument;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import jakarta.annotation.PostConstruct;
import me.don1ns.telegrambudgetbot.constant.ButtonCommand;
import me.don1ns.telegrambudgetbot.model.*;
import me.don1ns.telegrambudgetbot.keyboard.Keyboard;
import me.don1ns.telegrambudgetbot.service.ExpenseService;
import me.don1ns.telegrambudgetbot.service.FamilyBudgetService;
import me.don1ns.telegrambudgetbot.service.IncomeService;
import me.don1ns.telegrambudgetbot.service.UserService;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


import javax.xml.crypto.Data;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;


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
                if (text != null) {
                    switch (parse(text)) {
                        case START -> {
                            if (userService.getByChatId(chatId).isEmpty()) {
                                sendResponseMessage(chatId, "Привет! Я помогу тебе планировать семейный бюджет, создай или добавься в существующий бюджет");
                                User user = new User();
                                user.setChatId(chatId);
                                user.setName(firsName);
                                user.setUserState(UserState.IDLE);
                                userService.save(user);
                                keyboard.chooseMenu(chatId);
                            } else if (userService.getByChatId(chatId).isPresent() && userService.getByChatId(chatId).get().getFamilyBudget() == null) {
                                keyboard.chooseMenu(chatId);
                            } else {
                                keyboard.mainMenu(chatId);
                            }
                        }
                        case CREATE_BUDGET -> {
                            if (userService.getByChatId(chatId).isPresent()) {
                                User user = userService.getByChatId(chatId).get();
                                if (user.getFamilyBudget() == null) {
                                    user.setUserState(UserState.CREATING_BUDGET_1);
                                    userService.save(user);
                                    sendResponseMessage(chatId, "Введите название бюджета:");
                                }
                            }
/*                            if(familyBudgetService.getByUserChatId(chatId).isEmpty() && (userService.getByChatId(chatId).isPresent())) {
                                User user = userService.getByChatId(chatId).get();
                                user.setUserState(UserState.CREATING_BUDGET_1);
                                userService.save(user);
                                sendResponseMessage(chatId, "Введите название бюджета:");
                            }*/
                        }
                        case ADD_TO_BUDGET -> {
                            if (userService.getByChatId(chatId).isPresent()) {
                                User user = userService.getByChatId(chatId).get();
                                if (user.getFamilyBudget() == null) {
                                    user.setUserState(UserState.ADD_TO_BUDGET_1);
                                    userService.save(user);
                                    sendResponseMessage(chatId, "Введите название бюджета:");
                                }
                            }
/*                            if(familyBudgetService.getByUserChatId(chatId).isEmpty() && (userService.getByChatId(chatId).isPresent())) {
                                User user = userService.getByChatId(chatId).get();
                                user.setUserState(UserState.ADD_TO_BUDGET_1);
                                userService.save(user);
                                sendResponseMessage(chatId, "Введите название бюджета:");
                            }*/
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
                        case GET_BALANCE -> {
                            User user = userService.getByChatId(chatId).get();
                            FamilyBudget familyBudget = user.getFamilyBudget();

                            int balance = familyBudget.getIncomes().stream()
                                    .mapToInt(Income::getAmount)
                                    .sum()
                                    - familyBudget.getExpenses().stream()
                                    .mapToInt(Expense::getAmount)
                                    .sum();
                            sendResponseMessage(chatId, String.valueOf(balance) + " руб.");
                        }
                        case INCOMES -> {
                            User user = userService.getByChatId(chatId).get();
                            FamilyBudget familyBudget = user.getFamilyBudget();

                            List<Income> sortedIncomes = familyBudget.getIncomes().stream()
                                    .sorted(Comparator.comparing(Income::getDate).reversed())
                                    .toList();
                            String tableName = "incomes-" + familyBudget.getId();
                            try {
                                sendResponseIncomesExcel(chatId, tableName, sortedIncomes);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }

                        }
                        case EXPENSES -> {
                            User user = userService.getByChatId(chatId).get();
                            FamilyBudget familyBudget = user.getFamilyBudget();

                            List<Expense> sortedExpenses = familyBudget.getExpenses().stream()
                                    .sorted(Comparator.comparing(Expense::getDate).reversed())
                                    .toList();
                            String tableName = "expenses-" + familyBudget.getId();
                            try {
                                sendResponseExpensesExcel(chatId, tableName, sortedExpenses);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }

                        }
                        case LAST_OPERATIONS -> {
                            User user = userService.getByChatId(chatId).get();
                            FamilyBudget familyBudget = user.getFamilyBudget();

                            List<Income> lastIncomes = familyBudget.getIncomes().stream()
                                    .sorted(Comparator.comparing(Income::getDate).reversed())
                                    .limit(5)
                                    .toList();
                            StringBuilder tableIncomes = new StringBuilder();
                            tableIncomes.append(String.format("<code>%-8s | %-13s | %-7s | %-6s</code>\n", "Дата", "Категория", "Имя", "Сумма"));
                            for (Income income : lastIncomes) {
                                tableIncomes.append(String.format("<code>%-8s | %-13s | %-7s | %-6s</code>\n", income.getDate(), income.getCategory().getText(), income.getUserName(), income.getAmount()));
                            }

                            List<Expense> lastExpenses = familyBudget.getExpenses().stream()
                                    .sorted(Comparator.comparing(Expense::getDate).reversed())
                                    .limit(5)
                                    .toList();
                            StringBuilder tableExpenses = new StringBuilder();
                            tableExpenses.append(String.format("<code>%-8s | %-13s | %-7s | %-6s</code>\n", "Дата", "Категория", "Имя", "Сумма"));
                            for (Expense expense : lastExpenses) {
                                tableExpenses.append(String.format("<code>%-8s | %-13s | %-7s | %-6s</code>\n", expense.getDate(), expense.getCategory().getText(), expense.getUserName(), expense.getAmount()));
                            }

                            SendMessage sendMessage = new SendMessage(chatId, "Последние пять доходов:\n" +
                                    tableIncomes +
                                    "\n" +
                                    "Последние пять расходов:\n" +
                                    tableExpenses);
                            sendMessage.parseMode(ParseMode.HTML);
                            SendResponse sendResponse = telegramBot.execute(sendMessage);
                            if (!sendResponse.isOk()) {
                                logger.error("Error during sending message: {}", sendResponse.description());
                            }
//                            sendResponseMessage(chatId, "Последние пять доходов:\n" +
//                                    tableIncomes +
//                                    "\n" +
//                                    "Последние пять расходов:\n" +
//                                    tableExpenses);
                        }
                        case ADD_INCOME -> {
                            keyboard.addIncome(chatId);
                        }
                        case ADD_EXPENSE -> {
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
                        case SALARY -> {
                            addIncome(chatId, Category.SALARY);
                        }
                        case BONUSES -> {
                            addIncome(chatId, Category.BONUSES);
                        }
                        case SOCIAL_PAYMENTS -> {
                            addIncome(chatId, Category.SOCIAL_PAYMENTS);
                        }
                        case SHARES -> {
                            addIncome(chatId, Category.SHARES);
                        }
                        case MONEY_TRANSFERS_INCOME -> {
                            addIncome(chatId, Category.MONEY_TRANSFERS);
                        }
                        case OTHER_INCOME -> {
                            addIncome(chatId, Category.OTHER);
                        }
                        case HELP -> {
                            sendResponseMessage(chatId, "1. Сократите излишние расходы: Проанализируйте свои расходы и определите, на что можно сэкономить. Избегайте мелких покупок, контролируйте использование энергии и принимайте меры к уменьшению счетов за мобильную связь и интернет.\n" +
                                    "2. Не занимайте деньги, если необходимо: Ограничьте использование кредитных карт и займов на минимум. Если вам все же нужно занять деньги, выбирайте возможности с наименьшей процентной ставкой и устанавливайте строгий план по возврату долга.\n" +
                                    "3. Используйте метод «50/30/20» для управления расходами: Распределите свои расходы следующим образом: 50% на необходимые жизненные нужды (питание, жилье, транспорт), 30% на желаемые вещи и развлечения, и 20% на сбережения.\n");
                        }
                        default -> {
                            if (!userService.getByChatId(chatId).get().getUserState().equals(UserState.IDLE)) {
                                User user = userService.getByChatId(chatId).get();
                                switch (user.getUserState()) {
                                    case CREATING_BUDGET_1 -> {
                                        FamilyBudget budget = new FamilyBudget();
                                        budget.setBudgetName(text);
                                        FamilyBudget savedBudget = familyBudgetService.save(budget);
                                        user.setUserState(UserState.CREATING_BUDGET_2);
                                        user.setFamilyBudget(savedBudget);
                                        userService.save(user);
                                        sendResponseMessage(chatId, "Введите ключевое слово:");
                                    }
                                    case CREATING_BUDGET_2 -> {
                                        FamilyBudget budget = user.getFamilyBudget();
                                        budget.setKeyword(text);
                                        FamilyBudget savedBudget = familyBudgetService.save(budget);
                                        user.setFamilyBudget(savedBudget);
                                        user.setUserState(UserState.IDLE);
                                        userService.save(user);
                                        sendResponseMessage(chatId, "Бюджет создан!");
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
                                        if (optionalBudget.isPresent() && optionalBudget.get().getKeyword().equals(text)) {
                                            FamilyBudget budget = optionalBudget.get();
                                            user.setUserState(UserState.IDLE);
                                            user.setFamilyBudget(budget);
                                            userService.save(user);
                                            keyboard.mainMenu(chatId);
                                        } else {

                                            sendResponseMessage(chatId, "Некорректно введены название бюджета или ключевое слово, повторите попытку!");
                                        }
                                    }
                                    case ADD_EXPENSE -> {
                                        Expense expense = new Expense();
                                        expense.setUserName(user.getName());
                                        expense.setAmount(Integer.parseInt(text));
                                        expense.setCategory(user.getIntroducedCategory());
                                        Expense savedExpense = expenseService.save(expense);
                                        FamilyBudget budget = user.getFamilyBudget();
                                        budget.getExpenses().add(savedExpense);
                                        FamilyBudget savedBudget = familyBudgetService.save(budget);
                                        user.setFamilyBudget(savedBudget);
                                        user.setUserState(UserState.IDLE);
                                        userService.save(user);
                                        sendResponseMessage(chatId, "Расход был добавлен!");
                                        keyboard.mainMenu(chatId);

                                    }
                                    case ADD_INCOME -> {
                                        Income income = new Income();
                                        income.setUserName(user.getName());
                                        income.setAmount(Integer.parseInt(text));
                                        income.setCategory(user.getIntroducedCategory());
                                        Income savedIncome = incomeService.save(income);
                                        FamilyBudget budget = user.getFamilyBudget();
                                        budget.getIncomes().add(savedIncome);
                                        FamilyBudget savedBudget = familyBudgetService.save(budget);
                                        user.setFamilyBudget(savedBudget);
                                        user.setUserState(UserState.IDLE);
                                        userService.save(user);
                                        sendResponseMessage(chatId, "Доход был добавлен!");
                                        keyboard.mainMenu(chatId);

                                    }
                                }
                            } else {
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
    private void sendResponseExpensesExcel(long chatId, String tableName, List<Expense> expenses) throws IOException {
        SendDocument sendDocument = new SendDocument(chatId, createExpensesExcel(tableName, expenses));
        SendResponse sendResponse = telegramBot.execute(sendDocument);
        if (!sendResponse.isOk()) {
            logger.error("Error during sending message: {}", sendResponse.description());
        }
    }

    private File createExpensesExcel(String tableName, List<Expense> expenses) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet(tableName);
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Дата");
        headerRow.createCell(1).setCellValue("Категория");
        headerRow.createCell(2).setCellValue("Имя пользователя");
        headerRow.createCell(3).setCellValue("Сумма");

        for (int i = 0; i < expenses.size(); i++) {
            Expense expense = expenses.get(i);
            Row row = sheet.createRow(i + 1);
            row.createCell(0).setCellValue(expense.getDate());
            row.createCell(1).setCellValue(expense.getCategory().getText());
            row.createCell(2).setCellValue(expense.getUserName());
            row.createCell(3).setCellValue(expense.getAmount());
        }
        File tempFile = File.createTempFile(tableName, ".xlsx");
        FileOutputStream fileOutputStream = new FileOutputStream(tempFile);
        workbook.write(fileOutputStream);
        fileOutputStream.close();
        return tempFile;
    }
    private void sendResponseIncomesExcel(long chatId, String tableName, List<Income> incomes) throws IOException {
        SendDocument sendDocument = new SendDocument(chatId, createIncomesExcel(tableName, incomes));
        SendResponse sendResponse = telegramBot.execute(sendDocument);
        if (!sendResponse.isOk()) {
            logger.error("Error during sending message: {}", sendResponse.description());
        }
    }

    private File createIncomesExcel(String tableName, List<Income> incomes) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet(tableName);
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Дата");
        headerRow.createCell(1).setCellValue("Категория");
        headerRow.createCell(2).setCellValue("Имя пользователя");
        headerRow.createCell(3).setCellValue("Сумма");

        for (int i = 0; i < incomes.size(); i++) {
            Income income = incomes.get(i);
            Row row = sheet.createRow(i + 1);
            row.createCell(0).setCellValue(income.getDate());
            row.createCell(1).setCellValue(income.getCategory().getText());
            row.createCell(2).setCellValue(income.getUserName());
            row.createCell(3).setCellValue(income.getAmount());
        }
        File tempFile = File.createTempFile(tableName, ".xlsx");
        FileOutputStream fileOutputStream = new FileOutputStream(tempFile);
        workbook.write(fileOutputStream);
        fileOutputStream.close();
        return tempFile;
    }

    private void addExpense(Long chatId, Category category) {
        User user = userService.getByChatId(chatId).get();
        user.setUserState(UserState.ADD_EXPENSE);
        user.setIntroducedCategory(category);
        userService.save(user);
        sendResponseMessage(chatId, "Введите сумму:");
    }

    private void addIncome(Long chatId, Category category) {
        User user = userService.getByChatId(chatId).get();
        user.setUserState(UserState.ADD_INCOME);
        user.setIntroducedCategory(category);
        userService.save(user);
        sendResponseMessage(chatId, "Введите сумму:");
    }
}