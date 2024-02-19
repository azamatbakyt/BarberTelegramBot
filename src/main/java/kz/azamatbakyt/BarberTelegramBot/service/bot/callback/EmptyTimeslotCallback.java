package kz.azamatbakyt.BarberTelegramBot.service.bot.callback;

import kz.azamatbakyt.BarberTelegramBot.helpers.Status;
import kz.azamatbakyt.BarberTelegramBot.service.AppointmentService;
import kz.azamatbakyt.BarberTelegramBot.service.bot.TelegramUtils;
import kz.azamatbakyt.BarberTelegramBot.service.bot.model.Message;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

@Component
public class EmptyTimeslotCallback implements CallbackHandler {

    private final AppointmentService appointmentService;

    public EmptyTimeslotCallback(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @Override
    public List<Message> apply(Callback callback, Update update) {
        String chatId = TelegramUtils.getMessageChatId(update);
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(String.format("Пожалуйста нажмите на кнопку ниже чтобы забронировать на другую дату \n%s", callback.getData()));
        appointmentService.deleteAppointment(
                appointmentService.getNotCreatedAppointmentByChatId(
                        Long.valueOf(chatId), Status.SERVICE_SELECTED
                )
        );
        return sendMessage(message);
    }

}
