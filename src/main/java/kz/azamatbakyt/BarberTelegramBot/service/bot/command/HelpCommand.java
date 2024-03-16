package kz.azamatbakyt.BarberTelegramBot.service.bot.command;

import kz.azamatbakyt.BarberTelegramBot.service.bot.TelegramUtils;
import kz.azamatbakyt.BarberTelegramBot.service.bot.model.Message;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

@Component
public class HelpCommand  implements Command {
    private final static String HELP_TEXT = "Этот бот создан для бронирования моих услуг.\n\n" +
            "Просим ознакомиться командами которые расположены ниже: \n\n" +
            "Напишите /start для перезагрузки бота в случае каких то ошибок\n\n" +
            "Нажмите Услуги для просмотра всех категории\n\n" +
            "Нажмите Мои работы для просмотра фото моих работ\n\n" +
            "Нажмите Связаться с барбером для просмотра адреса и моего номера\n\n" +
            "Нажмите Активные записи для просмотра всех ваших активных записей\n\n" +
            "Нажмите /help для просмотра этого сообщения обратно";

    @Override
    public List<Message> apply(Update update) {
        String chatId = TelegramUtils.getStringChatId(update);
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(HELP_TEXT);

        return build(sendMessage);
    }
}
