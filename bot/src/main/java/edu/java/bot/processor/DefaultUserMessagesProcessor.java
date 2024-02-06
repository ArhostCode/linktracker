package edu.java.bot.processor;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.commands.Command;
import edu.java.bot.commands.HelpCommand;
import edu.java.bot.commands.ListCommand;
import edu.java.bot.commands.StartCommand;
import edu.java.bot.commands.TrackCommand;
import edu.java.bot.commands.UntrackCommand;
import edu.java.bot.executor.RequestExecutor;
import edu.java.bot.service.BotService;
import edu.java.bot.util.TextResolver;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class DefaultUserMessagesProcessor implements UserMessagesProcessor {

    private final RequestExecutor requestExecutor;
    private final TextResolver textResolver;
    private final BotService botService;
    private final ArrayList<Command> commands = new ArrayList<>();

    public DefaultUserMessagesProcessor(
        RequestExecutor requestExecutor,
        TextResolver textResolver,
        BotService botService
    ) {
        this.requestExecutor = requestExecutor;
        this.textResolver = textResolver;
        this.botService = botService;
        registerCommand(new HelpCommand(textResolver, commands));
        registerCommand(new StartCommand(
            textResolver,
            requestExecutor,
            commands
        ));
        registerCommand(new TrackCommand(textResolver, botService));
        registerCommand(new UntrackCommand(textResolver, botService));
        registerCommand(new ListCommand(textResolver, botService));
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
        if (update.message() == null) {
            return null;
        }
        String text = update.message().text();
        return new SendMessage(
            update.message().chat().id(),
            textResolver.resolve(
                "message.unknown_command",
                Map.of("command", text != null ? text : "None")
            )
        );
    }

}
