package kz.azamatbakyt.BarberTelegramBot.service.bot.callback;

import kz.azamatbakyt.BarberTelegramBot.entity.Appointment;
import kz.azamatbakyt.BarberTelegramBot.entity.Timeslot;
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
    
    private final static String empty = "К сожалению на сегодня свободных таймслотов нету!";
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
        if (callback.getData().equals("null")){
            EditMessageText messageText = new EditMessageText();
            messageText.setChatId(chatId);
            messageText.setMessageId(messageId);
            messageText.setText(empty);

            appointmentService.deleteAppointment(
                    appointmentService.getNotCreatedAppointmentByChatId(
                            Long.valueOf(chatId), Status.DATE_SELECTED
                    )
            );
            return build(messageText);
        }

        LocalTime parsedTimeslot = LocalTime.parse(callback.getData());
        Appointment appointmentByChatId = appointmentService.getNotCreatedAppointmentByChatId(Long.valueOf(chatId), Status.DATE_SELECTED);

        int timeslotQty = appointmentByChatId.getService().getDuration() / 60;
        List<Timeslot> timeslotsToBook = timeslots(appointmentByChatId, parsedTimeslot);
        if (timeslotQty == 2 ){
            String text = "Вы выбрали время " + timeslotsToBook.get(0).getStartTime() +
                    " - " + timeslotsToBook.get(0).getEndTime() + "\n" +
                    timeslotsToBook.get(1).getStartTime() + " - " + timeslotsToBook.get(1).getEndTime() +
                    ". \nБронировать на это время?.";

            EditMessageText messageText = new EditMessageText();
            messageText.setChatId(chatId);
            messageText.setMessageId(messageId);
            appointmentTimeslotService.update(timeslotsToBook, appointmentByChatId);
            appointmentService.updateAppointmnetByStatus(Long.valueOf(chatId), Status.DATE_SELECTED, Status.TIMESLOT_SELECTED);
            messageText.setText(text);
            messageText.setReplyMarkup(keyboardForConfirmAppointment(timeslotsToBook.get(0)));
            return build(messageText);
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

        appointmentTimeslotService.updateOneHourAppointmnent(timeslotsToBook, appointmentByChatId);
        appointmentService.updateAppointmnetByStatus(Long.valueOf(chatId), Status.DATE_SELECTED, Status.TIMESLOT_SELECTED);
        return build(messageText);
    }

    private List<Timeslot> timeslots(Appointment appointmentByChatId, LocalTime parsedTimeslot){
        List<LocalTime> times = new ArrayList<>();
        int duration = appointmentByChatId.getService().getDuration();
        int timeslotQty = duration / 60;
        for (int i = 0; i < timeslotQty; i++) {
            times.add(parsedTimeslot.plusHours(i).withMinute(0).withSecond(0));
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
                List.of(CallbackType.APPOINTMENT_NOT_CREATED, timeslot.getEndTime().toString())
        );

        rowsInline.add(TelegramUtils.createAnswerKeyboard(
                callback1,
                callback2
        ));

        markup.setKeyboard(rowsInline);
        return markup;
    }

}
