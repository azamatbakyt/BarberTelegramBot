package kz.azamatbakyt.BarberTelegramBot.service.bot.command;

public enum CommandType {

    START("/start"),
    SERVICE_GROUP("Услуги"),
    HELP("/help"),
    PORTFOLIO("Мои работы"),
    ACTIVE_APPOINTMENTS("Мои активные записи"),
    CONTACT_BARBER("Связаться с барбером"),
    MANAGE_APPOINTMENTS("Управление бронированием"),
    NOT_FINISHED_APPOINTEMNT_COMMAND("Незавершенные бронирования")
    ;


    private final String commandText;

    CommandType(String commandText) {
        this.commandText = commandText;
    }

    public String getCommandText() {
        return commandText;
    }

    static CommandType fromText(String text) {
        for (CommandType commandType : values()) {
            if (commandType.commandText.equals(text)) {
                return commandType;
            }
        }
        throw new IllegalArgumentException("Неизвестная команда: " + text);
    }
}
