package kz.azamatbakyt.BarberTelegramBot.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class SchedulerNotificationService {

    @Scheduled(fixedRate = 3000)
    public void sendNotifications(){
        System.out.println("Scheduler Notification is active");
    }
}
