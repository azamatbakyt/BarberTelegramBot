package kz.azamatbakyt.BarberTelegramBot.service.bot.command;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface Command {
    SendMessage apply(Update update);
}
