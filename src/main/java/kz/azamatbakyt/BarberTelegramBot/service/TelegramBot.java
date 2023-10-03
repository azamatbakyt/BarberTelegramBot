package kz.azamatbakyt.BarberTelegramBot.service;


import com.vdurmont.emoji.EmojiParser;
import kz.azamatbakyt.BarberTelegramBot.customerServices.*;
import kz.azamatbakyt.BarberTelegramBot.entity.CustomerServiceGroup;
import kz.azamatbakyt.BarberTelegramBot.repository.CustomerServiceGroupRepository;
import kz.azamatbakyt.BarberTelegramBot.repository.CustomerServiceRepository;
import kz.azamatbakyt.BarberTelegramBot.config.BotConfig;
import kz.azamatbakyt.BarberTelegramBot.customerServices.ComplexServices;
import kz.azamatbakyt.BarberTelegramBot.entity.CustomerService;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
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
    private CustomerServiceRepository customerServiceRepository;
    @Autowired
    private CustomerServiceGroupRepository customerServiceGroupRepository;

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

//    @PostConstruct
//    private void init() {
//        List<BotCommand> listOfCommands = new ArrayList();
//        listOfCommands.add(new BotCommand("/start", "get a welcome message"));
//        listOfCommands.add(new BotCommand("/services", "Services"));
//        listOfCommands.add(new BotCommand("/mydata", "get your data stored"));
//        listOfCommands.add(new BotCommand("/help", "info how to use this bot"));
//        try {
//            this.execute(new SetMyCommands(listOfCommands,
//                    new BotCommandScopeDefault(),
//                    null));
//        } catch (TelegramApiException e) {
//            e.printStackTrace();
//        }
//    }


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

            switch (message) {
                case "/start":
                    startCommandReceived(chatId, update.getMessage().getChat().getFirstName());
                    break;
                case "Help":
                    sendHelp(chatId, HELP_TEXT);
                    break;
                case "Services":
                    sendServices(chatId, ALL_SERVICES);
                    break;
                default:
                    sendMessage(chatId, "Sorry command wasn't recognized");

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
                    CustomerServiceGroup group = customerServiceGroupRepository.findByName(callbackName);
                    choiceServiceGroup(chatId, messageId, group);
                    break;
                case SERVICE:
                    CustomerService service = customerServiceRepository.findByName(callbackName);
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
        List<CustomerService> services = customerServiceRepository.findAll()
                .stream()
                .filter(s -> s.getGroup().getId().equals(group.getId()))
                .collect(Collectors.toList());
        List<InlineKeyboardButton> haircutServices = new ArrayList<>();
        for (CustomerService service : services) {
            InlineKeyboardButton haircutMen = new InlineKeyboardButton();
            haircutMen.setText(service.getName());
            haircutMen.setCallbackData(CallbackType.SERVICE + "%" + service.getName());
            haircutServices.add(haircutMen);
        }
        rowsInline.add(haircutServices);
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

    private void startCommandReceived(long chatId, String name) {
        String answer = EmojiParser.parseToUnicode("Hi, " + name + ", nice to meet you!" + "\uD83D\uDE0A");
        sendMessage(chatId, answer);
    }

    private void sendMessage(long chatId, String textToSend) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chatId));
        sendMessage.setText(textToSend);
        sendMessage.setReplyMarkup(startCommandKeyboard());

        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            System.out.println(e.getMessage());
        }

    }

    private void sendServices(long chatId, String textToSend) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chatId));
        sendMessage.setText(textToSend);
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

        row.add("Services");
        row.add("My works");
        rows.add(row);

        row = new KeyboardRow();
        row.add("My history");
        rows.add(row);

        keyboard.setKeyboard(rows);

        return keyboard;
    }

    private InlineKeyboardMarkup services() {
        List<CustomerServiceGroup> serviceGroups = customerServiceGroupRepository.findAll();
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowHaircutAndBeard = new ArrayList<>();
        for (CustomerServiceGroup serviceGroup : serviceGroups) {

            InlineKeyboardButton buttonHaircutService = new InlineKeyboardButton();
            buttonHaircutService.setText(serviceGroup.getName());
            buttonHaircutService.setCallbackData(CallbackType.SERVICE_GROUP + "%" + serviceGroup.getName());
            rowHaircutAndBeard.add(buttonHaircutService);
        }
        rowsInline.add(rowHaircutAndBeard);
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

