package kz.azamatbakyt.BarberTelegramBot;

import kz.azamatbakyt.BarberTelegramBot.service.AppointmentService;
import kz.azamatbakyt.BarberTelegramBot.service.AppointmentTimeslotService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BarberTelegramBotApplication{

    private final AppointmentService appointmentService;
    private  final AppointmentTimeslotService appointmentTimeslotService;

    public BarberTelegramBotApplication(AppointmentService appointmentService, AppointmentTimeslotService appointmentTimeslotService) {
        this.appointmentService = appointmentService;
        this.appointmentTimeslotService = appointmentTimeslotService;
    }

    public static void main(String[] args) {
        SpringApplication.run(BarberTelegramBotApplication.class, args);
//        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
//        System.out.println(passwordEncoder.encode("meirbek123"));
    }




}