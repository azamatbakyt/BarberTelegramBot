package kz.azamatbakyt.BarberTelegramBot.repository;

import kz.azamatbakyt.BarberTelegramBot.entity.CustomSchedules;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomSchedulesRepository extends JpaRepository<CustomSchedules, Long> {
}
