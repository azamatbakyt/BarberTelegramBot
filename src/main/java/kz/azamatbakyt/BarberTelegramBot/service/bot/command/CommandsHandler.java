package kz.azamatbakyt.BarberTelegramBot.service.bot.command;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Map;

@Component
@Slf4j
public class CommandsHandler {

    private final Map<CommandType, Command> commands;

    public CommandsHandler(StartCommand startCommand,
                           ServicesGroupsCommand servicesGroupsCommand) {
        this.commands = Map.of(
                CommandType.START, startCommand,
                CommandType.SERVICE_GROUP, servicesGroupsCommand

        );
    }


    public SendMessage handleCommands(Update update) {
        String messageText = update.getMessage().getText();
        String command = messageText.split(" ")[0];
        CommandType commandType = CommandType.fromText(command);
        long chatId = update.getMessage().getChatId();

        var commandHandler = commands.get(commandType);
        if (commandHandler != null) {
            return commandHandler.apply(update);
        } else {
            return new SendMessage(String.valueOf(chatId), "Consts.UNKNOWN_COMMAND");
        }
    }

}
