package kz.azamatbakyt.BarberTelegramBot.service.bot.register;

import kz.azamatbakyt.BarberTelegramBot.entity.Client;
import kz.azamatbakyt.BarberTelegramBot.service.ClientService;
import kz.azamatbakyt.BarberTelegramBot.service.bot.register.model.RegistrationHandlerResult;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public abstract class RegistrationMessageHandler {

    protected final ClientService clientService;

    protected RegistrationMessageHandler(ClientService clientService) {
        this.clientService = clientService;
    }

    abstract RegistrationHandlerResult apply(Update update);
}
