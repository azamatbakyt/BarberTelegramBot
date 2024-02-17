package kz.azamatbakyt.BarberTelegramBot.service.bot.callback;

import kz.azamatbakyt.BarberTelegramBot.entity.CustomerService;
import kz.azamatbakyt.BarberTelegramBot.service.CSService;
import kz.azamatbakyt.BarberTelegramBot.service.bot.JsonHandler;
import kz.azamatbakyt.BarberTelegramBot.service.bot.TelegramUtils;
import kz.azamatbakyt.BarberTelegramBot.service.bot.model.Message;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

@Component
public class ServiceAgreeCallback implements CallbackHandler {

    private final static String MAIN_TEXT = "%s" + "\n" + "Услуга: %s" + "\n" +
            "Цена: %d" + "тг" + "\n" +
            "Длительность: %d" + " мин\n" +
            "Вы хотите заказать эту услугу?";

    private final CSService csService;

    public ServiceAgreeCallback(CSService csService) {
        this.csService = csService;
    }

    @Override
    public List<Message> apply(Callback callback, Update update) {
        String text = "Вы выбрали - " + callback.getData() + ". \n" +
                "Ниже представлена информация про услугу:\n";
        CustomerService service = csService.getServiceByName(callback.getData());
        String chatId = TelegramUtils.getMessageChatId(update);
        int messageId = TelegramUtils.getMessageId(update);

        String title = String.format(MAIN_TEXT, text, service.getName(), service.getPrice(), service.getDuration());
        EditMessageText messageText = new EditMessageText();
        messageText.setMessageId(messageId);
        messageText.setChatId(chatId);
        messageText.setText(title);
        messageText.setReplyMarkup(answers(callback.getData()));
        return build(messageText);
    }

    private InlineKeyboardMarkup answers(String service) {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        String callback1 = JsonHandler.toJson(List.of(CallbackType.YES_CREATE_SERVICE, service));
        String callback2 = JsonHandler.toJson(List.of(CallbackType.NO_CANCEL_ACTION, service));
        rowsInline.add(
                TelegramUtils.createAnswerKeyboard(
                        callback1,
                        callback2
                )
        );
        markup.setKeyboard(rowsInline);
        return markup;
    }
}
