package kz.azamatbakyt.BarberTelegramBot.repository;

import kz.azamatbakyt.BarberTelegramBot.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.DayOfWeek;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    Schedule findScheduleByDayOfWeek(DayOfWeek dayOfWeek);
}
