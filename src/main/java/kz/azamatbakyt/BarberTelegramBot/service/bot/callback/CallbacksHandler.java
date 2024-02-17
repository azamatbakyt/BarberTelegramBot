package kz.azamatbakyt.BarberTelegramBot.service.bot.callback;

import kz.azamatbakyt.BarberTelegramBot.service.bot.JsonHandler;
import kz.azamatbakyt.BarberTelegramBot.service.bot.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Component
public class CallbacksHandler {

    private final Map<CallbackType, CallbackHandler> callbacks;

    @Autowired
    public CallbacksHandler(ServicesCallback servicesCallback,
                            ServiceAgreeCallback serviceAgreeCallback,
                            GetDaysCallback daysCallback,
                            CancelActionCallback  cancelCallback,
                            TimeslotCallback timeslotCallback,
                            ConfirmAppointmentCallback confirmAppointmentCallback,
                            AppointmentCreated appointmentCreated) {
        this.callbacks = Map.of(
                CallbackType.SERVICES, servicesCallback,
                CallbackType.SERVICE_AGREE, serviceAgreeCallback,
                CallbackType.YES_CREATE_SERVICE, daysCallback,
                CallbackType.NO_CANCEL_ACTION, cancelCallback,
                CallbackType.DATE, timeslotCallback,
                CallbackType.TIMESLOTS, confirmAppointmentCallback,
                CallbackType.APPOINTMENT_CREATED, appointmentCreated
        );

    }

    public List<Message> handleCallbacks(Update update) {
        List<String> list = JsonHandler.toList(update.getCallbackQuery().getData());
        long chatId = update.getCallbackQuery().getMessage().getChatId();

        List<Message> answer;
        if (list.isEmpty()) {
            answer = Collections.singletonList(new Message(new EditMessageText(String.valueOf(chatId))));
        } else {
            CallbackType callbackType = CallbackType.valueOf(list.get(0));
            Callback callback = new Callback(callbackType, list.get(1));
            CallbackHandler callbackBiFunction = callbacks.get(callback.getCallbackType());
            answer = callbackBiFunction.apply(callback, update);
        }

        return answer;
    }

}