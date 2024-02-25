package kz.azamatbakyt.BarberTelegramBot.service.bot.register;

import kz.azamatbakyt.BarberTelegramBot.entity.Client;
import kz.azamatbakyt.BarberTelegramBot.service.ClientService;
import kz.azamatbakyt.BarberTelegramBot.service.bot.TelegramUtils;
import kz.azamatbakyt.BarberTelegramBot.service.bot.register.model.RegistrationHandlerResult;
import kz.azamatbakyt.BarberTelegramBot.service.bot.register.model.RegistrationStep;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Map;

import static kz.azamatbakyt.BarberTelegramBot.service.bot.register.model.RegistrationStep.NAME;
import static kz.azamatbakyt.BarberTelegramBot.service.bot.register.model.RegistrationStep.PHONE;
import static kz.azamatbakyt.BarberTelegramBot.service.bot.register.model.RegistrationStep.REGISTRATION_SUCCESSFUL;

@Component
public class ClientRegistrationHandler {

    private final ClientService clientService;
    private final Map<RegistrationStep, RegistrationMessageHandler> registrationHandlers;


    public ClientRegistrationHandler(ClientService clientService) {
        this.clientService = clientService;
        this.registrationHandlers = Map.of(
                NAME, new AskNameRegistrationMessageHandler(clientService),
                PHONE, new AskPhoneRegistrationHandler(clientService),
                REGISTRATION_SUCCESSFUL, new SuccessRegistrationMessageHandler(clientService)
        );
    }

    public SendMessage registerClientIfNeeded(Update update) {
        Long chatId = TelegramUtils.getChatId(update);
        Client client = clientService.getClientByChatId(chatId);
        if (client != null && client.isRegistrationCompleted()) return null;
        RegistrationStep step = getRegistrationStep(client);
        RegistrationMessageHandler handler = registrationHandlers.get(step);
        RegistrationHandlerResult result = handler.apply(update);
        clientService.save(result.getClient());
        return result.getMessage();
    }


    private RegistrationStep getRegistrationStep(Client client) {
        if (client == null) {
            return NAME;
        } else {
            if (client.getName() == null) {
                return PHONE;
            } else return REGISTRATION_SUCCESSFUL;
        }
    }


}
