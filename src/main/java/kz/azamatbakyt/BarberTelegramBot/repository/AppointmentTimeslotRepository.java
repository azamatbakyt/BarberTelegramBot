package kz.azamatbakyt.BarberTelegramBot.repository;

import kz.azamatbakyt.BarberTelegramBot.entity.Appointment;
import kz.azamatbakyt.BarberTelegramBot.entity.AppointmentTimeslot;
import kz.azamatbakyt.BarberTelegramBot.helpers.Status;
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

    @Query(nativeQuery = true,
    value = "select appointment_timeslot.* from appointment_timeslot " +
            "left join appointment on appointment_timeslot.appointment_id = appointment.id " +
            "where public.appointment.status=?")
    List<AppointmentTimeslot> findAllSuccessfulAppointments(String status);

    @Query(nativeQuery = true,
    value = "select appointment_timeslot.* from appointment_timeslot " +
            "where appointment_timeslot.appointment_id=?")
    AppointmentTimeslot findByAppointmentId(Long id);

    @Query(nativeQuery = true,
    value = "select appointment_timeslot.* from appointment_timeslot " +
            "left join appointment on appointment_timeslot.appointment_id = appointment.id " +
            "left join client on client.id = appointment.client_id " +
            "where client.chat_id=? and appointment.status=?")
    List<AppointmentTimeslot> findAllByStatus(Long id, String status);
}

