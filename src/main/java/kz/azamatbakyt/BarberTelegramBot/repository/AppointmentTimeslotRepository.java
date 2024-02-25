package kz.azamatbakyt.BarberTelegramBot.repository;

import kz.azamatbakyt.BarberTelegramBot.entity.AppointmentTimeslot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AppointmentTimeslotRepository extends JpaRepository<AppointmentTimeslot, Long> {
    List<AppointmentTimeslot> findAllByAppointmentIdIn(List<Long> appointmentIds);

    AppointmentTimeslot findAppointmentTimeslotByAppointmentId(Long id);

    List<AppointmentTimeslot> findAllByAppointmentId(Long id);

    @Query(nativeQuery = true,
            value = "select appointment_timeslot.* from appointment_timeslot " +
                    "join appointment on appointment_timeslot.appointment_id = appointment.id " +
                    "join timeslot on appointment_timeslot.timeslot_id = timeslot.id " +
                    "join client on appointment.client_id = client.id " +
                    "where client.chat_id=? and appointment.status='BOOKING_SUCCESSFUL'")
    List<AppointmentTimeslot> findAllByChatId(Long chatId);
    AppointmentTimeslot findAllById(Long id);
}

