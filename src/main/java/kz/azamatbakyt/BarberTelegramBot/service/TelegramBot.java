package kz.azamatbakyt.BarberTelegramBot.service;


import com.vdurmont.emoji.EmojiParser;
import kz.azamatbakyt.BarberTelegramBot.customerServices.*;
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

@Slf4j
@Component
public class TelegramBot extends TelegramLongPollingBot {


    @Autowired
    private CustomerServiceRepository customerServiceRepository;

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
            String callbackData = update.getCallbackQuery().getData();
            long messageId = update.getCallbackQuery().getMessage().getMessageId();
            long chatId = update.getCallbackQuery().getMessage().getChatId();

            if (callbackData.equals(String.valueOf(Services.HAIRCUT))) {
                choiceHaircut(chatId, messageId);

            } else if (callbackData.equals(String.valueOf(Services.SHAVING))) {
                choiceShaving(chatId, messageId);
            }
//            } else if (callbackData.equals(String.valueOf(Services.OTHERS))) {
//                choiceOthers(chatId, messageId);
//            }
            else if (callbackData.equals(String.valueOf(Services.COMPLEX))) {
                choiceComplex(chatId, messageId);
            } else if (callbackData.equals(String.valueOf(HaircutServices.MENS_HAIRCUT))) {
                serviceHaircutMan(chatId, messageId);
            } else if (callbackData.equals(String.valueOf(ComplexServices.HAIRCUTTING))) {
                serviceHaircutting(chatId, messageId);
            }

        }

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
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();

        // row haircut and beard
        List<InlineKeyboardButton> rowHaircutAndBeard = new ArrayList<>();

        InlineKeyboardButton buttonHaircutService = new InlineKeyboardButton();
        buttonHaircutService.setText("Стрижка");
        buttonHaircutService.setCallbackData(String.valueOf(Services.HAIRCUT));

        InlineKeyboardButton buttonBeardService = new InlineKeyboardButton();
        buttonBeardService.setText("Борода");
        buttonBeardService.setCallbackData(String.valueOf(Services.SHAVING));

        rowHaircutAndBeard.add(buttonHaircutService);
        rowHaircutAndBeard.add(buttonBeardService);

        // row complex and Camouflage
        List<InlineKeyboardButton> rowComplexAndCamouflage = new ArrayList<>();

        InlineKeyboardButton buttonComplex = new InlineKeyboardButton();
        buttonComplex.setText("Комплекс");
        buttonComplex.setCallbackData(String.valueOf(Services.COMPLEX));

        InlineKeyboardButton buttonCamouflage = new InlineKeyboardButton();
        buttonCamouflage.setText("Камуфляж");
        buttonCamouflage.setCallbackData(String.valueOf(Services.CAMOUFLAGE));

        rowComplexAndCamouflage.add(buttonComplex);
        rowComplexAndCamouflage.add(buttonCamouflage);

        // row Others

        List<InlineKeyboardButton> rowOther = new ArrayList<>();

        InlineKeyboardButton others = new InlineKeyboardButton();
        others.setText("Другие");
        others.setCallbackData(String.valueOf(Services.OTHERS));

        rowOther.add(others);


        rowsInline.add(rowHaircutAndBeard);
        rowsInline.add(rowComplexAndCamouflage);
        rowsInline.add(rowOther);
        markupInline.setKeyboard(rowsInline);

        return markupInline;
    }


    private void choiceHaircut(long chatId, long messageId) {
        String text = "Вы выбрали Стрижку. \nДавайте выберем какую именно услугу вы хотите?";
        EditMessageText message = new EditMessageText();
        message.setChatId(String.valueOf(chatId));
        message.setText(text);
        message.setMessageId((int) messageId);
        message.setReplyMarkup(haircutInlineKeyboard());
        try {
            execute(message);
        } catch (TelegramApiException e) {
            System.out.println(e.getMessage());
        }
    }

    private InlineKeyboardMarkup haircutInlineKeyboard() {
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();

        List<InlineKeyboardButton> haircutServices = new ArrayList<>();
        InlineKeyboardButton haircutMen = new InlineKeyboardButton();
        haircutMen.setText("Стрижка Мужская");
        haircutMen.setCallbackData(String.valueOf(HaircutServices.MENS_HAIRCUT));

        InlineKeyboardButton haircutKids = new InlineKeyboardButton();
        haircutKids.setText("Стрижка Детская");
        haircutKids.setCallbackData(String.valueOf(HaircutServices.HAIRCUT_KIDS));


        List<InlineKeyboardButton> haircutMachine = new ArrayList<>();
        InlineKeyboardButton haircutByMachine = new InlineKeyboardButton();
        haircutByMachine.setText("Стрижка машинкой");
        haircutByMachine.setCallbackData(String.valueOf(HaircutServices.HAIRCUT_MACHINE));

        haircutServices.add(haircutMen);
        haircutServices.add(haircutKids);
        haircutMachine.add(haircutByMachine);

        rowsInline.add(haircutServices);
        rowsInline.add(haircutMachine);
        markupInline.setKeyboard(rowsInline);
        return markupInline;
    }

    private void choiceShaving(long chatId, long messageId) {
        String text = "Вы выбрали Стрижку бороды. \nДавайте выберем какую именно услугу вы хотите?";
        EditMessageText message = new EditMessageText();
        String[] beard = serviceBeard();
        message.setChatId(String.valueOf(chatId));
        message.setText(text + "\n" +
                "Услуга: " + beard[0] + "\n" +
                "Стоимость: " + beard[1] + "тг\n" +
                "Длительность: " + beard[2]);
        message.setMessageId((int) messageId);
        message.setReplyMarkup(beardInlineKeyboard());


        try {
            execute(message);
        } catch (TelegramApiException e) {
            System.out.println(e.getMessage());
        }
    }

    private InlineKeyboardMarkup beardInlineKeyboard() {
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();

        List<InlineKeyboardButton> beardList = new ArrayList<>();
        InlineKeyboardButton beardCorrection = new InlineKeyboardButton();
        beardCorrection.setText("Коррекция бороды");
        beardCorrection.setCallbackData(String.valueOf(BeardServices.BEARD_CORRECTION));

        List<InlineKeyboardButton> royalShaveList = new ArrayList<>();
        InlineKeyboardButton royalShave = new InlineKeyboardButton();
        royalShave.setText("Королевская бритье");
        royalShave.setCallbackData(String.valueOf(BeardServices.ROYAL_SHAVE));


        beardList.add(beardCorrection);
        royalShaveList.add(royalShave);

        rowsInline.add(beardList);
        rowsInline.add(royalShaveList);
        markupInline.setKeyboard(rowsInline);
        return markupInline;
    }

    private void choiceComplex(long chatId, long messageId) {
        String text = "Вы выбрали Комплекс. \nНа данный момент у меня доступна одна услуга";
        EditMessageText message = new EditMessageText();
        message.setChatId(String.valueOf(chatId));
        message.setMessageId((int) messageId);
        message.setText(text + "\n");
        message.setReplyMarkup(complexInlineKeyboard());
        try {
            execute(message);
        } catch (TelegramApiException e) {
            System.out.println(e.getMessage());
        }
    }

    private InlineKeyboardMarkup complexInlineKeyboard() {
        InlineKeyboardMarkup markupIn = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        String[] complex = serviceComplex();
        List<InlineKeyboardButton> compexList = new ArrayList<>();
        InlineKeyboardButton complexBtn = new InlineKeyboardButton();
        complexBtn.setText("Стрижика+Борода");
        complexBtn.setCallbackData(String.valueOf(ComplexServices.HAIRCUTTING));
        compexList.add(complexBtn);

        rowsInline.add(compexList);
        markupIn.setKeyboard(rowsInline);
        return markupIn;
    }


