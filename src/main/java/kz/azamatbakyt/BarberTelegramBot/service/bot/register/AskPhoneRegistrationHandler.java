package kz.azamatbakyt.BarberTelegramBot.service.bot.register;

import kz.azamatbakyt.BarberTelegramBot.entity.Client;
import kz.azamatbakyt.BarberTelegramBot.service.ClientService;
import kz.azamatbakyt.BarberTelegramBot.service.bot.TelegramUtils;
import kz.azamatbakyt.BarberTelegramBot.service.bot.register.model.RegistrationHandlerResult;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;

public class AskPhoneRegistrationHandler extends RegistrationMessageHandler {

    private final static String ASK_PHONE = "А теперь пожалуйста напишите ваш номер телефона: ";

    protected AskPhoneRegistrationHandler(ClientService clientService) {
        super(clientService);
    }

    @Override
    RegistrationHandlerResult apply(Update update) {
        String name = update.getMessage().getText();
        Long chatId = TelegramUtils.getChatId(update);
        Client client = clientService.getClientByChatId(chatId);
        client.setName(name);

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chatId));
        sendMessage.setReplyMarkup(ReplyKeyboardRemove.builder().removeKeyboard(true).build());
        sendMessage.setText(ASK_PHONE);

        return new RegistrationHandlerResult(sendMessage, client);
    }
}
