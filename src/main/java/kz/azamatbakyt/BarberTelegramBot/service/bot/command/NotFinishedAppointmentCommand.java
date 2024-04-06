package kz.azamatbakyt.BarberTelegramBot.service.bot.command;

import kz.azamatbakyt.BarberTelegramBot.entity.Appointment;
import kz.azamatbakyt.BarberTelegramBot.helpers.Status;
import kz.azamatbakyt.BarberTelegramBot.service.AppointmentService;
import kz.azamatbakyt.BarberTelegramBot.service.bot.JsonHandler;
import kz.azamatbakyt.BarberTelegramBot.service.bot.TelegramUtils;
import kz.azamatbakyt.BarberTelegramBot.service.bot.callback.CallbackType;
import kz.azamatbakyt.BarberTelegramBot.service.bot.model.Message;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

@Component
public class NotFinishedAppointmentCommand implements Command{

    private final static String NOT_FINISHED_TEXT = "Ниже представлены ваши незавершенные бронирования, прошу вас отменить их нажав на запись и начать новую сессию бронирования: ";
    private final static String NOT_FOUND_TEXT = "У вас нету незавершенных бронирований";

    private final AppointmentService appointmentService;

    public NotFinishedAppointmentCommand(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }


    @Override
    public List<Message> apply(Update update) {
        String chatId = TelegramUtils.getStringChatId(update);
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        List<Appointment> appointments = appointmentService.getNotFinishedAppointments(
                chatId,
                Status.SERVICE_SELECTED,
                Status.DATE_SELECTED,
                Status.TIMESLOT_SELECTED
        );
        if (!appointments.isEmpty()){
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(chatId);
            sendMessage.setText(NOT_FINISHED_TEXT);
            // TODO finish this
            sendMessage.setReplyMarkup(notFinishedAppointments(appointments));
            return build(sendMessage);
        }
        message.setText(NOT_FOUND_TEXT);
        return build(message);
    }


    private InlineKeyboardMarkup notFinishedAppointments(List<Appointment> appointments){
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();
        for (Appointment appointment : appointments) {
            InlineKeyboardButton button = new InlineKeyboardButton();
            button.setText(appointment.getService().getName() != null ? appointment.getService().getName() : "Услуга не выбрана");
            String callback = JsonHandler.toJson(List.of(CallbackType.DELETE_NOT_FINISHED_APPOINTMENT, appointment.getId().toString()));
            button.setCallbackData(callback);
            rowsInLine.add(List.of(button));
        }


        markup.setKeyboard(rowsInLine);
        return markup;
    }
}
