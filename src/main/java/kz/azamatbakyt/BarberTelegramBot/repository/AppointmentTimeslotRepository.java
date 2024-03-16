package kz.azamatbakyt.BarberTelegramBot.repository;

import kz.azamatbakyt.BarberTelegramBot.entity.AppointmentTimeslot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface AppointmentTimeslotRepository extends JpaRepository<AppointmentTimeslot, Long> {
    List<AppointmentTimeslot> findAllByAppointmentIdIn(List<Long> appointmentIds);

    AppointmentTimeslot findAppointmentTimeslotByAppointmentId(Long id);

    List<AppointmentTimeslot> findAllByAppointmentId(Long id);
    
    AppointmentTimeslot findAllById(Long id);

    @Query(nativeQuery = true,
    value = "select appointment_timeslot.* from appointment_timeslot " +
            "left join appointment on appointment_timeslot.appointment_id = appointment.id " +
            "where date_of_booking=?")
    List<AppointmentTimeslot> findAllByDateOfBooking(LocalDate date);
}

