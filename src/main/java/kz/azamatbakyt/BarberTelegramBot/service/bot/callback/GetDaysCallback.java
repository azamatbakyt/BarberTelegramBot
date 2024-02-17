package kz.azamatbakyt.BarberTelegramBot.service.bot.callback;

import kz.azamatbakyt.BarberTelegramBot.entity.Appointment;
import kz.azamatbakyt.BarberTelegramBot.entity.CustomerService;
import kz.azamatbakyt.BarberTelegramBot.helpers.Status;
import kz.azamatbakyt.BarberTelegramBot.service.AppointmentService;
import kz.azamatbakyt.BarberTelegramBot.service.CSService;
import kz.azamatbakyt.BarberTelegramBot.service.ClientService;
import kz.azamatbakyt.BarberTelegramBot.service.ScheduleService;
import kz.azamatbakyt.BarberTelegramBot.service.bot.JsonHandler;
import kz.azamatbakyt.BarberTelegramBot.service.bot.TelegramUtils;
import kz.azamatbakyt.BarberTelegramBot.service.bot.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component
public class GetDaysCallback implements CallbackHandler{

    private final static String text = "Вы выбрали услугу %s" +
            ". \nА давайте теперь выберем свободное для вас время.";

    private final CSService csService;
    private final AppointmentService appointmentService;
    private final ScheduleService scheduleService;
    private final ClientService clientService;

    @Autowired
    public GetDaysCallback(CSService csService, AppointmentService appointmentService, ScheduleService scheduleService, ClientService clientService) {
        this.csService = csService;
        this.appointmentService = appointmentService;
        this.scheduleService = scheduleService;
        this.clientService = clientService;
    }


    @Override
    public List<Message> apply(Callback callback, Update update) {
        CustomerService service = csService.getServiceByName(callback.getData());
        String chatId = TelegramUtils.getMessageChatId(update);
        int messageId = TelegramUtils.getMessageId(update);
        EditMessageText messageText = new EditMessageText();
        messageText.setChatId(chatId);
        messageText.setMessageId(messageId);
        messageText.setText(String.format(text, service.getName()));
        messageText.setReplyMarkup(daysKeyboard());
        appointmentService.save(new Appointment(
                clientService.getClientByChatId(Long.valueOf(chatId)),
                service,
                Status.SERVICE_SELECTED.toString())
        );
        return build(messageText);
    }

    private InlineKeyboardMarkup daysKeyboard() {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<LocalDate> days = scheduleService.getDays();
        // TODO вывести дни
        List<InlineKeyboardButton> currentRow = new ArrayList<>();

        for (LocalDate day : days) {
            InlineKeyboardButton button = new InlineKeyboardButton();
            String formattedDay = day.format(DateTimeFormatter.ofPattern("dd MMMM yyyy"));
            button.setText(formattedDay);
            String callback = JsonHandler.toJson(List.of(CallbackType.DATE, day.toString()));
            button.setCallbackData(callback);
            currentRow.add(button);

            if (currentRow.size() == 2) {
                rowsInline.add(currentRow);
                currentRow = new ArrayList<>();
            }
        }

        if (!currentRow.isEmpty()) {
            rowsInline.add(currentRow);
        }

        markup.setKeyboard(rowsInline);

        return markup;
    }
}
