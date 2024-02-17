package kz.azamatbakyt.BarberTelegramBot.service.bot.command;

import kz.azamatbakyt.BarberTelegramBot.service.bot.TelegramUtils;
import kz.azamatbakyt.BarberTelegramBot.service.bot.model.Message;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

@Component
public class HelpCommand  implements Command {
    private final static String HELP_TEXT = "This bot is created to demonstrate Spring capabilities.\n\n" +
            "You can execute commands from the main menu on the left or by typing commands:\n\n" +
            "Type /start to see welcome message\n\n" +
            "Type /services to see welcome message\n\n" +
            "Type /register to register yourself\n\n" +
            "Type /mydata to see data stored about yourself\n\n" +
            "Type /help to see this message again";

    @Override
    public List<Message> apply(Update update) {
        String chatId = TelegramUtils.getStringChatId(update);
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(HELP_TEXT);

        return build(sendMessage);
    }
}
