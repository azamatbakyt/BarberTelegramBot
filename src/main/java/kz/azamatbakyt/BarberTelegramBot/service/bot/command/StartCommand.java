package kz.azamatbakyt.BarberTelegramBot.service.bot.command;

import com.vdurmont.emoji.EmojiParser;
import kz.azamatbakyt.BarberTelegramBot.entity.Client;
import kz.azamatbakyt.BarberTelegramBot.service.ClientService;
import kz.azamatbakyt.BarberTelegramBot.service.bot.model.Message;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

@Component
public class StartCommand implements Command {


    private final ClientService clientsService;

    public StartCommand(ClientService clientsService) {
        this.clientsService = clientsService;
    }

    @Override
    public List<Message> apply(Update update) {
        long chatId = update.getMessage().getChatId();
        Client client = clientsService.getClientByChatId(chatId);
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chatId));
        String text = EmojiParser.parseToUnicode("И снова здравствуйте, " + client.getName()
                + ", что я могу вам предложить!" + "\uD83D\uDE0A");
        sendMessage.setText(text);

        return build(sendMessage);
    }

}
