package edu.java.bot.service;

import edu.java.bot.model.response.AddLinkToTrackingResponse;
import edu.java.bot.model.response.ListLinksResponse;
import edu.java.bot.model.response.RemoveLinkFromTrackingResponse;

public interface BotService {

    void registerUser(String name, Long id);

    AddLinkToTrackingResponse linkUrlToUser(String url, Long userId);

    RemoveLinkFromTrackingResponse unlinkUrlFromUser(String url, Long userId);

    ListLinksResponse listLinks(Long userId);

}
