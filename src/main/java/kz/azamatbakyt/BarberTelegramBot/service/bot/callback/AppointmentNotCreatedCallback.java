package kz.azamatbakyt.BarberTelegramBot.service.bot.callback;

import kz.azamatbakyt.BarberTelegramBot.helpers.Status;
import kz.azamatbakyt.BarberTelegramBot.service.AppointmentService;
import kz.azamatbakyt.BarberTelegramBot.service.bot.TelegramUtils;
import kz.azamatbakyt.BarberTelegramBot.service.bot.model.Message;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

@Component
public class AppointmentNotCreatedCallback implements CallbackHandler{

    private final static String text = "Вы только что отменили действие!\nВаша бронь получила статус: Отменено\nВы можете забронировать другую услугу нажав кнпоку Услуги!";

    private final AppointmentService appointmentService;

    public AppointmentNotCreatedCallback(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @Override
    public List<Message> apply(Callback callback, Update update) {
        String chatId = TelegramUtils.getMessageChatId(update);
        int messageId = TelegramUtils.getMessageId(update);
        EditMessageText messageText = new EditMessageText();
        messageText.setChatId(chatId);
        messageText.setMessageId(messageId);
        messageText.setText(text);
        appointmentService.deleteAppointment(
                appointmentService.getNotCreatedAppointmentByChatId(
                        Long.valueOf(chatId), Status.TIMESLOT_SELECTED
                )
        );
        return build(messageText);
    }
}
