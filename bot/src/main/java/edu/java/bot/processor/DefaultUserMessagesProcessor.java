package edu.java.bot.processor;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.executor.RequestExecutor;
import edu.java.bot.model.Command;
import edu.java.bot.commands.HelpCommand;
import edu.java.bot.commands.StartCommand;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class DefaultUserMessagesProcessor implements UserMessagesProcessor {

    private RequestExecutor requestExecutor;
    private ArrayList<Command> commands = new ArrayList<>();

    public DefaultUserMessagesProcessor(RequestExecutor requestExecutor) {
        this.requestExecutor = requestExecutor;
        registerCommand(new HelpCommand());
        registerCommand(new StartCommand(requestExecutor, commands.stream().map(Command::toApiCommand).toList()));
    }

    @Override
    public List<Command> commands() {
        return commands;
    }

    @Override
    public SendMessage process(Update update) {
        for (Command command : commands) {
            if (command.supports(update)) {
                return command.handle(update);
            }
        }
        return new SendMessage(update.message().chat().id(), "Команда не найдена");
    }

}
