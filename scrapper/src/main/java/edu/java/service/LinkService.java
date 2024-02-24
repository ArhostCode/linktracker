package edu.java.service;

import edu.java.model.response.LinkResponse;
import edu.java.model.response.ListLinksResponse;
import java.net.URI;

public interface LinkService {

    ListLinksResponse listLinks(Long tgChatId);

    LinkResponse addLink(URI link, Long tgChatId);

    LinkResponse removeLink(URI link, Long tgChatId);
}
