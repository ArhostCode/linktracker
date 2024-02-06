package edu.java.bot.processor;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.commands.Command;
import java.util.List;

public interface UserMessagesProcessor {
    List<Command> commands();

    default void registerCommand(Command command) {
        commands().add(command);
    }

    SendMessage process(Update update);
}
