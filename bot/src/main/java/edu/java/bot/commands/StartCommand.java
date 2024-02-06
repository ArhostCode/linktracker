package edu.java.bot.commands;

import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SetMyCommands;
import edu.java.bot.executor.RequestExecutor;
import edu.java.bot.service.BotService;
import edu.java.bot.util.TextResolver;
import java.util.List;
import java.util.Map;

public class StartCommand extends AbstractCommand {

    private final RequestExecutor requestExecutor;
    private final List<Command> commands;
    private final BotService botService;

    public StartCommand(
        TextResolver textResolver,
        RequestExecutor requestExecutor,
        BotService botService,
        List<Command> commands
    ) {
        super(textResolver);
        this.requestExecutor = requestExecutor;
        this.commands = commands;
        this.botService = botService;
    }

    @Override
    public String command() {
        return "/start";
    }

    @Override
    public String description() {
        return "command.start.description";
    }

    @Override
    public SendMessage handle(Update update) {
        requestExecutor.execute(
            new SetMyCommands(commands.stream()
                .map(Command::toApiCommand)
                .toList()
                .toArray(new BotCommand[0])
            )
        );
        botService.registerUser(update.message().chat().firstName(), update.message().chat().id());
        return new SendMessage(
            update.message().chat().id(),
            textResolver.resolve(
                "command.start.hello_message",
                Map.of("user_name", update.message().chat().firstName())
            )
        );
    }
}
