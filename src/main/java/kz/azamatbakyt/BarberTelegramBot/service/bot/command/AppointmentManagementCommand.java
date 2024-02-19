package kz.azamatbakyt.BarberTelegramBot.service.bot.command;

import kz.azamatbakyt.BarberTelegramBot.entity.AppointmentTimeslot;
import kz.azamatbakyt.BarberTelegramBot.helpers.ContentType;
import kz.azamatbakyt.BarberTelegramBot.service.AppointmentTimeslotService;
import kz.azamatbakyt.BarberTelegramBot.service.bot.JsonHandler;
import kz.azamatbakyt.BarberTelegramBot.service.bot.TelegramUtils;
import kz.azamatbakyt.BarberTelegramBot.service.bot.callback.CallbackType;
import kz.azamatbakyt.BarberTelegramBot.service.bot.model.Message;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component
public class AppointmentManagementCommand implements Command {

    private final AppointmentTimeslotService appointmentTimeslotService;

    public AppointmentManagementCommand(AppointmentTimeslotService appointmentTimeslotService) {
        this.appointmentTimeslotService = appointmentTimeslotService;
    }

    @Override
    public List<Message> apply(Update update) {
        SendMessage message = new SendMessage();
        Long chatId = TelegramUtils.getChatId(update);
        message.setChatId(chatId.toString());
        message.setText("Ваши активные записи на данный момент");
        message.setReplyMarkup(appointmentManagementKeyboard(chatId));

        return build(message);
    }

    private InlineKeyboardMarkup appointmentManagementKeyboard(long chatId) {
        List<AppointmentTimeslot> activeAppointments = appointmentTimeslotService.getAllActiveBookings(chatId);
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();

        for (int i = 0; i < activeAppointments.size(); i++) {
            InlineKeyboardButton button = new InlineKeyboardButton();
            String text = activeAppointments.get(i).getAppointment().getDateOfBooking().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
                    + activeAppointments.get(i).getTimeslot().getStartTime() + "-" + activeAppointments.get(i).getTimeslot().getEndTime();
            button.setText(text);
            String callback = JsonHandler.toJson(List.of(CallbackType.VIEW_APPOINTMENT, activeAppointments.get(i).getId()));
            button.setCallbackData(callback);

            List<InlineKeyboardButton> buttons = new ArrayList<>();
            buttons.add(button);
            rowsInLine.add(buttons);
        }

        markup.setKeyboard(rowsInLine);
        return markup;
    }


}
