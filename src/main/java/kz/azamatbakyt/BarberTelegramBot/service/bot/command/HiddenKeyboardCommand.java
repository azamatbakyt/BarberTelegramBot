package kz.azamatbakyt.BarberTelegramBot.service.bot.command;

import kz.azamatbakyt.BarberTelegramBot.service.bot.JsonHandler;
import kz.azamatbakyt.BarberTelegramBot.service.bot.TelegramUtils;
import kz.azamatbakyt.BarberTelegramBot.service.bot.callback.CallbackType;
import kz.azamatbakyt.BarberTelegramBot.service.bot.model.Message;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

@Component
public class HiddenKeyboardCommand implements Command{

    private final static String TEXT = "Просим прощения что с вами случилось такое, но это ошибка со стороны Telegram!" +
            "Если у вас исчезла клавиатура, пожалуйста нажмите на кнопку удалить мои данные и перезапустите бота. Спасибо за ваше понимание!";

    @Override
    public List<Message> apply(Update update) {
        String chatId = TelegramUtils.getStringChatId(update);
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(TEXT);
        message.setReplyMarkup(deleteMyData(chatId));
        return build(message);
    }

    private InlineKeyboardMarkup deleteMyData(String chatId){
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();
        String callback = JsonHandler.toJson(List.of(CallbackType.DELETE_MY_DATA, chatId));
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText("Удалить");
        button.setCallbackData(callback);

        rowsInLine.add(List.of(button));
        markup.setKeyboard(rowsInLine);
        return markup;
    }


}
