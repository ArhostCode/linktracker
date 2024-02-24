package edu.java.bot.service;

import edu.java.bot.model.Link;
import edu.java.bot.model.response.AddLinkToTrackingResponse;
import edu.java.bot.model.response.ListLinksResponse;
import edu.java.bot.model.response.RemoveLinkFromTrackingResponse;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Service;

// Test Service Implementation
@Service
public class InMemoryBotService implements BotService {

    private final Map<Long, List<Link>> usersLinks = new ConcurrentHashMap<>();

    @Override
    public void registerUser(String name, Long id) {
        usersLinks.put(id, new ArrayList<>());
    }

    @Override
    public AddLinkToTrackingResponse linkUrlToUser(String url, Long userId) {
        usersLinks.computeIfAbsent(userId, k -> new ArrayList<>()).add(new Link(1L, URI.create(url), ""));
        return new AddLinkToTrackingResponse(true, "");
    }

    @Override
    public RemoveLinkFromTrackingResponse unlinkUrlFromUser(Long linkId, Long userId) {
        usersLinks.computeIfAbsent(userId, k -> new ArrayList<>()).removeIf(link -> link.id().equals(linkId));
        return new RemoveLinkFromTrackingResponse(true, "");
    }

    @Override
    public ListLinksResponse listLinks(Long userId) {
        return new ListLinksResponse(usersLinks.get(userId));
    }
}
