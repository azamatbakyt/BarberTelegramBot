package kz.azamatbakyt.BarberTelegramBot.repository;

import kz.azamatbakyt.BarberTelegramBot.entity.CustomSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomScheduleRepository extends JpaRepository<CustomSchedule, Long> {
}
