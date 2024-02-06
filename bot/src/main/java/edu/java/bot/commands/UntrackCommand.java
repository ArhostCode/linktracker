package edu.java.bot.commands;

import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.model.response.RemoveLinkFromTrackingResponse;
import edu.java.bot.service.BotService;
import edu.java.bot.util.TextResolver;
import java.util.Map;

public class UntrackCommand extends TrackingCommandBase {

    public UntrackCommand(TextResolver textResolver, BotService botService) {
        super(textResolver, botService);
    }

    @Override
    public String command() {
        return "/untrack";
    }

    @Override
    public String description() {
        return "command.untrack.description";
    }

    @Override
    protected SendMessage performAction(Long chatId, String link) {
        RemoveLinkFromTrackingResponse response = botService.unlinkUrlFromUser(link, chatId);
        if (!response.success()) {
            return new SendMessage(
                chatId,
                textResolver.resolve(
                    "command.untrack.error",
                    Map.of(
                        "request_link", link,
                        "error_message", response.errorMessage()
                    )
                )
            );
        }
        return new SendMessage(
            chatId,
            textResolver.resolve(
                "command.untrack.success",
                Map.of("untracking_link", link)
            )
        );
    }
}
