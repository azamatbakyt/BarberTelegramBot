package kz.azamatbakyt.BarberTelegramBot.service.bot;

import jakarta.annotation.PostConstruct;
import kz.azamatbakyt.BarberTelegramBot.config.BotConfig;
import kz.azamatbakyt.BarberTelegramBot.entity.AppointmentTimeslot;
import kz.azamatbakyt.BarberTelegramBot.entity.Client;
import kz.azamatbakyt.BarberTelegramBot.helpers.Status;
import kz.azamatbakyt.BarberTelegramBot.service.AppointmentTimeslotService;
import kz.azamatbakyt.BarberTelegramBot.service.ClientService;
import kz.azamatbakyt.BarberTelegramBot.service.bot.callback.CallbacksHandler;
import kz.azamatbakyt.BarberTelegramBot.service.bot.command.CommandsHandler;
import kz.azamatbakyt.BarberTelegramBot.service.bot.model.Message;
import kz.azamatbakyt.BarberTelegramBot.service.bot.register.ClientRegistrationHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@Component
public class TelegramBotV2 extends TelegramLongPollingBot {

    private static final Logger LOGGER = LoggerFactory.getLogger(TelegramBotV2.class);

    public final BotConfig botProperties;

    public final CommandsHandler commandsHandler;

    public final CallbacksHandler callbacksHandler;

    private final ClientService clientsService;

    private final ClientRegistrationHandler clientRegistrationHandler;
    private final AppointmentTimeslotService appointmentTimeslotService;

    public TelegramBotV2(BotConfig botProperties,
                         CommandsHandler commandsHandler,
                         CallbacksHandler callbacksHandler,
                         ClientService clientsService,
                         ClientRegistrationHandler clientRegistrationHandler, AppointmentTimeslotService appointmentTimeslotService) {
        this.botProperties = botProperties;
        this.commandsHandler = commandsHandler;
        this.callbacksHandler = callbacksHandler;
        this.clientsService = clientsService;
        this.clientRegistrationHandler = clientRegistrationHandler;
        this.appointmentTimeslotService = appointmentTimeslotService;
    }

    @Override
    public String getBotUsername() {
        return botProperties.getBotName();
    }

    @Override
    public String getBotToken() {
        return botProperties.getToken();
    }

    /**
     * /start -
     * /other -
     *
     * @param update
     */
    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            Long chatId = update.getMessage().getChatId();
            Client client = clientsService.getClientByChatId(chatId);
            SendMessage registrationMessage = clientRegistrationHandler.registerClientIfNeeded(update);

            if (registrationMessage == null) {
                send(commandsHandler.handleCommand(update));
            } else {
                sendMessage(registrationMessage);
            }
        } else if (update.hasCallbackQuery()) {
            send(callbacksHandler.handleCallbacks(update));
        }
    }

    private void send(List<Message> messages) {
        for (Message msg : messages) {
            switch (msg.getMessageType()) {
                case PHOTO:
                    sendPhoto(msg.getSendPhoto());
                case MESSAGE:
                    sendMessage(msg.getSendMessage());
                case EDIT_MESSAGE_TEXT:
                    editMessageText(msg.getEditMessageText());
            }
        }
    }


    private void sendMessage(SendMessage sendMessage) {
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            LOGGER.error("", e);
        }
    }

    private void sendPhoto(SendPhoto sendPhoto) {
        try {
            execute(sendPhoto);
        } catch (TelegramApiException e) {
            LOGGER.error("", e);
        }
    }

    private void editMessageText(EditMessageText editMessageText) {
        if (editMessageText != null) {
            try {
                execute(editMessageText);
            } catch (TelegramApiException e) {
                LOGGER.error("", e);
            }
        } else {
            LOGGER.error("EditMessageText object is null.");
        }
    }


    @PostConstruct
    private void menu() {
        List<BotCommand> menu = new ArrayList<>();
        menu.add(new BotCommand("/start", "Перезапустить бота"));
        menu.add(new BotCommand("/help", "Помощь"));
        menu.add(new BotCommand("/keyboard", "Исчезла клавиатура?"));
        try {
            execute(new SetMyCommands(menu, new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e) {
            System.out.println(e.getMessage());
        }
    }

    @Scheduled(cron = "${scheduler.cron}")
    public void reminder() {
        var appointments = appointmentTimeslotService.getAllSuccessfulAppointments(Status.BOOKING_SUCCESSFUL);
        LocalTime currentTime = LocalDateTime.now()
                .atZone(ZoneId.of("Asia/Almaty"))
                .toLocalTime();
        LocalDate currentDate = LocalDateTime.now()
                .atZone(ZoneId.of("Asia/Almaty"))
                .toLocalDate();
        for (AppointmentTimeslot appointment : appointments) {
            boolean firstCondition = appointment.getAppointment().getDateOfBooking().isEqual(currentDate);
            boolean secondCondition = currentTime.getHour() == appointment.getTimeslot().getStartTime().minusHours(2).getHour();
            if (firstCondition) {
                if (secondCondition) {
                    String textToSend = "Напоминаем что у вас сегодня в " + appointment.getTimeslot().getStartTime()
                            + " часов стоит запись на услугу " + appointment.getAppointment().getService().getName() + "!";
                    reminderSender(appointment.getAppointment().getClient().getChatId(), textToSend);
                }
            }
        }

    }

    public void reminderSender(String chatId, String textToSend) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(chatId);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            System.out.println(e.getMessage());
        }
    }
}
