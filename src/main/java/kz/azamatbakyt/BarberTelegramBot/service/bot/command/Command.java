package kz.azamatbakyt.BarberTelegramBot.service.bot.command;

import kz.azamatbakyt.BarberTelegramBot.service.bot.model.Message;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Collections;
import java.util.List;

public interface Command {

    default List<Message> build(SendMessage sendMessage) {
        return Collections.singletonList(new Message(sendMessage));
    }

    List<Message> apply(Update update);
}
