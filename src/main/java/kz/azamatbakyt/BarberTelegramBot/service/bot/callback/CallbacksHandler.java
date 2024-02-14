package kz.azamatbakyt.BarberTelegramBot.service.bot.callback;

import kz.azamatbakyt.BarberTelegramBot.service.bot.JsonHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Component
public class CallbacksHandler {

    private final Map<CallbackType, CallbackHandler> callbacks;

    @Autowired
    public CallbacksHandler(ServicesCallback servicesCallback) {
        this.callbacks = Map.of(CallbackType.SERVICES, servicesCallback
        );

    }

    public EditMessageText handleCallbacks(Update update) {
        List<String> list = JsonHandler.toList(update.getCallbackQuery().getData());
        long chatId = update.getCallbackQuery().getMessage().getChatId();

        EditMessageText answer;
        if (list.isEmpty()) {
            answer = new EditMessageText(String.valueOf(chatId));
        } else {
            CallbackType callbackType = CallbackType.valueOf(list.get(0));
            Callback callback = new Callback(callbackType, list.get(1));
            CallbackHandler callbackBiFunction = callbacks.get(callback.getCallbackType());
            answer = callbackBiFunction.apply(callback, update);
        }

        return answer;
    }

}