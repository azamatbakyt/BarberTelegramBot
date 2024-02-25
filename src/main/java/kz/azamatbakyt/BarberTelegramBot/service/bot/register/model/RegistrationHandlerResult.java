package kz.azamatbakyt.BarberTelegramBot.service.bot.register.model;

import kz.azamatbakyt.BarberTelegramBot.entity.Client;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public class RegistrationHandlerResult {

    private final SendMessage message;
    private final Client client;

    public RegistrationHandlerResult(SendMessage message, Client client) {
        this.message = message;
        this.client = client;
    }

    public SendMessage getMessage() {
        return message;
    }

    public Client getClient() {
        return client;
    }
}
