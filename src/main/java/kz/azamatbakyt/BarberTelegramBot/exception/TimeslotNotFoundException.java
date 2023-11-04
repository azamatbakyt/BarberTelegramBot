package kz.azamatbakyt.BarberTelegramBot.exception;

public class TimeslotNotFoundException extends RuntimeException{
    public TimeslotNotFoundException() {
    }

    public TimeslotNotFoundException(String message) {
        super(message);
    }
}
