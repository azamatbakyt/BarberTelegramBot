package kz.azamatbakyt.BarberTelegramBot.service.bot;


import kz.azamatbakyt.BarberTelegramBot.service.bot.command.CommandType;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

public class TelegramUtils {

    public static Long getChatId(Update update) {
        return update.getMessage().getChatId();
    }

    public static String getStringChatId(Update update) {
        return getChatId(update).toString();
    }

    public static Integer getStringMessageId(Update update){ return update.getCallbackQuery().getMessage().getMessageId();}
    public static ReplyKeyboardMarkup startCommandKeyboard() {

        ReplyKeyboardMarkup keyboard = new ReplyKeyboardMarkup();
        List<KeyboardRow> rows = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();

        row.add(CommandType.SERVICE_GROUP.getCommandText());
        row.add("Мои работы");
        rows.add(row);

        row = new KeyboardRow();
        row.add("Мои активные записи");
        row.add("Связаться с барбером");
        rows.add(row);

        row = new KeyboardRow();
        row.add("Управление бронированием");
        rows.add(row);
        keyboard.setKeyboard(rows);

        return keyboard;
    }

}
