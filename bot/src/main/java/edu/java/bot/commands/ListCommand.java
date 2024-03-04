package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.client.scrapper.dto.response.LinkResponse;
import edu.java.bot.client.scrapper.dto.response.ListLinksResponse;
import edu.java.bot.service.BotService;
import edu.java.bot.util.TextResolver;

public class ListCommand extends AbstractCommand {

    private final BotService botService;

    public ListCommand(TextResolver textResolver, BotService botService) {
        super(textResolver);
        this.botService = botService;
    }

    @Override
    public String command() {
        return "/list";
    }

    @Override
    public String description() {
        return "command.list.description";
    }

    @Override
    public SendMessage handle(Update update) {
        ListLinksResponse response = botService.listLinks(update.message().chat().id()).answer();
        if (response.links() == null || response.links().isEmpty()) {
            return new SendMessage(update.message().chat().id(), textResolver.resolve("command.list.empty"));
        }
        StringBuilder message = new StringBuilder();
        message.append(textResolver.resolve("command.list.main"));
        int id = 1;
        for (LinkResponse link : response.links()) {
            message.append(id).append(". ").append(link.url()).append("\n");
            id++;
        }
        return new SendMessage(update.message().chat().id(), message.toString()).disableWebPagePreview(true);
    }
}
