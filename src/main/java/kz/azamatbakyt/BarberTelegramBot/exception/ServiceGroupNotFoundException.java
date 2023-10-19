package kz.azamatbakyt.BarberTelegramBot.exception;

public class ServiceGroupNotFoundException extends RuntimeException{
    public ServiceGroupNotFoundException() {
    }

    public ServiceGroupNotFoundException(String message) {
        super(message);
    }
}
