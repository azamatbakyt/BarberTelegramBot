package kz.azamatbakyt.BarberTelegramBot.service.bot.register;

import com.vdurmont.emoji.EmojiParser;
import kz.azamatbakyt.BarberTelegramBot.entity.Client;
import kz.azamatbakyt.BarberTelegramBot.service.ClientService;
import kz.azamatbakyt.BarberTelegramBot.service.bot.TelegramUtils;
import kz.azamatbakyt.BarberTelegramBot.service.bot.register.model.RegistrationHandlerResult;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;

public class SuccessRegistrationMessageHandler extends RegistrationMessageHandler {


    private final static String MESSAGE_TEXT = EmojiParser.parseToUnicode("Добро пожаловать, %s что я могу вам предложить!" + "\uD83D\uDE0A");

    protected SuccessRegistrationMessageHandler(ClientService clientService) {
        super(clientService);
    }

    @Override
    RegistrationHandlerResult apply(Update update) {
        String phone = update.getMessage().getText();
        Long chatId = TelegramUtils.getChatId(update);
        Client client = clientService.getClientByChatId(chatId);
        client.setPhone(phone);
        client.setRegistrationCompleted(true);

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chatId));
        sendMessage.setReplyMarkup(TelegramUtils.startCommandKeyboard());
        sendMessage.setText(String.format(MESSAGE_TEXT, client.getName()));

        return new RegistrationHandlerResult(sendMessage, client);
    }
}
