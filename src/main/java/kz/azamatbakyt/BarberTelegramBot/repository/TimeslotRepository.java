package kz.azamatbakyt.BarberTelegramBot.repository;

import kz.azamatbakyt.BarberTelegramBot.entity.Timeslot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalTime;
import java.util.List;

public interface TimeslotRepository extends JpaRepository<Timeslot, Long> {
    Timeslot findTimeslotByStartTime(LocalTime time);

    List<Timeslot> findAllByStartTimeIn(List<LocalTime> times);

    @Query(nativeQuery = true,
            value = "select timeslot.* from timeslot " +
                    "join appointment_timeslot on timeslot.id = appointment_timeslot.timeslot_id " +
                    "join appointment on appointment_timeslot.appointment_id = appointment.id " +
                    "join client on appointment.client_id = client.id " +
                    "where appointment.status='BOOKING_SUCCESSFUL' and chat_id = ?")
    List<Timeslot> findAllActiveTimeslots(Long chatId);
}
