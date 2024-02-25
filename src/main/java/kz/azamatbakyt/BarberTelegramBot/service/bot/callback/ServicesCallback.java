package kz.azamatbakyt.BarberTelegramBot.service.bot.callback;

import kz.azamatbakyt.BarberTelegramBot.entity.CustomerService;
import kz.azamatbakyt.BarberTelegramBot.entity.CustomerServiceGroup;

import kz.azamatbakyt.BarberTelegramBot.service.CSService;
import kz.azamatbakyt.BarberTelegramBot.service.CustomerServiceGroupService;
import kz.azamatbakyt.BarberTelegramBot.service.bot.JsonHandler;
import kz.azamatbakyt.BarberTelegramBot.service.bot.TelegramUtils;
import kz.azamatbakyt.BarberTelegramBot.service.bot.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ServicesCallback implements CallbackHandler{
    private final CustomerServiceGroupService customerServiceGroupService;
    private final CSService csService;

    @Autowired
    public ServicesCallback(CustomerServiceGroupService customerServiceGroupService, CSService csService) {
        this.customerServiceGroupService = customerServiceGroupService;
        this.csService = csService;
    }

    @Override
    public List<Message> apply(Callback callback, Update update) {
        String text = "Вы выбрали " + callback.getData() + " . \nДавайте выберем какую именно услугу вы хотите?";
        String chatId = TelegramUtils.getMessageChatId(update);
        int messageId = TelegramUtils.getMessageId(update);
        CustomerServiceGroup group = customerServiceGroupService.getServiceGroupByName(callback.getData());
        EditMessageText answer = new EditMessageText();
        answer.setChatId(chatId);
        answer.setMessageId(messageId);
        answer.setText(text);
        answer.setReplyMarkup(services(group));
        return build(answer);
    }

    private InlineKeyboardMarkup services(CustomerServiceGroup group) {
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<CustomerService> services = csService.getServices()
                .stream()
                .filter(s -> s.getGroup().getId().equals(group.getId()))
                .collect(Collectors.toList());

        List<InlineKeyboardButton> currentRow = new ArrayList<>();
        for (CustomerService service : services) {
            InlineKeyboardButton haircutMen = new InlineKeyboardButton();
            haircutMen.setText(service.getName());
            String callback = JsonHandler.toJson(List.of(CallbackType.SERVICE_AGREE, service.getName()));
            haircutMen.setCallbackData(callback);

            currentRow.add(haircutMen);

            if (currentRow.size() == 2) {
                rowsInline.add(currentRow);
                currentRow = new ArrayList<>();
            }
        }

        if (!currentRow.isEmpty()) {
            rowsInline.add(currentRow);
        }

        markupInline.setKeyboard(rowsInline);
        return markupInline;
    }
}
