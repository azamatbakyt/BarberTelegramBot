package kz.azamatbakyt.BarberTelegramBot.service.bot.callback;

import kz.azamatbakyt.BarberTelegramBot.entity.AppointmentTimeslot;
import kz.azamatbakyt.BarberTelegramBot.service.AppointmentService;
import kz.azamatbakyt.BarberTelegramBot.service.AppointmentTimeslotService;
import kz.azamatbakyt.BarberTelegramBot.service.bot.JsonHandler;
import kz.azamatbakyt.BarberTelegramBot.service.bot.TelegramUtils;
import kz.azamatbakyt.BarberTelegramBot.service.bot.model.Message;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Component
public class AppointmentManagementCallback implements CallbackHandler{

    private final AppointmentService appointmentService;
    private final AppointmentTimeslotService appointmentTimeslotService;
    public AppointmentManagementCallback(AppointmentService appointmentService, AppointmentTimeslotService appointmentTimeslotService) {
        this.appointmentService = appointmentService;
        this.appointmentTimeslotService = appointmentTimeslotService;
    }

    @Override
    public List<Message> apply(Callback callback, Update update) {
        EditMessageText messageText = new EditMessageText();
        Locale locale_ru = new Locale("ru", "RU");
        List<AppointmentTimeslot> appointments = appointmentTimeslotService.getListAT(Long.valueOf(callback.getData()));
        String chatId = TelegramUtils.getMessageChatId(update);
        int messageId = TelegramUtils.getMessageId(update);
        StringBuilder builder = new StringBuilder();
        if (appointments.size() == 2){
            builder.append("Ниже представлена информация о записи:").append("\n")
                    .append("Услуга: ").append(appointments.get(0).getAppointment().getService().getName()).append("\n")
                    .append("Дата: ").append(appointments.get(0).getAppointment().getDateOfBooking().format(DateTimeFormatter.ofPattern("dd MMMM yyyy", locale_ru))).append("\n")
                    .append("Длительность: ").append(appointments.get(0).getAppointment().getService().getDuration()).append(" мин").append("\n")
                    .append("Время: ").append("\n").append(appointments.get(0).getTimeslot().getStartTime()).append(" - ").append(appointments.get(0).getTimeslot().getEndTime()).append("\n")
                    .append(appointments.get(1).getTimeslot().getStartTime()).append(" - ").append(appointments.get(1).getTimeslot().getEndTime()).append("\n")
                    .append("Цена: ").append(appointments.get(0).getAppointment().getService().getPrice()).append("тг").append("\n").append("\n")
                    .append("Ваши действия:");

            messageText.setMessageId(messageId);
            messageText.setChatId(chatId);
            messageText.setText(builder.toString());
            messageText.setReplyMarkup(appointmentToDelete(Long.valueOf(callback.getData())));
            return build(messageText);
        }
        builder.append("Ниже представлена информация о записи:").append("\n")
                .append("Услуга: ").append(appointments.get(0).getAppointment().getService().getName()).append("\n")
                .append("Дата: ").append(appointments.get(0).getAppointment().getDateOfBooking().format(DateTimeFormatter.ofPattern("dd MMMM yyyy", locale_ru))).append(" года").append("\n")
                .append("Длительность: ").append(appointments.get(0).getAppointment().getService().getDuration()).append(" мин").append("\n")
                .append("Время: ").append(appointments.get(0).getTimeslot().getStartTime()).append(" - ").append(appointments.get(0).getTimeslot().getEndTime()).append("\n")
                .append("Цена: ").append(appointments.get(0).getAppointment().getService().getPrice()).append("тг").append("\n").append("\n")
                .append("Ваши действия:");

        messageText.setChatId(chatId);
        messageText.setMessageId(messageId);
        messageText.setText(builder.toString());
        messageText.setReplyMarkup(appointmentToDelete(Long.valueOf(callback.getData())));
        return build(messageText);
    }

    private InlineKeyboardMarkup appointmentToDelete(Long id){
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        String callback = JsonHandler.toJson(List.of(CallbackType.ACTIVE_APPOINTMENT_DELETED, String.valueOf(id)));
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText("Отменить запись");
        button.setCallbackData(callback);
        rows.add(List.of(button));

        markup.setKeyboard(rows);
        return markup;
    }

}
