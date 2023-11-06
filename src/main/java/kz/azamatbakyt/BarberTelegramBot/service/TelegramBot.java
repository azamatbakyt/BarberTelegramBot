package kz.azamatbakyt.BarberTelegramBot.service;


import com.vdurmont.emoji.EmojiParser;
import kz.azamatbakyt.BarberTelegramBot.config.BotConfig;
import kz.azamatbakyt.BarberTelegramBot.helpers.CallbackType;
import kz.azamatbakyt.BarberTelegramBot.helpers.YesNoCommands;
import kz.azamatbakyt.BarberTelegramBot.entity.Client;
import kz.azamatbakyt.BarberTelegramBot.entity.CustomerService;
import kz.azamatbakyt.BarberTelegramBot.entity.CustomerServiceGroup;
import kz.azamatbakyt.BarberTelegramBot.service.CustomerServiceGroupService;
import kz.azamatbakyt.BarberTelegramBot.service.CSService;
import kz.azamatbakyt.BarberTelegramBot.service.ClientService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@Component
public class TelegramBot extends TelegramLongPollingBot {


    @Autowired
    private CSService csService;
    @Autowired
    private CustomerServiceGroupService customerServiceGroupService;
    @Autowired
    private ClientService clientsService;

    private final BotConfig config;

    private final static String HELP_TEXT = "This bot is created to demonstrate Spring capabilities.\n\n" +
            "You can execute commands from the main menu on the left or by typing commands:\n\n" +
            "Type /start to see welcome message\n\n" +
            "Type /services to see welcome message\n\n" +
            "Type /register to register yourself\n\n" +
            "Type /mydata to see data stored about yourself\n\n" +
            "Type /help to see this message again";


    private final static String ALL_SERVICES = "What service would you want to choose?";

    public TelegramBot(BotConfig config) {
        this.config = config;
    }

    private final static String HELLO_MESSAGE = "Здравствуй, дорогой пользователь!" +
            " Я смотрю ты у нас новый посетитель. Давай пройдем мини регистрацию. \n\n Напиши свое имя:";

    private final static String ASK_PHONE = "А теперь пожалуйста напишите ваш номер телефона: ";
    private final static String REGISTRATION_SUCCESSFUL = "Спасибо! Регистрация прошла успешно.";
    @Override
    public String getBotUsername() {
        return config.getBotName();
    }

    @Override
    public String getBotToken() {
        return config.getToken();
    }

    @Override
    public void onUpdateReceived(Update update) {

        if (update.hasMessage() && update.getMessage().hasText()) {
            String message = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();
            Client client = clientsService.getClientByChatId(chatId);

            if (client == null) {
                sendMessage(chatId, HELLO_MESSAGE, false);
                client = new Client();
                client.setChatId(chatId);
                clientsService.save(client);
            } else{
                if (client.getName() == null) {
                    client.setName(message);
                    sendMessage(chatId, ASK_PHONE, client.isRegistrationCompleted());
                    clientsService.save(client);
                } else if (client.getPhone() == null) {
                    client.setPhone(message);
                    client.setRegistrationCompleted(true);
                    sendMessage(chatId, REGISTRATION_SUCCESSFUL, true);
                    clientsService.save(client);
                    String answer = EmojiParser.parseToUnicode("Добро пожаловать, " + client.getName()
                            + ", что я могу вам предложить!" + "\uD83D\uDE0A");
                    sendMessage(chatId, answer, true);
                } else {
                    switch (message) {
                        case "/start":
                            String answer = EmojiParser.parseToUnicode("И снова здравствуйте, " + client.getName()
                                    + ", что я могу вам предложить!" + "\uD83D\uDE0A");
                            sendMessage(chatId, answer, true);
                            break;
                        case "Help":
                            sendHelp(chatId, HELP_TEXT);
                            break;
                        case "Услуги":
                            sendServices(chatId);
                            break;
                    }
                }
            }

        } else if (update.hasCallbackQuery()) {
            String callback = update.getCallbackQuery().getData();
            String[] callbackData = callback.split("%");
            CallbackType callbackType = CallbackType.valueOf(callbackData[0]);
            String callbackName = callbackData[1];
            long messageId = update.getCallbackQuery().getMessage().getMessageId();
            long chatId = update.getCallbackQuery().getMessage().getChatId();

            switch (callbackType) {
                case SERVICE_GROUP:
                    CustomerServiceGroup group = customerServiceGroupService.getServiceGroupByName(callbackName);
                    choiceServiceGroup(chatId, messageId, group);
                    break;
                case SERVICE:
                    CustomerService service = csService.getServiceByName(callbackName);
                    choiceService(chatId, messageId, service);
                    break;

            }


        }

    }


    private void choiceService(long chatId, long messageId, CustomerService customerService) {
        String text = "Вы выбрали " + customerService.getName() + ". \n" +
                "Ниже представлена информация про услугу:\n";
        EditMessageText message = new EditMessageText();
        message.setChatId(String.valueOf(chatId));
        message.setMessageId((int) messageId);
        message.setText(text + "\n" + "Услуга: " + customerService.getName() + "\n" +
                "Цена: " + customerService.getPrice() + "\n" +
                "Длительность: " + customerService.getDuration() + "\n" +
                "Вы хотите заказать эту услугу?");
        message.setReplyMarkup(yesNoCommandKeyboard());
        try {
            execute(message);
        } catch (TelegramApiException e) {
            System.out.println(e.getMessage());
        }
    }

