package kz.azamatbakyt.BarberTelegramBot.service.bot.command;

import kz.azamatbakyt.BarberTelegramBot.entity.Appointment;
import kz.azamatbakyt.BarberTelegramBot.entity.AppointmentTimeslot;
import kz.azamatbakyt.BarberTelegramBot.helpers.Status;
import kz.azamatbakyt.BarberTelegramBot.service.AppointmentService;
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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Component
public class AppointmentManagementCommand implements Command{

    private static final String MANAGEMENT = "Ниже представлены ваши активные записи: ";
    private final AppointmentService appointmentService;
    private final AppointmentTimeslotService appointmentTimeslotService;

    public AppointmentManagementCommand(AppointmentService appointmentService, AppointmentTimeslotService appointmentTimeslotService) {
        this.appointmentService = appointmentService;
        this.appointmentTimeslotService = appointmentTimeslotService;
    }

    @Override
    public List<Message> apply(Update update) {
        String chatId = TelegramUtils.getStringChatId(update);
        List<Appointment> appointments = appointmentService.getActiveAppointments(Long.valueOf(chatId), Status.BOOKING_SUCCESSFUL)
                .stream()
                .filter(appointment -> appointment.getDateOfBooking().isAfter(LocalDateTime.now()
                        .atZone(ZoneId.of("Asia/Almaty"))
                        .toLocalDate()))
                .collect(Collectors.toList());
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        if (!appointments.isEmpty()) {
            sendMessage.setText(MANAGEMENT);
            sendMessage.setReplyMarkup(appointmentList(appointments));
        }

        sendMessage.setText("К сожалению на данный момент у вас нету активных записей");
        return build(sendMessage);
    }

    private InlineKeyboardMarkup appointmentList(List<Appointment> appointments) {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();

        for (Appointment appointment : appointments) {
            Locale locale_ru = new Locale("ru", "RU");
            InlineKeyboardButton btn = new InlineKeyboardButton();
            btn.setText(String.valueOf(appointment.getDateOfBooking().format(DateTimeFormatter.ofPattern("dd MMMM yyyy", locale_ru))));
            String callback = JsonHandler.toJson(List.of(CallbackType.APPOINTMENT_ID, String.valueOf(appointment.getId())));
            btn.setCallbackData(callback);


            List<InlineKeyboardButton> buttons = new ArrayList<>();
            buttons.add(btn);
            rowsInLine.add(buttons); 
        }

        markup.setKeyboard(rowsInLine);
        return markup;
    }

}
