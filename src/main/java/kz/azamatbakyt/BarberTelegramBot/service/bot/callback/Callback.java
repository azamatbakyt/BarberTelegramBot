package kz.azamatbakyt.BarberTelegramBot.service.bot.callback;


import lombok.Builder;

@Builder
public class Callback {

    private final CallbackType callbackType;

    private final String data;

    public Callback(CallbackType callbackType, String data) {
        this.callbackType = callbackType;
        this.data = data;
    }

    public CallbackType getCallbackType() {
        return callbackType;
    }

    public String getData() {
        return data;
    }
}


