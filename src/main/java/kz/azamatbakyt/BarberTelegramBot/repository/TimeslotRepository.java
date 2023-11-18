package kz.azamatbakyt.BarberTelegramBot.repository;

import kz.azamatbakyt.BarberTelegramBot.entity.Timeslot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalTime;

public interface TimeslotRepository extends JpaRepository<Timeslot, Long> {
    public Timeslot findTimeslotByStartTime(LocalTime time);
}
