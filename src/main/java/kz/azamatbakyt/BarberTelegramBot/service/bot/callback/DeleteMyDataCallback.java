package kz.azamatbakyt.BarberTelegramBot.service.bot.callback;

import kz.azamatbakyt.BarberTelegramBot.service.ClientService;
import kz.azamatbakyt.BarberTelegramBot.service.bot.TelegramUtils;
import kz.azamatbakyt.BarberTelegramBot.service.bot.model.Message;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

@Component
public class DeleteMyDataCallback implements CallbackHandler{

    private final ClientService clientService;

    public DeleteMyDataCallback(ClientService clientService) {
        this.clientService = clientService;
    }

    @Override
    public List<Message> apply(Callback callback, Update update) {
        EditMessageText messageText = new EditMessageText();
        String chatId = TelegramUtils.getMessageChatId(update);
        int messageId = TelegramUtils.getMessageId(update);
        clientService.delete(clientService.getClientByChatId(Long.valueOf(chatId)).getId());
        messageText.setChatId(chatId);
        messageText.setMessageId(messageId);
        messageText.setText("Ваши данныые удалены! \nНажмите на /start для перезапуска!");
        return build(messageText);
    }
}
