package edu.java.bot.service;

import edu.java.bot.model.response.AddLinkToTrackingResponse;
import edu.java.bot.model.response.ListLinksResponse;
import edu.java.bot.model.response.RemoveLinkFromTrackingResponse;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class DefaultBotService implements BotService {
    @Override
    public void registerUser(String name, Long id) {

    }

    @Override
    public AddLinkToTrackingResponse linkUrlToUser(String url, Long userId) {
        return new AddLinkToTrackingResponse(false, "Ссылка не может быть добавлена по причине \"На дауничах\"");
    }

    @Override
    public RemoveLinkFromTrackingResponse unlinkUrlFromUser(String url, Long userId) {
        return new RemoveLinkFromTrackingResponse(false, "Ссылка не может быть удалена по причине \"На дауничах\"");
    }

    @Override
    public ListLinksResponse listLinks(Long userId) {
        return new ListLinksResponse(List.of("Ссылка в Москву", "Ссылка в Санкт-Петербург"));
    }
}
