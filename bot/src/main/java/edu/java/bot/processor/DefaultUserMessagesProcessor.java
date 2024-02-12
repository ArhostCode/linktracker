package edu.java.bot.processor;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.commands.Command;
import edu.java.bot.commands.HelpCommand;
import edu.java.bot.commands.ListCommand;
import edu.java.bot.commands.StartCommand;
import edu.java.bot.commands.TrackCommand;
import edu.java.bot.commands.UntrackCommand;
import edu.java.bot.service.BotService;
import edu.java.bot.util.TextResolver;
import jakarta.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class DefaultUserMessagesProcessor implements UserMessagesProcessor {
    private final TextResolver textResolver;
    private final BotService botService;
    private final List<Command> commands = new ArrayList<>();

    @PostConstruct
    public void initialize() {
        commands.add(new HelpCommand(textResolver, commands));
        commands.add(new StartCommand(textResolver, botService));
        commands.add(new TrackCommand(textResolver, botService));
        commands.add(new UntrackCommand(textResolver, botService));
        commands.add(new ListCommand(textResolver, botService));
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
