package edu.java.persitence.common.service;

import edu.java.exception.ChatAlreadyRegisteredException;
import edu.java.persitence.common.repository.LinkRepository;
import edu.java.persitence.common.repository.TgChatLinkRepository;
import edu.java.persitence.common.repository.TgChatRepository;
import edu.java.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class DefaultChatService implements ChatService {

    private final TgChatRepository tgChatRepository;
    private final TgChatLinkRepository tgChatLinkRepository;
    private final LinkRepository linkRepository;

    @Override
    @Transactional
    public void registerChat(Long chatId) {
        if (tgChatRepository.existsByChatId(chatId)) {
            throw new ChatAlreadyRegisteredException(chatId);
        }
        tgChatRepository.add(chatId);
    }

    @Override
    @Transactional
    public void deleteChat(Long chatId) {
        var links = tgChatLinkRepository.findAllByChatId(chatId);
        tgChatLinkRepository.removeAllByChatId(chatId);
        links.forEach(link -> {
            if (tgChatLinkRepository.findAllByLinkId(link.getId()).isEmpty()) {
                linkRepository.remove(link.getId());
            }
        });
        tgChatRepository.remove(chatId);
    }
}
