package edu.java.bot.service;

import edu.java.bot.client.scrapper.dto.response.LinkResponse;
import edu.java.bot.client.scrapper.dto.response.ListLinksResponse;
import edu.java.bot.dto.OptionalAnswer;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

// Test Service Implementation
//@Service
public class InMemoryBotService implements BotService {

    private final Map<Long, List<LinkResponse>> usersLinks = new ConcurrentHashMap<>();

    @Override
    public void registerUser(Long id) {
        usersLinks.put(id, new ArrayList<>());
    }

    @Override
    public OptionalAnswer<LinkResponse> linkUrlToUser(String url, Long userId) {
        usersLinks.computeIfAbsent(userId, k -> new ArrayList<>()).add(new LinkResponse(1L, URI.create(url)));
        return OptionalAnswer.of(new LinkResponse(1L, URI.create(url)));
    }

    @Override
    public OptionalAnswer<LinkResponse> unlinkUrlFromUser(Long linkId, Long userId) {
        usersLinks.computeIfAbsent(userId, k -> new ArrayList<>()).removeIf(link -> link.id().equals(linkId));
        return OptionalAnswer.of(new LinkResponse(1L, URI.create("https://flame.ardyc.ru/generate")));
    }

    @Override
    public OptionalAnswer<ListLinksResponse> listLinks(Long userId) {
        return OptionalAnswer.of(new ListLinksResponse(usersLinks.get(userId), usersLinks.get(userId).size()));
    }
}
