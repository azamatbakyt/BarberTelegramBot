package kz.azamatbakyt.BarberTelegramBot.service.bot.callback;

import kz.azamatbakyt.BarberTelegramBot.entity.AppointmentTimeslot;
import kz.azamatbakyt.BarberTelegramBot.helpers.ContentType;
import kz.azamatbakyt.BarberTelegramBot.service.AppointmentTimeslotService;
import kz.azamatbakyt.BarberTelegramBot.service.bot.JsonHandler;
import kz.azamatbakyt.BarberTelegramBot.service.bot.TelegramUtils;
import kz.azamatbakyt.BarberTelegramBot.service.bot.model.Message;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component
public class ViewAppointmentCallback implements CallbackHandler {
    private final static String text = "Клиент: %s" + ".\n"
            + "Услуга: %s" + ".\n"
            + "Дата: %s" + ".\n"
            + "Время: %s" + ".\n"
            + "До скорой встречи %s" + "!";

    private final AppointmentTimeslotService appointmentTimeslotService;

    public ViewAppointmentCallback(AppointmentTimeslotService appointmentTimeslotService) {
        this.appointmentTimeslotService = appointmentTimeslotService;
    }

    @Override
    public List<Message> apply(Callback callback, Update update) {
        Long id = Long.valueOf(callback.getData());
        AppointmentTimeslot appointmentTimeslot = appointmentTimeslotService.getAllById(id);
        String chatId = TelegramUtils.getMessageChatId(update);
        int messageId = TelegramUtils.getMessageId(update);
        EditMessageText messageText = new EditMessageText();
        String date = appointmentTimeslot.getAppointment().getDateOfBooking().format(DateTimeFormatter.ofPattern("dd MMMM yyyy"));
        String time = appointmentTimeslot.getTimeslot().getStartTime() + " - " + appointmentTimeslot.getTimeslot().getEndTime();
        messageText.setText(
                String.format(
                        text,
                        appointmentTimeslot.getAppointment().getClient().getName(),
                        appointmentTimeslot.getAppointment().getService().getName(),
                        date,
                        time,
                        appointmentTimeslot.getAppointment().getClient().getName()
                )
        );
        messageText.setReplyMarkup(deleteAppointment(id));

        return build(messageText);
    }

    private InlineKeyboardMarkup deleteAppointment(Long id) {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        InlineKeyboardButton deleteBtn = new InlineKeyboardButton();
        deleteBtn.setText("Удалить");
        String callback = JsonHandler.toJson(
                List.of(
                        CallbackType.DELETED_APPOINTMENT, id
                )
        );
        deleteBtn.setCallbackData(callback);

        rows.add(List.of(
                deleteBtn
        ));

        markup.setKeyboard(rows);
        return markup;
    }
}
