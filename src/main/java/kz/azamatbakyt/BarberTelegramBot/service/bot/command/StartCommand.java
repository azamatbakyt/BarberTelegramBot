package kz.azamatbakyt.BarberTelegramBot.service.bot.command;

import com.vdurmont.emoji.EmojiParser;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class StartCommand implements Command {

    @Override
    public SendMessage apply(Update update) {
        long chatId = update.getMessage().getChatId();
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chatId));
        String text = EmojiParser.parseToUnicode("И снова здравствуйте, " + "client.getName()"
                + ", что я могу вам предложить!" + "\uD83D\uDE0A");
        sendMessage.setText(text);

        return sendMessage;
    }

}
