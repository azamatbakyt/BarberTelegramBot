package kz.azamatbakyt.BarberTelegramBot.repository;

import kz.azamatbakyt.BarberTelegramBot.entity.Timeslot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalTime;
import java.util.List;

public interface TimeslotRepository extends JpaRepository<Timeslot, Long> {
    Timeslot findTimeslotByStartTime(LocalTime time);

    List<Timeslot> findAllByStartTimeIn(List<LocalTime> times);

}
