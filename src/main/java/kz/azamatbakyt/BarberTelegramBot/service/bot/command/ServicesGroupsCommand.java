package kz.azamatbakyt.BarberTelegramBot.service.bot.command;

import com.vdurmont.emoji.EmojiParser;
import kz.azamatbakyt.BarberTelegramBot.entity.CustomerServiceGroup;
import kz.azamatbakyt.BarberTelegramBot.service.CustomerServiceGroupService;
import kz.azamatbakyt.BarberTelegramBot.service.bot.JsonHandler;
import kz.azamatbakyt.BarberTelegramBot.service.bot.TelegramUtils;
import kz.azamatbakyt.BarberTelegramBot.service.bot.callback.CallbackType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

@Component
public class ServicesGroupsCommand implements Command {
    private final static String ALL_SERVICES = "What service would you want to choose?";

    private final CustomerServiceGroupService customerServiceGroupService;

    public ServicesGroupsCommand(CustomerServiceGroupService customerServiceGroupService) {
        this.customerServiceGroupService = customerServiceGroupService;
    }

    @Override
    public SendMessage apply(Update update) {
        String chatId = TelegramUtils.getStringChatId(update);
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(ALL_SERVICES);
        sendMessage.setReplyMarkup(serviceGroups());

        return sendMessage;
    }

    private InlineKeyboardMarkup serviceGroups() {
        List<CustomerServiceGroup> serviceGroups = customerServiceGroupService.getServiceGroups();
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> currentRow = new ArrayList<>();
        for (CustomerServiceGroup serviceGroup : serviceGroups) {

            InlineKeyboardButton buttonHaircutService = new InlineKeyboardButton();
            buttonHaircutService.setText(serviceGroup.getName());
            String callback = JsonHandler.toJson(List.of(CallbackType.SERVICES, serviceGroup.getName()));
            buttonHaircutService.setCallbackData(callback);
            currentRow.add(buttonHaircutService);

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
