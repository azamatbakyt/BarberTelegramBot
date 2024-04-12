package kz.azamatbakyt.BarberTelegramBot;

import kz.azamatbakyt.BarberTelegramBot.entity.AppointmentTimeslot;
import kz.azamatbakyt.BarberTelegramBot.helpers.Status;
import kz.azamatbakyt.BarberTelegramBot.repository.AppointmentTimeslotRepository;
import kz.azamatbakyt.BarberTelegramBot.service.AppointmentTimeslotService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;


class BarberTelegramBotApplicationTests {

	private final AppointmentTimeslotService appointmentTimeslotService;

    BarberTelegramBotApplicationTests(AppointmentTimeslotService appointmentTimeslotService) {
        this.appointmentTimeslotService = appointmentTimeslotService;
    }

    @Test
	void contextLoads() {
		PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		System.out.println(passwordEncoder.encode("1234"));
	}


	@Test
	void getAll(){

		List<AppointmentTimeslot> mockAppointments = new ArrayList<>();
		// Добавляем несколько тестовых значений в список
//		mockAppointments.add(new AppointmentTimeslot(...)); // Создайте объект AppointmentTimeslot здесь с нужными параметрами
//		mockAppointments.add(new AppointmentTimeslot(...));

		// Мокируем вызов repository.findAllSuccessfulAppointments()
		AppointmentTimeslotRepository appointmentTimeslotRepository;
//		when(appointmentTimeslotRepository.findAllSuccessfulAppointments(anyString())).thenReturn(mockAppointments);

		// Вызываем метод вашего сервиса, который должен вызывать метод repository
		List<AppointmentTimeslot> appointments = appointmentTimeslotService.getAllSuccessfulAppointments(Status.BOOKING_SUCCESSFUL);

		// Проверяем, что возвращенный список не пустой и содержит ожидаемое количество элементов
		Assertions.assertNotNull(appointments);
		Assertions.assertEquals(mockAppointments.size(), appointments.size());

		// Проверяем, что возвращенные значения совпадают с теми, которые мы подготовили для мокирования
		for (int i = 0; i < mockAppointments.size(); i++) {
			Assertions.assertEquals(mockAppointments.get(i), appointments.get(i));
		}

		// Проверяем, что метод repository был вызван ровно один раз с нужным аргументом
//		verify(appointmentTimeslotRepository, times(1)).findAllSuccessfulAppointments(anyString());
	}

}
