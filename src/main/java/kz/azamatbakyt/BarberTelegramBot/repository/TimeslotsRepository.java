package kz.azamatbakyt.BarberTelegramBot.repository;

import kz.azamatbakyt.BarberTelegramBot.entity.Timeslots;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TimeslotsRepository extends JpaRepository<Timeslots, Long> {
}
