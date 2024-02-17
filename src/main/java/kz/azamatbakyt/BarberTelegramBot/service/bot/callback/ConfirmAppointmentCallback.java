package kz.azamatbakyt.BarberTelegramBot.service.bot.callback;

import kz.azamatbakyt.BarberTelegramBot.entity.Appointment;
import kz.azamatbakyt.BarberTelegramBot.entity.Timeslot;
import kz.azamatbakyt.BarberTelegramBot.helpers.ContentType;
import kz.azamatbakyt.BarberTelegramBot.helpers.Status;
import kz.azamatbakyt.BarberTelegramBot.service.AppointmentService;
import kz.azamatbakyt.BarberTelegramBot.service.AppointmentTimeslotService;
import kz.azamatbakyt.BarberTelegramBot.service.TimeslotService;
import kz.azamatbakyt.BarberTelegramBot.service.bot.JsonHandler;
import kz.azamatbakyt.BarberTelegramBot.service.bot.TelegramUtils;
import kz.azamatbakyt.BarberTelegramBot.service.bot.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class ConfirmAppointmentCallback implements CallbackHandler{
    private final AppointmentService appointmentService;
    private final TimeslotService timeslotService;
    private final AppointmentTimeslotService appointmentTimeslotService;

    @Autowired
    public ConfirmAppointmentCallback(AppointmentService appointmentService, TimeslotService timeslotService, AppointmentTimeslotService appointmentTimeslotService) {
        this.appointmentService = appointmentService;
        this.timeslotService = timeslotService;
        this.appointmentTimeslotService = appointmentTimeslotService;
    }

    @Override
    public List<Message> apply(Callback callback, Update update) {

        String chatId = TelegramUtils.getMessageChatId(update);
        int messageId = TelegramUtils.getMessageId(update);

        LocalTime parsedTimeslot = LocalTime.parse(callback.getData());
        Appointment appointmentByChatId = appointmentService.getNotCreatedAppointmentByChatId(Long.valueOf(chatId), Status.DATE_SELECTED);
        int timeslotQty = appointmentByChatId.getService().getDuration() / 60;
        List<Timeslot> timeslotsToBook = timeslots(appointmentByChatId, parsedTimeslot);
        if (timeslotQty == 2 ){


        }

        Timeslot timeslot = timeslotsToBook.get(0);
        String text = "Вы выбрали время " + timeslot.getStartTime() +
                " - " + timeslot.getEndTime() +
                ". \nБронировать на это время?.";

        EditMessageText messageText = new EditMessageText();
        messageText.setChatId(chatId);
        messageText.setMessageId(messageId);
        messageText.setText(text);
        messageText.setReplyMarkup(keyboardForConfirmAppointment(timeslot));

        appointmentTimeslotService.update(timeslotsToBook, appointmentByChatId);
        appointmentService.updateAppointmnetByStatus(Long.valueOf(chatId), Status.DATE_SELECTED, Status.TIMESLOT_SELECTED);
        return build(messageText);
    }

    private List<Timeslot> timeslots(Appointment appointmentByChatId, LocalTime parsedTimeslot){
        List<LocalTime> times = new ArrayList<>();
        int duration = appointmentByChatId.getService().getDuration();
        int timeslotQty = duration / 60;
        for (int i = 0; i < timeslotQty; i++) {
            times.add(parsedTimeslot.plusHours(i));
        }
        List<Timeslot> timeslots = timeslotService.getTimeslots(times);

        return timeslots;
    }


    private InlineKeyboardMarkup keyboardForConfirmAppointment(Timeslot timeslot) {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();

        String callback1 = JsonHandler.toJson(
                List.of(CallbackType.APPOINTMENT_CREATED, timeslot.getStartTime().toString())
        );
        String callback2 = JsonHandler.toJson(
                List.of(CallbackType.NO_CANCEL_ACTION, timeslot.getEndTime().toString())
        );

        rowsInline.add(TelegramUtils.createAnswerKeyboard(
                callback1,
                callback2
        ));

        markup.setKeyboard(rowsInline);
        return markup;
    }

}
