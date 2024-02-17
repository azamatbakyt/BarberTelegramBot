package kz.azamatbakyt.BarberTelegramBot.service.bot.callback;

import kz.azamatbakyt.BarberTelegramBot.service.bot.model.Message;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Collections;
import java.util.List;

public interface CallbackHandler {

    default List<Message> build(EditMessageText messageText) {
        return Collections.singletonList(new Message(messageText));
    }

    List<Message> apply(Callback callback, Update update);
}
