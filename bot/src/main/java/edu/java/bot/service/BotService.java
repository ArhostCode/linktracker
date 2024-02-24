package edu.java.bot.service;

import edu.java.bot.dto.response.AddLinkToTrackingResponse;
import edu.java.bot.dto.response.ListLinksResponse;
import edu.java.bot.dto.response.RemoveLinkFromTrackingResponse;

public interface BotService {

    void registerUser(String name, Long id);

    AddLinkToTrackingResponse linkUrlToUser(String url, Long userId);

    RemoveLinkFromTrackingResponse unlinkUrlFromUser(Long linkId, Long userId);

    ListLinksResponse listLinks(Long userId);

}
