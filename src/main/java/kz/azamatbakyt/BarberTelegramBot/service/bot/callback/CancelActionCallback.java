package kz.azamatbakyt.BarberTelegramBot.service.bot.callback;

import kz.azamatbakyt.BarberTelegramBot.service.bot.TelegramUtils;
import kz.azamatbakyt.BarberTelegramBot.service.bot.model.Message;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;


@Component
public class CancelActionCallback implements CallbackHandler{

    private final static String CANCEL_ACTION_TEXT = "Вы только что отменили действие! \nПрошу вас начните заново! \nСпасибо за ваше понимание!";

    @Override
    public List<Message> apply(Callback callback, Update update) {
        String chatId = TelegramUtils.getMessageChatId(update);
        int messageId = TelegramUtils.getMessageId(update);
        EditMessageText messageText = new EditMessageText();
        messageText.setChatId(chatId);
        messageText.setMessageId(messageId);
        messageText.setText(CANCEL_ACTION_TEXT);
        return build(messageText);
    }
}
