package kz.azamatbakyt.BarberTelegramBot.service.bot.callback;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface CallbackHandler {
    EditMessageText apply(Callback callback, Update update);
}
