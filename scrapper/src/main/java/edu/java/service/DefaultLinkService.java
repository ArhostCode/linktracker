package edu.java.service;

import edu.java.dto.response.LinkResponse;
import edu.java.dto.response.ListLinksResponse;
import java.net.URI;
import java.util.List;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

@Component
@Log4j2
// Test service
public class DefaultLinkService implements LinkService {
    @Override
    public ListLinksResponse listLinks(Long tgChatId) {
        log.info("Listing links for chat {}", tgChatId);
        return new ListLinksResponse(List.of(new LinkResponse(1L, URI.create("http://localhost"))), 1);
    }

    @Override
    public LinkResponse addLink(URI link, Long tgChatId) {
        log.info("Adding id {} for chat {}", link, tgChatId);
        return new LinkResponse(1L, link);
    }

    @Override
    public LinkResponse removeLink(Long id, Long tgChatId) {
        log.info("Removing id {} for chat {}", id, tgChatId);
        return new LinkResponse(id, URI.create("http://localhost2"));
    }
}
