package kz.azamatbakyt.BarberTelegramBot;

import kz.azamatbakyt.BarberTelegramBot.service.AppointmentService;
import kz.azamatbakyt.BarberTelegramBot.service.CSService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BarberTelegramBotApplication {

    @Autowired
    private AppointmentService appointmentService;
    @Autowired
    private CSService csService;

    public static void main(String[] args) {
        SpringApplication.run(BarberTelegramBotApplication.class, args);
    }


}