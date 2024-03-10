package edu.java.persitence.common.service;

import edu.java.dto.response.LinkResponse;
import edu.java.dto.response.ListLinksResponse;
import edu.java.persitence.common.dto.Link;
import edu.java.persitence.common.repository.LinkRepository;
import edu.java.persitence.common.repository.TgChatLinkRepository;
import edu.java.service.LinkService;
import java.net.URI;
import java.time.OffsetDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
public class AbstractLinkService implements LinkService {

    private final LinkRepository linkRepository;
    private final TgChatLinkRepository tgChatLinkRepository;

    @Override
    @Transactional
    public ListLinksResponse listLinks(Long tgChatId) {
        var links = tgChatLinkRepository.findAllByChatId(tgChatId);
        var linkResponses = links.stream()
            .map(link -> new LinkResponse(link.getId(), URI.create(link.getUrl())))
            .toList();
        return new ListLinksResponse(linkResponses, linkResponses.size());
    }

    @Override
    @Transactional
    public LinkResponse addLink(URI link, Long tgChatId) {
        var id = linkRepository.add(Link.create(link.toString(), "", OffsetDateTime.MIN, OffsetDateTime.MAX));
        tgChatLinkRepository.add(tgChatId, id);
        return new LinkResponse(id, link);
    }

    @Override
    @Transactional
    public LinkResponse removeLink(Long id, Long tgChatId) {
        Link link = linkRepository.findById(id);
        tgChatLinkRepository.remove(tgChatId, id);
        if (tgChatLinkRepository.findAllByLinkId(id).isEmpty()) {
            linkRepository.remove(id);
        }
        return new LinkResponse(link.getId(), URI.create(link.getUrl()));
    }
}
