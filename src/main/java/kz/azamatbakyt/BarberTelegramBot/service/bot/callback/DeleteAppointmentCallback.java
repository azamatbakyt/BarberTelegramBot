package kz.azamatbakyt.BarberTelegramBot.service.bot.callback;

import kz.azamatbakyt.BarberTelegramBot.entity.AppointmentTimeslot;
import kz.azamatbakyt.BarberTelegramBot.service.AppointmentService;
import kz.azamatbakyt.BarberTelegramBot.service.AppointmentTimeslotService;
import kz.azamatbakyt.BarberTelegramBot.service.bot.TelegramUtils;
import kz.azamatbakyt.BarberTelegramBot.service.bot.model.Message;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
public class DeleteAppointmentCallback implements CallbackHandler{


    private final AppointmentTimeslotService appointmentTimeslotService;
    private final AppointmentService appointmentService;

    public DeleteAppointmentCallback(AppointmentTimeslotService appointmentTimeslotService, AppointmentService appointmentService) {
        this.appointmentTimeslotService = appointmentTimeslotService;
        this.appointmentService = appointmentService;
    }

    @Override
    public List<Message> apply(Callback callback, Update update) {
        Long id = Long.valueOf(callback.getData());
        String chatId = TelegramUtils.getMessageChatId(update);
        int messageId = TelegramUtils.getMessageId(update);
        AppointmentTimeslot appointmentTimeslotToDelete = appointmentTimeslotService.getAllById(id);
        String text = "Ваша запись успешно удалена!\n" +
                "Клиент: " + appointmentTimeslotToDelete.getAppointment().getClient().getName() + ".\n"
                + "Услуга: " + appointmentTimeslotToDelete.getAppointment().getService().getName() + ".\n"
                + "Дата: " + appointmentTimeslotToDelete.getAppointment().getDateOfBooking().format(DateTimeFormatter.ofPattern("dd MMMM yyyy")) + ".\n"
                + "Время: " + appointmentTimeslotToDelete.getTimeslot().getStartTime() + "-" + appointmentTimeslotToDelete.getTimeslot().getEndTime() + ".\n";
        EditMessageText messageText = new EditMessageText();
        messageText.setChatId(chatId);
        messageText.setMessageId(messageId);
        messageText.setText(text);
        appointmentService.delete(appointmentTimeslotService.getAllById(id).getAppointment().getId());
        return build(messageText);
    }
}
