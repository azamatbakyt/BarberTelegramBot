package kz.azamatbakyt.BarberTelegramBot;

import kz.azamatbakyt.BarberTelegramBot.service.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BarberTelegramBotApplication {

	@Autowired
	private AppointmentService appointmentService;

	public static void main(String[] args) {
		SpringApplication.run(BarberTelegramBotApplication.class, args);
	}

//	@Override
//	public void run(String... args) throws Exception {
//		appointmentService.getAvailableTimeslots(
//				LocalDate.of(2023, 12, 10),
//				null
//		).forEach(System.out::println);
//
//		System.out.println("======");
//		System.out.println("======");
//		System.out.println("======");
//
//		appointmentService.getBookedTimeslotsOnDate(
//				LocalDate.of(2023, 12, 10)
//		).forEach(System.out::println);
//	}
}