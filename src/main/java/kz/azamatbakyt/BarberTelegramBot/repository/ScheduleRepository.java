package kz.azamatbakyt.BarberTelegramBot.repository;

import kz.azamatbakyt.BarberTelegramBot.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.DayOfWeek;
import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    // TODO

    @Query(
            nativeQuery = true,
            value = "select * from schedule where day_of_week = ?"
    )
    List<Schedule> findScheduleByDayOfWeek(String dayOfWeek);
}
