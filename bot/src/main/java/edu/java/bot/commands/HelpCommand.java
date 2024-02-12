package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.util.TextResolver;
import java.util.List;
import java.util.Map;
import org.apache.commons.text.StringSubstitutor;

public class HelpCommand extends AbstractCommand {

    private final List<Command> commands;

    public HelpCommand(TextResolver textResolver, List<Command> commands) {
        super(textResolver);
        this.commands = commands;
    }

    @Override
    public String command() {
        return "/help";
    }

    @Override
    public String description() {
        return "command.help.description";
    }

    @Override
    public SendMessage handle(Update update) {
        StringBuilder message = new StringBuilder();
        message.append(textResolver.resolve("command.help.main"));
        String format = textResolver.resolve("command.help.format");
        commands.forEach(command -> message.append(
            StringSubstitutor.replace(format,
                Map.of(
                    "command", command.command(),
                    "description", textResolver.resolve(command.description())
                ), "%", "%"
            )));

        return new SendMessage(update.message().chat().id(), message.toString());
    }

}
