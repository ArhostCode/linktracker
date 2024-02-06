package edu.java.bot.service;

import edu.java.bot.model.response.AddLinkToTrackingResponse;
import edu.java.bot.model.response.ListLinksResponse;
import edu.java.bot.model.response.RemoveLinkFromTrackingResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Service;

@Service
public class InMemoryBotService implements BotService {

    private final Map<Long, List<String>> usersLinks = new ConcurrentHashMap<>();

    @Override
    public void registerUser(String name, Long id) {
        usersLinks.put(id, new ArrayList<>());
    }

    @Override
    public AddLinkToTrackingResponse linkUrlToUser(String url, Long userId) {
        usersLinks.computeIfAbsent(userId, k -> new ArrayList<>()).add(url);
        return new AddLinkToTrackingResponse(true, "");
    }

    @Override
    public RemoveLinkFromTrackingResponse unlinkUrlFromUser(String url, Long userId) {
        usersLinks.computeIfAbsent(userId, k -> new ArrayList<>()).remove(url);
        return new RemoveLinkFromTrackingResponse(true, "");
    }

    @Override
    public ListLinksResponse listLinks(Long userId) {
        return new ListLinksResponse(usersLinks.get(userId));
    }
}
