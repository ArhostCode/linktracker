package edu.java.bot.commands;

import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SetMyCommands;
import edu.java.bot.executor.RequestExecutor;
import edu.java.bot.model.Command;
import java.util.List;

public class StartCommand implements Command {

    private RequestExecutor requestExecutor;
    private List<BotCommand> commands;

    public StartCommand(RequestExecutor requestExecutor, List<BotCommand> commands) {
        this.requestExecutor = requestExecutor;
        this.commands = commands;
    }

    @Override
    public String command() {
        return "/start";
    }

    @Override
    public String description() {
        return null;
    }

    @Override
    public SendMessage handle(Update update) {
        requestExecutor.execute(new SetMyCommands(commands.toArray(new BotCommand[0])));
        return new SendMessage(update.message().chat().id(), "Начало работы");
    }
}
