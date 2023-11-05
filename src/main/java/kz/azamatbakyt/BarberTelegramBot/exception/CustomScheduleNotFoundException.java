package kz.azamatbakyt.BarberTelegramBot.exception;

public class CustomScheduleNotFoundException extends RuntimeException {

    public CustomScheduleNotFoundException() {}

    public CustomScheduleNotFoundException(String message) {
        super(message);
    }
}
