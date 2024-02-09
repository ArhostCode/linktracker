package edu.java.bot.processor;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.commands.Command;
import edu.java.bot.util.TextResolver;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DefaultUserMessagesProcessor implements UserMessagesProcessor {
    private final TextResolver textResolver;
    private final List<Command> commands;

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
