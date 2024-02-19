package kz.azamatbakyt.BarberTelegramBot.service.bot.callback;

import kz.azamatbakyt.BarberTelegramBot.entity.Appointment;
import kz.azamatbakyt.BarberTelegramBot.entity.AppointmentTimeslot;
import kz.azamatbakyt.BarberTelegramBot.entity.Timeslot;
import kz.azamatbakyt.BarberTelegramBot.helpers.Status;
import kz.azamatbakyt.BarberTelegramBot.service.AppointmentService;
import kz.azamatbakyt.BarberTelegramBot.service.AppointmentTimeslotService;
import kz.azamatbakyt.BarberTelegramBot.service.TimeslotService;
import kz.azamatbakyt.BarberTelegramBot.service.bot.TelegramUtils;
import kz.azamatbakyt.BarberTelegramBot.service.bot.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class AppointmentCreated implements CallbackHandler{

    private final AppointmentService appointmentService;
    private final AppointmentTimeslotService appointmentTimeslotService;

    @Autowired
    public AppointmentCreated(AppointmentService appointmentService, AppointmentTimeslotService appointmentTimeslotService) {
        this.appointmentService = appointmentService;
        this.appointmentTimeslotService = appointmentTimeslotService;
    }


    @Override
    public List<Message> apply(Callback callback, Update update) {
        String chatId = TelegramUtils.getMessageChatId(update);
        int messageId = TelegramUtils.getMessageId(update);
        EditMessageText messageText = new EditMessageText();
        messageText.setChatId(chatId);
        messageText.setMessageId(messageId);

        appointmentService.updateAppointmnetByStatus(Long.valueOf(chatId), Status.TIMESLOT_SELECTED, Status.BOOKING_SUCCESSFUL);

        List<Appointment> activeAppointments = appointmentService.getActiveAppointments(Long.valueOf(chatId));
        List<AppointmentTimeslot> appointmentTimeslots = appointmentTimeslotService.getAllByIdIn(
                activeAppointments.stream().map(Appointment::getId).collect(Collectors.toList())
        );
        StringBuilder msg = new StringBuilder();
        for (int i = 0; i < activeAppointments.size(); i++) {
            Appointment appointment = activeAppointments.get(i);

            msg.append("Ваши активные записи: ").append("\n\n")
                    .append("Имя клиента: ").append(appointment.getClient().getName()).append("\n")
                    .append("Услуга: ").append(appointment.getService().getName()).append("\n")
                    .append("Цена: ").append(appointment.getService().getPrice()).append(" тг\n")
                    .append("Длительность: ").append(appointment.getService().getDuration()).append(" мин\n")
                    .append("Дата: ").append(appointment.getDateOfBooking().format(DateTimeFormatter.ofPattern("dd MMMM yyyy"))).append(" года").append("\n")
                    .append("Время: ");

            appointmentTimeslots.stream()
                    .filter(appointmentTimeslot -> appointmentTimeslot.getAppointment().getId().equals(appointment.getId()))
                    .forEach(appointmentTimeslot ->
                            msg.append(String.format("\n%s - %s",
                                    appointmentTimeslot.getTimeslot().getStartTime(),
                                    appointmentTimeslot.getTimeslot().getEndTime()
                            ))
                    );
            msg.append("\n\n");
        }
        msg.append("До скорой встречи!");

        messageText.setText(msg.toString());


        return  build(messageText);
    }
}
