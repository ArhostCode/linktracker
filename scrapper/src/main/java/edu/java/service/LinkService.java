package edu.java.service;

import edu.java.dto.response.LinkResponse;
import edu.java.dto.response.ListLinksResponse;
import java.net.URI;

public interface LinkService {

    ListLinksResponse listLinks(Long tgChatId);

    LinkResponse addLink(URI link, Long tgChatId);

    LinkResponse removeLink(Long id, Long tgChatId);
}
