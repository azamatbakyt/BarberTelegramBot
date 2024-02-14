package kz.azamatbakyt.BarberTelegramBot.service.bot;


import com.vdurmont.emoji.EmojiParser;
import jakarta.annotation.PostConstruct;
import kz.azamatbakyt.BarberTelegramBot.config.BotConfig;
import kz.azamatbakyt.BarberTelegramBot.entity.*;
import kz.azamatbakyt.BarberTelegramBot.helpers.CallbackTypes;
import kz.azamatbakyt.BarberTelegramBot.helpers.Status;
import kz.azamatbakyt.BarberTelegramBot.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@Slf4j
//@Component
public class TelegramBot extends TelegramLongPollingBot {


    @Autowired
    private CSService csService;
    @Autowired
    private CustomerServiceGroupService customerServiceGroupService;
    @Autowired
    private ClientService clientsService;
    @Autowired
    private ScheduleService scheduleService;
    @Autowired
    private TimeslotService timeslotService;
    @Autowired
    private AppointmentService appointmentService;
    @Autowired
    private PortfolioService portfolioService;
    @Autowired
    private AppointmentTimeslotService appointmentTimeslotService;
    private final BotConfig config;

    private final static String HELP_TEXT = "This bot is created to demonstrate Spring capabilities.\n\n" +
            "You can execute commands from the main menu on the left or by typing commands:\n\n" +
            "Type /start to see welcome message\n\n" +
            "Type /services to see welcome message\n\n" +
            "Type /register to register yourself\n\n" +
            "Type /mydata to see data stored about yourself\n\n" +
            "Type /help to see this message again";


    private final static String ALL_SERVICES = "What service would you want to choose?";

    private final static String BARBER_PHONE_NUMBER = "Номер телефона: +7(747) 917-73-66";

    public TelegramBot(BotConfig config) {
        this.config = config;
    }

    private final static String HELLO_MESSAGE = "Здравствуй, дорогой пользователь!" +
            " Я смотрю ты у нас новый посетитель. Давай пройдем мини регистрацию. \n\n Напиши свое имя:";

    private final static String ASK_PHONE = "А теперь пожалуйста напишите ваш номер телефона: ";
    private final static String REGISTRATION_SUCCESSFUL = "Спасибо! Регистрация прошла успешно.";
    private final static String CANCEL_ACTION_TEXT = "Вы только что отменили действие! \nПрошу вас начните заново! \nСпасибо за ваше понимание!";
    private final static String PORTFOLIO_TEXT = "Фотографии, как отражение души. " +
            "Добро пожаловать в галерею моего творчества!";

    @Override
    public String getBotUsername() {
        return config.getBotName();
    }

    @Override
    public String getBotToken() {
        return config.getToken();
    }

