package kz.azamatbakyt.BarberTelegramBot.service.bot.command;

import kz.azamatbakyt.BarberTelegramBot.service.bot.model.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class CommandsHandler {

    private final Map<CommandType, Command> commands;

    public CommandsHandler(StartCommand startCommand,
                           ServicesGroupsCommand servicesGroupsCommand,
                           HelpCommand helpCommand,
                           PortfolioCommand portfolioCommand,
                           ActiveAppointmentsCommand activeAppointmentsCommand,
                           ContactBarberCommand contactBarberCommand,
                           AppointmentManagementCommand appointmentManagementCommand,
                           NotFinishedAppointmentCommand notFinishedAppointmentCommand) {
        this.commands = Map.of(
                CommandType.START, startCommand,
                CommandType.SERVICE_GROUP, servicesGroupsCommand,
                CommandType.HELP,  helpCommand,
                CommandType.PORTFOLIO, portfolioCommand,
                CommandType.ACTIVE_APPOINTMENTS, activeAppointmentsCommand,
                CommandType.CONTACT_BARBER, contactBarberCommand,
                CommandType.MANAGE_APPOINTMENTS, appointmentManagementCommand,
                CommandType.NOT_FINISHED_APPOINTEMNT_COMMAND, notFinishedAppointmentCommand
        );
    }

    public List<Message> handleCommand(Update update) {
        String messageText = update.getMessage().getText();
        CommandType commandType = CommandType.fromText(messageText);
        long chatId = update.getMessage().getChatId();

        var commandHandler = commands.get(commandType);
        if (commandHandler != null) {
            return commandHandler.apply(update);
        } else {
            return Collections.singletonList(
                    new Message(new SendMessage(String.valueOf(chatId), "Неизвестная команда"))
            );
        }
    }

}
