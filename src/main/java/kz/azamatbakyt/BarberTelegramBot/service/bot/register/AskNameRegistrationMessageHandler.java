package kz.azamatbakyt.BarberTelegramBot.service.bot.register;

import kz.azamatbakyt.BarberTelegramBot.entity.Client;
import kz.azamatbakyt.BarberTelegramBot.service.ClientService;
import kz.azamatbakyt.BarberTelegramBot.service.bot.TelegramUtils;


import kz.azamatbakyt.BarberTelegramBot.service.bot.register.model.RegistrationHandlerResult;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;

public class AskNameRegistrationMessageHandler extends RegistrationMessageHandler {
    private final static String HELLO_MESSAGE = "Здравствуй, дорогой пользователь!" +
            " Я смотрю ты у нас новый посетитель. Давай пройдем мини регистрацию. \n\n Напиши свое имя:";

    protected AskNameRegistrationMessageHandler(ClientService clientService) {
        super(clientService);
    }

    @Override
    RegistrationHandlerResult apply(Update update) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(TelegramUtils.getStringChatId(update));
        sendMessage.setText(HELLO_MESSAGE);
        sendMessage.setReplyMarkup(ReplyKeyboardRemove.builder().removeKeyboard(true).build());

        Client client = new Client();
        client.setChatId(TelegramUtils.getChatId(update));

        return new RegistrationHandlerResult(sendMessage, client);
    }
}
