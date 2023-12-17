package kz.azamatbakyt.BarberTelegramBot;

import kz.azamatbakyt.BarberTelegramBot.entity.CustomerService;
import kz.azamatbakyt.BarberTelegramBot.entity.Timeslot;
import kz.azamatbakyt.BarberTelegramBot.service.AppointmentService;
import kz.azamatbakyt.BarberTelegramBot.service.CSService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDate;
import java.util.List;

@SpringBootApplication
public class BarberTelegramBotApplication implements CommandLineRunner {

	@Autowired
	private AppointmentService appointmentService;
	@Autowired
	private CSService csService;

	public static void main(String[] args) {
		SpringApplication.run(BarberTelegramBotApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		CustomerService service = csService.getServiceById(6L);
		System.out.println(service.getName() + "=====" + service.getDuration());
		List<Timeslot> timeslots = appointmentService.getAvailableTimeslots(LocalDate.parse("2023-12-18"), service);
		for (Timeslot timeslot : timeslots) {
			System.out.println(timeslot.getStartTime().toString() + " - " + timeslot.getEndTime().toString());
		}

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