    private void choiceServiceGroup(long chatId, long messageId, CustomerServiceGroup group) {
        String text = "Вы выбрали " + group.getName() + " . \nДавайте выберем какую именно услугу вы хотите?";
        EditMessageText message = new EditMessageText();
        message.setChatId(String.valueOf(chatId));
        message.setText(text);
        message.setMessageId((int) messageId);
        message.setReplyMarkup(haircutInlineKeyboard(group));
        try {
            execute(message);
        } catch (TelegramApiException e) {
            System.out.println(e.getMessage());
        }
    }

    private InlineKeyboardMarkup haircutInlineKeyboard(CustomerServiceGroup group) {
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<CustomerService> services = csService.getServices()
                .stream()
                .filter(s -> s.getGroup().getId().equals(group.getId()))
                .collect(Collectors.toList());

        List<InlineKeyboardButton> currentRow = new ArrayList<>();
        for (CustomerService service : services) {
            InlineKeyboardButton haircutMen = new InlineKeyboardButton();
            haircutMen.setText(service.getName());
            haircutMen.setCallbackData(CallbackType.SERVICE + "%" + service.getName());

            currentRow.add(haircutMen);

            if (currentRow.size() == 2) {
                rowsInline.add(currentRow);
                currentRow = new ArrayList<>();
            }
        }

        if (!currentRow.isEmpty()) {
            rowsInline.add(currentRow);
        }

        markupInline.setKeyboard(rowsInline);
        return markupInline;
    }


    private void sendHelp(long chatId, String helpText) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chatId));
        sendMessage.setText(helpText);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            System.out.println(e.getMessage());
        }
    }


    private void register(long chatId, String message) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chatId));
        sendMessage.setText(message);
        sendMessage.setReplyMarkup(registerKeyboard());
        try {
            execute(sendMessage);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private InlineKeyboardMarkup registerKeyboard() {
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> registerList = new ArrayList<>();

        InlineKeyboardButton register = new InlineKeyboardButton();
        register.setText("Register");
        register.setCallbackData(String.valueOf(CallbackType.REGISTER));


        registerList.add(register);
        rowsInline.add(registerList);

        markupInline.setKeyboard(rowsInline);
        return markupInline;

    }

    private void sendMessage(long chatId, String textToSend, boolean registered) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chatId));
        sendMessage.setText(textToSend);

        if (registered) {
            sendMessage.setReplyMarkup(startCommandKeyboard());
        } else {
            sendMessage.setReplyMarkup(ReplyKeyboardRemove.builder().removeKeyboard(true).build());
        }

        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            System.out.println(e.getMessage());
        }

    }

    private void sendServices(long chatId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chatId));
        sendMessage.setText(ALL_SERVICES);
        sendMessage.setReplyMarkup(services());
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            System.out.println(e.getMessage());
        }
    }

    private ReplyKeyboardMarkup startCommandKeyboard() {

        ReplyKeyboardMarkup keyboard = new ReplyKeyboardMarkup();
        List<KeyboardRow> rows = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();

        row.add("Услуги");
        row.add("Мои работы");
        rows.add(row);

        row = new KeyboardRow();
        row.add("Гайды");
        rows.add(row);

        keyboard.setKeyboard(rows);

        return keyboard;
    }

    private InlineKeyboardMarkup services() {
        List<CustomerServiceGroup> serviceGroups = customerServiceGroupService.getServiceGroups();
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> currentRow = new ArrayList<>();
        for (CustomerServiceGroup serviceGroup : serviceGroups) {

            InlineKeyboardButton buttonHaircutService = new InlineKeyboardButton();
            buttonHaircutService.setText(serviceGroup.getName());
            buttonHaircutService.setCallbackData(CallbackType.SERVICE_GROUP + "%" + serviceGroup.getName());
            currentRow.add(buttonHaircutService);

            if (currentRow.size() == 2) {
                rowsInline.add(currentRow);
                currentRow = new ArrayList<>();
            }
        }

        if (!currentRow.isEmpty()) {
            rowsInline.add(currentRow);
        }


        markupInline.setKeyboard(rowsInline);
        return markupInline;
    }


    private InlineKeyboardMarkup yesNoCommandKeyboard() {
        InlineKeyboardMarkup markupIn = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();

        List<InlineKeyboardButton> answerList = new ArrayList<>();
        InlineKeyboardButton yesBtn = new InlineKeyboardButton();
        yesBtn.setText("Да");
        yesBtn.setCallbackData(String.valueOf(YesNoCommands.YES));

        InlineKeyboardButton noBtn = new InlineKeyboardButton();
        noBtn.setText("Нет");
        noBtn.setCallbackData(String.valueOf(YesNoCommands.NO));
        answerList.add(yesBtn);
        answerList.add(noBtn);

        rowsInline.add(answerList);
        markupIn.setKeyboard(rowsInline);
        return markupIn;
    }


}