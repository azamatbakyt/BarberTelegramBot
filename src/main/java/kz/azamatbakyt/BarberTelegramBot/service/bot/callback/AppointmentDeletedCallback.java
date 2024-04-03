package kz.azamatbakyt.BarberTelegramBot.service.bot.callback;

import kz.azamatbakyt.BarberTelegramBot.entity.Appointment;
import kz.azamatbakyt.BarberTelegramBot.helpers.Status;
import kz.azamatbakyt.BarberTelegramBot.service.AppointmentService;
import kz.azamatbakyt.BarberTelegramBot.service.bot.TelegramUtils;
import kz.azamatbakyt.BarberTelegramBot.service.bot.model.Message;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

@Component
public class AppointmentDeletedCallback implements CallbackHandler{

    private static final String TEXT = "Запись на дату %s года успешно отменена";

    private final AppointmentService appointmentService;

    public AppointmentDeletedCallback(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @Override
    public List<Message> apply(Callback callback, Update update) {
        Locale locale_ru = new Locale("ru", "RU");
        Appointment appointment = appointmentService.getAppointment(Long.valueOf(callback.getData()));
        String chatId = TelegramUtils.getMessageChatId(update);
        int messageId = TelegramUtils.getMessageId(update);
        String textToShow = String.format(TEXT, appointment.getDateOfBooking().format(DateTimeFormatter.ofPattern("dd MMMM yyyy", locale_ru)));
        appointmentService.delete(appointment.getId());
        EditMessageText messageText = new EditMessageText();
        messageText.setChatId(chatId);
        messageText.setMessageId(messageId);
        messageText.setText(textToShow);

        return build(messageText);
    }
}
