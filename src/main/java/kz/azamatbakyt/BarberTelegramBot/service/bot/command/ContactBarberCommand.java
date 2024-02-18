package kz.azamatbakyt.BarberTelegramBot.service.bot.command;

import kz.azamatbakyt.BarberTelegramBot.service.bot.TelegramUtils;
import kz.azamatbakyt.BarberTelegramBot.service.bot.model.Message;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

@Component
public class ContactBarberCommand implements Command{

    private final static String BARBER_PHONE_NUMBER = "Номер телефона: +7(747) 917-73-66\nАдрес: https://go.2gis.com/txowv";

    @Override
    public List<Message> apply(Update update) {
        SendMessage message = new SendMessage();
        message.setChatId(TelegramUtils.getStringChatId(update));
        message.setText(BARBER_PHONE_NUMBER);
        return build(message);
    }
}
