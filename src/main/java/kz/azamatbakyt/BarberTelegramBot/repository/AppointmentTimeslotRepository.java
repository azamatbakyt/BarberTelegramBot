package kz.azamatbakyt.BarberTelegramBot.repository;

import kz.azamatbakyt.BarberTelegramBot.entity.AppointmentTimeslot;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppointmentTimeslotRepository extends JpaRepository<AppointmentTimeslot, Long> {
}
