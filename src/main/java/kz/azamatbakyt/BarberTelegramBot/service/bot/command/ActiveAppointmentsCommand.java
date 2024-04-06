package kz.azamatbakyt.BarberTelegramBot.service.bot.command;

import kz.azamatbakyt.BarberTelegramBot.entity.Appointment;
import kz.azamatbakyt.BarberTelegramBot.entity.AppointmentTimeslot;
import kz.azamatbakyt.BarberTelegramBot.helpers.Status;
import kz.azamatbakyt.BarberTelegramBot.repository.AppointmentTimeslotRepository;
import kz.azamatbakyt.BarberTelegramBot.service.AppointmentService;
import kz.azamatbakyt.BarberTelegramBot.service.bot.model.Message;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Component
public class ActiveAppointmentsCommand implements Command {


    private final AppointmentService appointmentService;

    private final AppointmentTimeslotRepository appointmentTimeslotRepository;

    public ActiveAppointmentsCommand(AppointmentService appointmentService,
                                     AppointmentTimeslotRepository appointmentTimeslotRepository) {
        this.appointmentService = appointmentService;
        this.appointmentTimeslotRepository = appointmentTimeslotRepository;
    }


    @Override
    public List<Message> apply(Update update) {
        long chatId = update.getMessage().getChatId();
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        List<Appointment> appointments = appointmentService.getActiveAppointments(chatId, Status.BOOKING_SUCCESSFUL)
                .stream()
                .filter(appointment -> appointment.getDateOfBooking().isAfter(LocalDateTime.now()
                        .atZone(ZoneId.of("Asia/Almaty"))
                        .toLocalDate()
                ))
                .collect(Collectors.toList());
        List<AppointmentTimeslot> appointmentTimeslots = appointmentTimeslotRepository.findAllByAppointmentIdIn(
                appointments.stream().map(Appointment::getId).collect(Collectors.toList())
        );
        Locale locale_ru = new Locale("ru", "RU");
        StringBuilder msg = new StringBuilder();
        msg.append("Ваши активные записи: ").append("\n\n");
        for (int i = 0; i < appointments.size(); i++) {
            Appointment appointment = appointments.get(i);

            msg.append("Имя клиента: ").append(appointment.getClient().getName()).append("\n")
                    .append("Услуга: ").append(appointment.getService().getName()).append("\n")
                    .append("Цена: ").append(appointment.getService().getPrice()).append(" тг\n")
                    .append("Длительность: ").append(appointment.getService().getDuration()).append(" мин\n")
                    .append("Дата: ").append(appointment.getDateOfBooking().format(DateTimeFormatter.ofPattern("dd MMMM yyyy", locale_ru))).append(" года").append("\n")
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
        message.setText(msg.toString());
        if (appointments.isEmpty()) {
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(String.valueOf(chatId));
            sendMessage.setText("К сожалению на данный момент у вас нету активных записей");
            return build(sendMessage);
        }

        return build(message);
    }
}