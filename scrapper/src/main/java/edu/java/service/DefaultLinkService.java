package edu.java.service;

import edu.java.model.response.LinkResponse;
import edu.java.model.response.ListLinksResponse;
import java.net.URI;
import org.springframework.stereotype.Component;

@Component
public class DefaultLinkService implements LinkService {
    @Override
    public ListLinksResponse listLinks(Long tgChatId) {
        return null;
    }

    @Override
    public LinkResponse addLink(URI link, Long tgChatId) {
        return null;
    }

    @Override
    public LinkResponse removeLink(URI link, Long tgChatId) {
        return null;
    }
}
