package kz.azamatbakyt.BarberTelegramBot.exception;

public class ScheduleNotFoundException extends RuntimeException{

    public ScheduleNotFoundException() {
    }

    public ScheduleNotFoundException(String message) {
        super(message);
    }
}
