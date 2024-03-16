package kz.azamatbakyt.BarberTelegramBot.repository;

import kz.azamatbakyt.BarberTelegramBot.entity.CustomSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface CustomScheduleRepository extends JpaRepository<CustomSchedule, Long> {

    List<CustomSchedule> findCustomScheduleByCustomDate(LocalDate date);

}
