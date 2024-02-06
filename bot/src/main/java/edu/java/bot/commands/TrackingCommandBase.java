package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.service.BotService;
import edu.java.bot.util.TextResolver;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class TrackingCommandBase extends AbstractCommand {
    protected final BotService botService;

    public TrackingCommandBase(TextResolver textResolver, BotService botService) {
        super(textResolver);
        this.botService = botService;
    }

    @Override
    public SendMessage handle(Update update) {
        Long chatId = update.message().chat().id();
        String userRequest = update.message().text();
        String link = parseLink(userRequest);
        if (link == null) {
            return new SendMessage(
                chatId,
                textResolver.resolve(
                    "command.track.invalid_command_usage",
                    Map.of("command", command())
                )
            );
        }
        if (!isLinkValid(link)) {
            return createInvalidLinkMessage(chatId, link);
        }
        return performAction(chatId, link);
    }

    protected abstract SendMessage performAction(Long chatId, String link);

    protected boolean isLinkValid(String link) {
        return link.startsWith("http://") || link.startsWith("https://");
    }

    protected String parseLink(String request) {
        String[] parts = request.split(" ");
        if (parts.length >= 2) {
            return Stream.of(parts).skip(1).collect(Collectors.joining(" "));
        }
        return null;
    }

    protected SendMessage createInvalidLinkMessage(Long chatId, String link) {
        return new SendMessage(
            chatId,
            textResolver.resolve(
                "command.track.invalid_request",
                Map.of("link", link)
            )
        );
    }
}
