package kz.azamatbakyt.BarberTelegramBot.repository;

import kz.azamatbakyt.BarberTelegramBot.entity.Schedule;
import kz.azamatbakyt.BarberTelegramBot.helpers.DayOfWeek;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    Schedule findScheduleByDayOfWeek(DayOfWeek dayOfWeek);
}
