package kz.azamatbakyt.BarberTelegramBot.repository;

import kz.azamatbakyt.BarberTelegramBot.entity.AppointmentTimeslot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AppointmentTimeslotRepository extends JpaRepository<AppointmentTimeslot, Long> {
    List<AppointmentTimeslot> findAllByAppointmentIdIn(List<Long> appointmentIds);
    AppointmentTimeslot findAppointmentTimeslotByAppointmentId(Long id);
    List<AppointmentTimeslot> findAllByAppointmentId(Long id);

}

