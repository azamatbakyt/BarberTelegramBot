package kz.azamatbakyt.BarberTelegramBot.service.bot;


import kz.azamatbakyt.BarberTelegramBot.service.bot.command.CommandType;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

public class TelegramUtils {


    public static String getMessageChatId(Update update){
        return update.getCallbackQuery().getMessage().getChatId().toString();
    }
    public static Long getChatId(Update update) {

        return update.getMessage().getChatId();
    }

    public static String getStringChatId(Update update) {
        return getChatId(update).toString();
    }

    public static Integer getMessageId(Update update){ return update.getCallbackQuery().getMessage().getMessageId();}
    public static ReplyKeyboardMarkup startCommandKeyboard() {

        ReplyKeyboardMarkup keyboard = new ReplyKeyboardMarkup();
        List<KeyboardRow> rows = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();

        row.add(CommandType.SERVICE_GROUP.getCommandText());
        row.add(CommandType.PORTFOLIO.getCommandText());
        rows.add(row);

        row = new KeyboardRow();
        row.add(CommandType.ACTIVE_APPOINTMENTS.getCommandText());
        row.add(CommandType.CONTACT_BARBER.getCommandText());
        rows.add(row);

        keyboard.setKeyboard(rows);

        return keyboard;
    }

    public static List<InlineKeyboardButton> createAnswerKeyboard(String callback1, String callback2) {
        InlineKeyboardButton yesBtn = new InlineKeyboardButton();
        yesBtn.setText("Да");
        yesBtn.setCallbackData(callback1);

        InlineKeyboardButton noBtn = new InlineKeyboardButton();
        noBtn.setText("Нет");
        noBtn.setCallbackData(callback2);

        return List.of(yesBtn, noBtn);
    }

}
