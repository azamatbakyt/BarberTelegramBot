package kz.azamatbakyt.BarberTelegramBot.service.bot.callback;

import kz.azamatbakyt.BarberTelegramBot.entity.Appointment;
import kz.azamatbakyt.BarberTelegramBot.entity.AppointmentTimeslot;
import kz.azamatbakyt.BarberTelegramBot.entity.CustomerService;
import kz.azamatbakyt.BarberTelegramBot.entity.Timeslot;
import kz.azamatbakyt.BarberTelegramBot.helpers.Status;
import kz.azamatbakyt.BarberTelegramBot.service.AppointmentService;
import kz.azamatbakyt.BarberTelegramBot.service.AppointmentTimeslotService;
import kz.azamatbakyt.BarberTelegramBot.service.TimeslotService;
import kz.azamatbakyt.BarberTelegramBot.service.bot.JsonHandler;
import kz.azamatbakyt.BarberTelegramBot.service.bot.TelegramUtils;
import kz.azamatbakyt.BarberTelegramBot.service.bot.command.CommandType;
import kz.azamatbakyt.BarberTelegramBot.service.bot.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Component
public class TimeslotCallback implements CallbackHandler {

    private final static String empty = "К сожалению на эту дату свободных таймслотов нету!";

    private final static String text = "Отлично! Вы выбрали дату %s" +
            ". \nА давайте теперь выберем свободное для вас время.";

    private final TimeslotService timeslotService;
    private final AppointmentService appointmentService;
    private final AppointmentTimeslotService appointmentTimeslotService;

    @Autowired
    public TimeslotCallback(TimeslotService timeslotService, AppointmentService appointmentService, AppointmentTimeslotService appointmentTimeslotService) {
        this.timeslotService = timeslotService;
        this.appointmentService = appointmentService;
        this.appointmentTimeslotService = appointmentTimeslotService;
    }


    @Override
    public List<Message> apply(Callback callback, Update update) {
        LocalDate date = LocalDate.parse(callback.getData());
        String chatId = TelegramUtils.getMessageChatId(update);
        int messageId = TelegramUtils.getMessageId(update);
        Appointment appointment1 = appointmentService.getNotCreatedAppointmentByChatId(Long.valueOf(chatId), Status.SERVICE_SELECTED);
        List<Timeslot> timeslots = appointmentService.getAvailableTimeslots(date, appointment1.getService());
        if (timeslots.isEmpty()){
            SendMessage message = new SendMessage();
            message.setChatId(chatId);
            message.setText(empty);
            message.setReplyMarkup(restart());
            return sendMessage(message);
        }
        Locale locale_ru = new Locale("ru", "RU");
        String formattedDay = date.format(DateTimeFormatter.ofPattern("dd MMMM yyyy", locale_ru));
        Appointment appointment = appointmentService.updateDateOfBookingByChatId(
                Long.valueOf(chatId),
                date,
                Status.DATE_SELECTED
        );
        EditMessageText messageText = new EditMessageText();
        messageText.setChatId(chatId);
        messageText.setMessageId(messageId);
        messageText.setText(String.format(text, formattedDay));
        messageText.setReplyMarkup(getTimeslots(date, appointment.getService()));

        appointmentTimeslotService.save(
                List.of(
                        new AppointmentTimeslot(appointment, null)
                )
        );

        return build(messageText);
    }

    // TODO  Перенести стрим в сервис
    private InlineKeyboardMarkup getTimeslots(LocalDate date, CustomerService service) {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();
        List<Timeslot> timeslots = appointmentService.getAvailableTimeslots(date, service);
        List<InlineKeyboardButton> currentRow = new ArrayList<>();
        for (Timeslot timeslot : timeslots) {
            InlineKeyboardButton timeslotBtn = new InlineKeyboardButton();
            timeslotBtn.setText(timeslot.getStartTime() + " - " + timeslot.getEndTime());
            String callback = JsonHandler.toJson(List.of(CallbackType.TIMESLOTS, timeslot.getStartTime().toString()));
            timeslotBtn.setCallbackData(callback);

            currentRow.add(timeslotBtn);
            if (currentRow.size() == 2) {
                rowsInLine.add(currentRow);
                currentRow = new ArrayList<>();
            }
        }

        if (!currentRow.isEmpty()) {
            rowsInLine.add(currentRow);
        }
        InlineKeyboardButton cancelBtn = new InlineKeyboardButton();
        cancelBtn.setText("Отменить запись");
        String cancelCallback = JsonHandler.toJson(List.of(CallbackType.TIMESLOT_CANCELED, timeslots.get(0).getStartTime().toString()));
        cancelBtn.setCallbackData(cancelCallback);


        List<InlineKeyboardButton> cancelBtnRow = new ArrayList<>();
        cancelBtnRow.add(cancelBtn);
        rowsInLine.add(cancelBtnRow);

        markup.setKeyboard(rowsInLine);
        return markup;
    }

    private InlineKeyboardMarkup restart(){
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        InlineKeyboardButton restart = new InlineKeyboardButton();
        String callback = JsonHandler.toJson(
                List.of(
                        CallbackType.EMPTY_TIMESLOTS, "/start"
                )
        );
        restart.setText("Начать заново");
        restart.setCallbackData(callback);
        rows.add(
                List.of(restart)
        );
        markup.setKeyboard(rows);
        return markup;
    }
}
