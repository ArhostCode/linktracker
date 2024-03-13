package edu.java.bot.service;

import edu.java.bot.client.scrapper.dto.response.LinkResponse;
import edu.java.bot.client.scrapper.dto.response.ListLinksResponse;
import edu.java.bot.dto.OptionalAnswer;

public interface BotService {

    OptionalAnswer<Void> registerUser(Long id);

    OptionalAnswer<LinkResponse> linkUrlToUser(String url, Long userId);

    OptionalAnswer<LinkResponse> unlinkUrlFromUser(Long linkId, Long userId);

    OptionalAnswer<ListLinksResponse> listLinks(Long userId);

}