//    private void choiceOthers(long chatId, long messageId) {
//        String text = "Вы выбрали Прочее. \nДавайте выберем какую именно услугу вы хотите?";
//        EditMessageText message = new EditMessageText();
//        String[] cleaning = serviceCleaningMask();
//        message.setChatId(String.valueOf(chatId));
//        message.setMessageId((int) messageId);
//        message.setText(text + "\n" +
//                "Услуга: " + cleaning[0] + "\n" +
//                "Цена: " + cleaning[1] + "тг\n" +
//                "Длительность: " + cleaning[2]);
//        message.setReplyMarkup(othersInlineKeyboard());
//        try {
//            execute(message);
//        } catch (TelegramApiException e) {
//            System.out.println(e.getMessage());
//        }
//    }

    private InlineKeyboardMarkup othersInlineKeyboard() {
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();

        List<InlineKeyboardButton> othersList1 = new ArrayList<>();
        InlineKeyboardButton edging = new InlineKeyboardButton();
        edging.setText("Окантовка");
        edging.setCallbackData(String.valueOf(OthersServices.EDGING));

        List<InlineKeyboardButton> hairRemovingList = new ArrayList<>();
        InlineKeyboardButton hairRemoving = new InlineKeyboardButton();
        hairRemoving.setText("Удаление волос воском");
        hairRemoving.setCallbackData(String.valueOf(OthersServices.HAIR_REMOVING));

        List<InlineKeyboardButton> othersListCleaning = new ArrayList<>();
        InlineKeyboardButton cleaningFace = new InlineKeyboardButton();
        cleaningFace.setText("Чистка лица(скраб+глина)");
        cleaningFace.setCallbackData(String.valueOf(OthersServices.CLEANING_FACE));


        othersList1.add(edging);
        hairRemovingList.add(hairRemoving);
        othersListCleaning.add(cleaningFace);

        rowsInline.add(othersList1);
        rowsInline.add(hairRemovingList);
        rowsInline.add(othersListCleaning);
        markupInline.setKeyboard(rowsInline);
        return markupInline;
    }

    private String[] serviceHaircutMen() {
        List<CustomerService> customerServices = customerServiceRepository.findAll();
        String[] haircut = new String[3];
        for (CustomerService customerService : customerServices) {
            if (customerService.getName().equals("Стрижка мужская")) {
                haircut[0] = customerService.getName();
                haircut[1] = customerService.getPrice();
                haircut[2] = customerService.getDuration();
            }
        }
        return haircut;
    }

    private String[] serviceBeard() {
        List<CustomerService> customerServices = customerServiceRepository.findAll();
        String[] beard = new String[3];
        for (CustomerService customerService : customerServices) {
            if (customerService.getName().equals("Борода")) {
                beard[0] = customerService.getName();
                beard[1] = customerService.getPrice();
                beard[2] = customerService.getDuration();
            }
        }
        return beard;
    }

    private String[] serviceComplex() {
        List<CustomerService> services = customerServiceRepository.findAll();
        String[] haircutAndMask = new String[3];
        for (CustomerService service : services) {
            if (service.getName().contains("Стрижка+Борода")) {
                haircutAndMask[0] = service.getName();
                haircutAndMask[1] = service.getPrice();
                haircutAndMask[2] = service.getDuration();
            }
        }
        return haircutAndMask;
    }

    private void serviceHaircutMan(long chatId, long messageId) {
        String text = "Вы выбрали Стрижка Мужская. \n" +
                "Ниже представлена информация про услугу:\n";
        EditMessageText message = new EditMessageText();
        message.setChatId(String.valueOf(chatId));
        message.setMessageId((int) messageId);
        String[] service = serviceHaircutMen();
        message.setText(text + "\n" + "Услуга: " + service[0] + "\n" +
                "Цена: " + service[1] + "\n" +
                "Длительность: " + service[2] + "\n" +
                "Вы хотите заказать эту услугу?");
        message.setReplyMarkup(yesNoCommandKeyboard());
        try {
            execute(message);
        } catch (TelegramApiException e) {
            System.out.println(e.getMessage());
        }
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

    private void serviceHaircutting(long chatId, long messageId) {
        String text = "Вы выбрали Стрижка+Борода. \nНиже представлена информация про услугу:\n";
        EditMessageText message = new EditMessageText();
        message.setChatId(String.valueOf(chatId));
        message.setMessageId((int) messageId);
        String[] service = serviceComplex();
        message.setText(text + "\n" + "Услуга: " + service[0] + "\n" +
                "Цена: " + service[1] + "\n" +
                "Длительность: " + service[2] + "\n" +
                "Вы хотите заказать эту услугу?");
        message.setReplyMarkup(yesNoCommandKeyboard());
        try {
            execute(message);
        } catch (TelegramApiException e) {
            System.out.println(e.getMessage());
        }
    }


}

