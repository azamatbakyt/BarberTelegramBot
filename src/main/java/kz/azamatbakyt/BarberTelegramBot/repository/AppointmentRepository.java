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
            value = "select appointment.* " +
                    "from appointment " +
                    "join client on appointment.client_id = client.id " +
                    "where client.chat_id = ? and appointment.is_created=false"
    )
    Appointment findByChatId(Long chatId);

    @Query(nativeQuery = true,
            value ="select appointment.* " +
                    "from appointment " +
                    "join client on client.id=appointment.client_id " +
                    "where client.chat_id=? and appointment.date_of_booking is null")
    Appointment findAllByChatId(Long chatId);

    @Query(
            nativeQuery = true,
            value = "select appointment.* " +
                    "from appointment " +
                    "join client on appointment.client_id=client.id " +
                    "where client.chat_id=? and appointment.is_created=true"

    )
    List<Appointment> findAllByCreatedAppointment(Long chatId);



}