    // TODO перенести все в отдельный сервис
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
            } else {
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
                        case "Мои работы":
                            try {
                                sendMessage(chatId, PORTFOLIO_TEXT, true);
                                showPortfolio(chatId);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                            break;
                        case "Мои активные записи":
                            sendAllActiveAppointments(chatId, appointmentService.getActiveAppointments(chatId));
                            sendMessage(chatId, "Во избежания ошибок при повторном бронировании " +
                                    "прошу вас перезапустить бота с помощью кнопки 'Перезапустить бота'", true);
                            break;
                        case "Связаться с барбером":
                            sendMessage(chatId, BARBER_PHONE_NUMBER, true);
                            break;
                        case "Управление бронированием":
                            appointmentManagement(chatId);
                            break;
                    }
                }
            }

        } else if (update.hasCallbackQuery()) {

            String callback = update.getCallbackQuery().getData();
            String[] callbackData = callback.split("%");
            CallbackTypes callbackTypes = CallbackTypes.valueOf(callbackData[0]);
            String callbackName = callbackData[1];
            long messageId = update.getCallbackQuery().getMessage().getMessageId();
            long chatId = update.getCallbackQuery().getMessage().getChatId();

            switch (callbackTypes) {
                case SERVICE_GROUP:
                    CustomerServiceGroup group = customerServiceGroupService.getServiceGroupByName(callbackName);
                    choiceServiceGroup(chatId, messageId, group);
                    break;

                case SERVICE:
                    CustomerService service = csService.getServiceByName(callbackName);
                    choiceService(chatId, messageId, service);
                    break;

                case YES_FOR_CREATING_APPOINTMENT:
                    CustomerService serviceForBooking = csService.getServiceByName(callbackName);
                    appointmentService.save(new Appointment(clientsService.getClientByChatId(chatId), serviceForBooking, Status.SERVICE_SELECTED.toString()));
                    choiceDayForBooking(chatId, messageId, serviceForBooking);
                    break;

                case CHOOSE_TIMESLOT:
                    LocalDate dayForBooking = LocalDate.parse(callbackName);
                    Appointment appointment = appointmentService.updateDateOfBookingByChatId(chatId, dayForBooking, Status.DATE_SELECTED);
                    appointmentTimeslotService.save(
                            List.of(
                                    new AppointmentTimeslot(appointment, null)
                            )
                    );
                    choiceTimeslot(chatId, messageId, dayForBooking, appointment.getService());

                    break;

                case MAKE_APPOINTMENT:
                    LocalTime parsedTimeslot = LocalTime.parse(callbackName);
                    List<LocalTime> times = new ArrayList<>();
                    Appointment appointmentByChatId = appointmentService.getNotCreatedAppointmentByChatId(chatId, Status.DATE_SELECTED);
                    int duration = appointmentByChatId.getService().getDuration();
                    int timeslotQty = duration / 60;
                    for (int i = 0; i < timeslotQty; i++) {
                        times.add(parsedTimeslot.plusHours(i));
                    }
                    List<Timeslot> timeslots = timeslotService.getTimeslots(times);


                    if (timeslotQty == 2) {
                        // TODO Сделать метод который возвращает String и передать его в confirmAppointment()
                        confirmAppointment(chatId, messageId, timeslots);
                        appointmentTimeslotService.update(timeslots, appointmentByChatId);

                    }
                    Timeslot timeslot = timeslots.get(0);
                    confirmAppointment(chatId, messageId, timeslot);
                    appointmentTimeslotService.update(timeslots, appointmentByChatId);
                    appointmentService.updateAppointmnetByStatus(chatId, Status.DATE_SELECTED, Status.TIMESLOT_SELECTED);                    break;

                case APPOINTMENT_CREATED:
                    appointmentService.updateAppointmnetByStatus(chatId, Status.TIMESLOT_SELECTED, Status.BOOKING_SUCCESSFUL);
                    showAllActiveAppointments(chatId, messageId, appointmentService.getActiveAppointments(chatId));
                    sendMessage(chatId, "Во избежания ошибок при повторном бронировании " +
                            "прошу вас перезапустить бота с помощью кнопки 'Перезапустить бота'", true);
                    break;
                case VIEW_APPOINTMENT:
                    editAppointment(Long.parseLong(callbackName), chatId);
                    break;

                case DELETED_APPOINTMENT:
                    Long atId = Long.parseLong(callbackName);
                    appointmentDeleteSuccessfull(chatId, atId);
                    appointmentService.delete(appointmentTimeslotService.getAllById(atId).getAppointment().getId());
                    break;
                case CANCEL_ACTION:
                    sendMessage(chatId, CANCEL_ACTION_TEXT, true);
            }


        }

    }

    @PostConstruct
    private void commands() {
        List<BotCommand> listOfCommands = new ArrayList<>();
        listOfCommands.add(new BotCommand("/start", "Перезапустить бота"));

        try {
            this.execute(new SetMyCommands(listOfCommands, new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e) {
            System.out.println(e.getMessage());
        }
    }

    private void choiceService(long chatId, long messageId, CustomerService customerService) {
        String text = "Вы выбрали - " + customerService.getName() + ". \n" +
                "Ниже представлена информация про услугу:\n";
        EditMessageText message = new EditMessageText();
        message.setChatId(String.valueOf(chatId));
        message.setMessageId((int) messageId);
        message.setText(text + "\n" + "Услуга: " + customerService.getName() + "\n" +
                "Цена: " + customerService.getPrice() + "тг" + "\n" +
                "Длительность: " + customerService.getDuration() + "\n" +
                "Вы хотите заказать эту услугу?");
        message.setReplyMarkup(getTimeslotsAfterServices(customerService));
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
            haircutMen.setCallbackData(CallbackTypes.SERVICE + "%" + service.getName());

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

    private void choiceTimeslot(long chatId, long messageId, LocalDate date, CustomerService customerService) {
        String formattedDay = date.format(DateTimeFormatter.ofPattern("dd MMMM yyyy"));
        String text = "Отлично! Вы выбрали дату " + formattedDay +
                ". \nА давайте теперь выберем свободное для вас время.";
        EditMessageText message = new EditMessageText();
        message.setChatId(String.valueOf(chatId));
        message.setMessageId((int) messageId);
        message.setText(text);
        message.setReplyMarkup(getTimeslots(date, customerService));

        try {
            execute(message);
        } catch (TelegramApiException e) {
            System.out.println(e.getMessage());
        }
    }

    private InlineKeyboardMarkup getTimeslots(LocalDate date, CustomerService service) {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();
        List<Timeslot> timeslots = appointmentService.getAvailableTimeslots(date, service);
        List<InlineKeyboardButton> currentRow = new ArrayList<>();
        for (Timeslot timeslot : timeslots) {
            InlineKeyboardButton timeslotBtn = new InlineKeyboardButton();
            timeslotBtn.setText(timeslot.getStartTime() + " - " + timeslot.getEndTime());
            timeslotBtn.setCallbackData(CallbackTypes.MAKE_APPOINTMENT + "%" + timeslot.getStartTime());

            currentRow.add(timeslotBtn);
            if (currentRow.size() == 2) {
                rowsInLine.add(currentRow);
                currentRow = new ArrayList<>();
            }
        }

        if (!currentRow.isEmpty()) {
            rowsInLine.add(currentRow);
        }

        markup.setKeyboard(rowsInLine);
        return markup;
    }

    private void choiceDayForBooking(long chatId, long messageId, CustomerService service) {
        String text = "Вы выбрали услугу " + service.getName() +
                ". \nА давайте теперь выберем свободное для вас время.";
        EditMessageText editMessage = new EditMessageText();
        editMessage.setChatId(String.valueOf(chatId));
        editMessage.setText(text);
        editMessage.setMessageId((int) messageId);
        editMessage.setReplyMarkup(getDaysFoorBooking());

        try {
            execute(editMessage);
        } catch (TelegramApiException e) {
            System.out.println(e.getMessage());
        }
    }

    private InlineKeyboardMarkup getDaysFoorBooking() {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<LocalDate> days = scheduleService.getDays();
        // TODO вывести дни
        List<InlineKeyboardButton> currentRow = new ArrayList<>();

        for (LocalDate day : days) {
            InlineKeyboardButton button = new InlineKeyboardButton();
            String formattedDay = day.format(DateTimeFormatter.ofPattern("dd MMMM yyyy"));
            button.setText(formattedDay);
            button.setCallbackData(CallbackTypes.CHOOSE_TIMESLOT + "%" + day);
            currentRow.add(button);

            if (currentRow.size() == 2) {
                rowsInline.add(currentRow);
                currentRow = new ArrayList<>();
            }
        }

        if (!currentRow.isEmpty()) {
            rowsInline.add(currentRow);
        }

        markup.setKeyboard(rowsInline);

        return markup;
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
        row.add("Мои активные записи");
        row.add("Связаться с барбером");
        rows.add(row);

        row = new KeyboardRow();
        row.add("Управление бронированием");
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
            buttonHaircutService.setCallbackData(CallbackTypes.SERVICE_GROUP + "%" + serviceGroup.getName());
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


    private InlineKeyboardMarkup getTimeslotsAfterServices(CustomerService customerService) {
        InlineKeyboardMarkup markupIn = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();

        rowsInline.add(
                createAnswerKeyboard(
                        CallbackTypes.YES_FOR_CREATING_APPOINTMENT + "%" + customerService.getName(),
                        CallbackTypes.CANCEL_ACTION + "%" + customerService.getName()
                )

        );
        markupIn.setKeyboard(rowsInline);
        return markupIn;
    }

    private void confirmAppointment(long chatId, long messageId, Timeslot timeslot) {
        String text = "Вы выбрали время " + timeslot.getStartTime() +
                " - " + timeslot.getEndTime() +
                ". \nБронировать на это время?.";
        EditMessageText editMessage = new EditMessageText();
        editMessage.setChatId(String.valueOf(chatId));
        editMessage.setText(text);
        editMessage.setMessageId((int) messageId);
        editMessage.setReplyMarkup(keyboardForConfirmAppointment(timeslot));

        try {
            execute(editMessage);
        } catch (TelegramApiException e) {
            System.out.println(e.getMessage());
        }
    }

    private void confirmAppointment(long chatId, long messageId, List<Timeslot> timeslot) {
        String text = "Вы выбрали время " + timeslot.get(0).getStartTime() +
                " - " + timeslot.get(0).getEndTime().plusHours(1) +
                ". \nБронировать на это время?.";
        EditMessageText editMessage = new EditMessageText();
        editMessage.setChatId(String.valueOf(chatId));
        editMessage.setText(text);
        editMessage.setMessageId((int) messageId);
        editMessage.setReplyMarkup(keyboardForConfirmAppointment(timeslot.get(0)));

        try {
            execute(editMessage);
        } catch (TelegramApiException e) {
            System.out.println(e.getMessage());
        }
    }

    private InlineKeyboardMarkup keyboardForConfirmAppointment(Timeslot timeslot) {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();

        rowsInline.add(createAnswerKeyboard(
                CallbackTypes.APPOINTMENT_CREATED + "%" + timeslot.getStartTime(),
                CallbackTypes.CANCEL_ACTION + "%" + timeslot.getEndTime()
        ));

        markup.setKeyboard(rowsInline);
        return markup;
    }

    private void showAllActiveAppointments(long chatId, long messageId,
                                           List<Appointment> clientAppointments) {
        EditMessageText messageText = new EditMessageText();
        messageText.setChatId(String.valueOf(chatId));
        messageText.setMessageId((int) messageId);

        List<Timeslot> activeTimeslots = timeslotService.getAllActiveTimeslotByChatId(chatId);

        for (int i = 0; i < clientAppointments.size(); i++) {
            Appointment appointment = clientAppointments.get(i);

            for (int j = 0; j < activeTimeslots.size(); j++) {
                Timeslot timeslot = activeTimeslots.get(j);
                String[] randomUUID = UUID.randomUUID().toString().split("-");
                String random = randomUUID[0];

                LocalTime endTime = timeslot.getStartTime().plusMinutes(appointment.getService().getDuration());
                String timeForBooking;
                if (appointment.getService().getDuration() > 60) {
                    timeForBooking = timeslot.getStartTime() + " - " + endTime.plusMinutes(60);
                } else {
                    timeForBooking = timeslot.getStartTime() + " - " + endTime;
                }

                String messageToClient = "Запись №: " + random + " создана.\n"
                        + "Клиент: " + appointment.getClient().getName() + ".\n"
                        + "Услуга: " + appointment.getService().getName() + ".\n"
                        + "Дата: " + appointment.getDateOfBooking().format(DateTimeFormatter.ofPattern("dd MMMM yyyy")) + ".\n"
                        + "Время: " + timeForBooking + ".\n"
                        + "До скорой встречи " + appointment.getClient().getName() + "!";
                messageText.setText(messageToClient);
            }
        }


        try {
            execute(messageText);
        } catch (TelegramApiException e) {
            sendMessage(chatId, "Произошла ошибка пожалуйста попробуйте заново", true);
        }

    }

    private void sendAllActiveAppointments(long chatId, List<Appointment> appointments) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        List<Timeslot> timeslotsByChatId = timeslotService.getAllActiveTimeslotByChatId(chatId);
        if (appointments.size() == timeslotsByChatId.size()) {
            for (int i = 0; i < appointments.size(); i++) {
                Appointment appointment = appointments.get(i);

                for (int j = 0; j < timeslotsByChatId.size(); j++) {
                    Timeslot timeslot = timeslotsByChatId.get(j);
                    String[] randomUUID = UUID.randomUUID().toString().split("-");
                    String random = randomUUID[0];

                    LocalTime endTime = timeslot.getStartTime().plusMinutes(appointment.getService().getDuration());
                    String timeForBooking;
                    if (appointment.getService().getDuration() > 60) {
                        timeForBooking = timeslot.getStartTime() + " - " + endTime.plusMinutes(60);
                    } else {
                        timeForBooking = timeslot.getStartTime() + " - " + endTime;
                    }

                    String messageToClient = "Запись №: " + random + " создана.\n"
                            + "Клиент: " + appointment.getClient().getName() + ".\n"
                            + "Услуга: " + appointment.getService().getName() + ".\n"
                            + "Дата: " + appointment.getDateOfBooking().format(DateTimeFormatter.ofPattern("dd MMMM yyyy")) + ".\n"
                            + "Время: " + timeForBooking + ".\n"
                            + "До скорой встречи " + appointment.getClient().getName() + "!";
                    message.setText(messageToClient);
                }
            }
        } else {
            for (int i = 0; i < appointments.size(); i++) {
                Appointment appointment = appointments.get(i);
                String[] randomUUID = UUID.randomUUID().toString().split("-");
                String random = randomUUID[0];

                String messageToClient = "Запись №: " + random + " создана.\n"
                        + "Клиент: " + appointment.getClient().getName() + ".\n"
                        + "Услуга: " + appointment.getService().getName() + ".\n"
                        + "Дата: " + appointment.getDateOfBooking().format(DateTimeFormatter.ofPattern("dd MMMM yyyy")) + ".\n"
                        + "Время: Уточните пожалуйста по телефону" + ".\n"
                        + "До скорой встречи " + appointment.getClient().getName() + "!";
                message.setText(messageToClient);
            }
        }


        try {
            execute(message);
        } catch (TelegramApiException e) {
            System.out.println(e.getMessage());
        }
    }

    private void appointmentManagement(long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText("Ваши активные записи на данный момент");
        message.setReplyMarkup(appointmentManagementKeyboard(chatId));

        try {
            execute(message);
        } catch (TelegramApiException e) {
            System.out.println(e.getMessage());
        }
    }


    private InlineKeyboardMarkup appointmentManagementKeyboard(long chatId) {
        List<AppointmentTimeslot> activeAppointments = appointmentTimeslotService.getAllActiveBookings(chatId);
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();
        List<InlineKeyboardButton> buttons = new ArrayList<>();
        InlineKeyboardButton button = new InlineKeyboardButton();
        for (int i = 0; i < activeAppointments.size(); i++) {
            String text = activeAppointments.get(i).getAppointment().getDateOfBooking().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
                    + "\n" + activeAppointments.get(i).getTimeslot().getStartTime() + "-" + activeAppointments.get(i).getTimeslot().getEndTime();
            button.setText(text);
            button.setCallbackData(CallbackTypes.VIEW_APPOINTMENT + "%" + activeAppointments.get(i).getId());
            buttons.add(button);
        }
        rowsInLine.add(buttons);
        markup.setKeyboard(rowsInLine);
        return markup;
    }

    private void showPortfolio(long chatId) throws IOException {
        List<byte[]> portfolioList = portfolioService.findAll();
        for (byte[] data : portfolioList) {
            File tempFile = File.createTempFile("abc", ".jpg");
            try (FileOutputStream fos = new FileOutputStream(tempFile)) {
                fos.write(data);
                SendPhoto sendPhoto = new SendPhoto(String.valueOf(chatId), new InputFile(tempFile));
                execute(sendPhoto);
            } catch (TelegramApiException e) {
                System.out.println(e.getMessage());
            }

        }

    }

    private List<InlineKeyboardButton> createAnswerKeyboard(String callback1, String callback2) {
        InlineKeyboardButton yesBtn = new InlineKeyboardButton();
        yesBtn.setText("Да");
        yesBtn.setCallbackData(callback1);

        InlineKeyboardButton noBtn = new InlineKeyboardButton();
        noBtn.setText("Нет");
        noBtn.setCallbackData(callback2);

        return List.of(yesBtn, noBtn);
    }

    private void editAppointment(Long id, long chatId) {
        AppointmentTimeslot appointmentTimeslot = appointmentTimeslotService.getAllById(id);
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chatId));
        String text = "Клиент: " + appointmentTimeslot.getAppointment().getClient().getName() + ".\n"
                + "Услуга: " + appointmentTimeslot.getAppointment().getService().getName() + ".\n"
                + "Дата: " + appointmentTimeslot.getAppointment().getDateOfBooking().format(DateTimeFormatter.ofPattern("dd MMMM yyyy")) + ".\n"
                + "Время: " + appointmentTimeslot.getTimeslot().getStartTime() + "-" + appointmentTimeslot.getTimeslot().getEndTime() + ".\n"
                + "До скорой встречи " + appointmentTimeslot.getAppointment().getClient().getName() + "!";
        sendMessage.setText(text);
        sendMessage.setReplyMarkup(deleteAppointment(appointmentTimeslot.getId()));
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            sendMessage(chatId, e.getMessage(), true);
        }

    }

    private InlineKeyboardMarkup deleteAppointment(Long id) {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        InlineKeyboardButton deleteBtn = new InlineKeyboardButton();
        deleteBtn.setText("Удалить");
        deleteBtn.setCallbackData(CallbackTypes.DELETED_APPOINTMENT + "%" + id);

        rows.add(List.of(
                deleteBtn
        ));

        markup.setKeyboard(rows);
        return markup;
    }

    private void appointmentDeleteSuccessfull(long chatId, Long id){
        SendMessage message = new SendMessage();
        AppointmentTimeslot appointmentTimeslotToDelete = appointmentTimeslotService.getAllById(id);
        String text = "Ваша запись успешно удалена!\n" +
                "Клиент: " + appointmentTimeslotToDelete.getAppointment().getClient().getName() + ".\n"
                + "Услуга: " + appointmentTimeslotToDelete.getAppointment().getService().getName() + ".\n"
                + "Дата: " + appointmentTimeslotToDelete.getAppointment().getDateOfBooking().format(DateTimeFormatter.ofPattern("dd MMMM yyyy")) + ".\n"
                + "Время: " + appointmentTimeslotToDelete.getTimeslot().getStartTime() + "-" + appointmentTimeslotToDelete.getTimeslot().getEndTime() + ".\n";
        message.setChatId(String.valueOf(chatId));
        message.setText(text);
        try{
            execute(message);
        } catch (TelegramApiException e){
            System.out.println(e.getMessage());
        }
    }


}