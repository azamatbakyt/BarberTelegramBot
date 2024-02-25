package kz.azamatbakyt.BarberTelegramBot.service.bot.command;

public enum CommandType {

    START("/start"),
    SERVICE_GROUP("Услуги"),
    HELP("Help"),
    PORTFOLIO("Мои работы"),
    ACTIVE_APPOINTMENTS("Мои активные записи"),
    CONTACT_BARBER("Связаться с барбером"),
    MANAGE_APPOINTMENTS("Управление бронированием")
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
