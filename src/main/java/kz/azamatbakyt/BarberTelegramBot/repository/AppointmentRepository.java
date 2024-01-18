package kz.azamatbakyt.BarberTelegramBot.repository;

import kz.azamatbakyt.BarberTelegramBot.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findAllByDateOfBooking(LocalDate date);

    @Query(
            nativeQuery = true,
            value = "select  appointment.* " +
                    "from appointment " +
                    "join client on appointment.client_id = client.id " +
                    "where client.chat_id = ?"

    )
    Appointment findByChatId(String chatId);

}
