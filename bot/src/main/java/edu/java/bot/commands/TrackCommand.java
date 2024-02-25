package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.service.BotService;
import edu.java.bot.util.TextResolver;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TrackCommand extends AbstractCommand {

    private final BotService botService;

    public TrackCommand(TextResolver textResolver, BotService botService) {
        super(textResolver);
        this.botService = botService;
    }

    @Override
    public String command() {
        return "/track";
    }

    @Override
    public String description() {
        return "command.track.description";
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

    public SendMessage performAction(Long chatId, String link) {
        var response = botService.linkUrlToUser(link, chatId);
        if (response.isError()) {
            return new SendMessage(
                chatId,
                textResolver.resolve(
                    "command.track.error",
                    Map.of(
                        "request_link", link,
                        "error_message", response.apiErrorResponse().description()
                    )
                )
            );
        }
        return new SendMessage(
            chatId,
            textResolver.resolve(
                "command.track.success",
                Map.of("tracking_link", link)
            )
        );
    }
}
