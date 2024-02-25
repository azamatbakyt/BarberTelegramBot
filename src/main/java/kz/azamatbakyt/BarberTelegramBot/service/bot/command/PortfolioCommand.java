package kz.azamatbakyt.BarberTelegramBot.service.bot.command;

import kz.azamatbakyt.BarberTelegramBot.config.s3.S3Config;
import kz.azamatbakyt.BarberTelegramBot.service.PortfolioService;
import kz.azamatbakyt.BarberTelegramBot.service.bot.TelegramUtils;
import kz.azamatbakyt.BarberTelegramBot.service.bot.model.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class PortfolioCommand implements Command {
    private static final Logger LOGGER = LoggerFactory.getLogger(S3Config.class);
    private final static String PORTFOLIO_TEXT = "Фотографии, как отражение души. " +
            "Добро пожаловать в галерею моего творчества!";
    private final PortfolioService portfolioService;

    public PortfolioCommand(PortfolioService portfolioService) {
        this.portfolioService = portfolioService;
    }

    @Override
    public List<Message> apply(Update update) {
        String chatId = TelegramUtils.getStringChatId(update);
        List<Message> result = new ArrayList<>();
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(PORTFOLIO_TEXT);
        result.add(new Message(sendMessage));

        try {
            List<byte[]> portfolioList = portfolioService.findAll();
            for (byte[] data : portfolioList) {
                File tempFile = File.createTempFile("abc", ".jpg");
                try (FileOutputStream fos = new FileOutputStream(tempFile)) {
                    fos.write(data);
                    result.add(new Message(new SendPhoto(chatId, new InputFile(tempFile))));
                }

            }
        } catch (IOException e) {
            LOGGER.error("Не удается загрузить файл картинки в сообщение", e);
        }

        return result;
    }
}
