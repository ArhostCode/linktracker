package edu.java.persitence.jpa.service;

import edu.java.exception.ChatAlreadyRegisteredException;
import edu.java.exception.ChatNotFoundException;
import edu.java.persitence.jpa.entity.LinkEntity;
import edu.java.persitence.jpa.entity.TgChatEntity;
import edu.java.persitence.jpa.repository.JpaChatRepository;
import edu.java.persitence.jpa.repository.JpaLinkRepository;
import edu.java.service.ChatService;
import java.util.ArrayList;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
public class JpaChatService implements ChatService {

    private final JpaChatRepository tgChatRepository;
    private final JpaLinkRepository linkRepository;

    @Override
    @Transactional
    public void registerChat(Long chatId) {
        if (tgChatRepository.findById(chatId).isPresent()) {
            throw new ChatAlreadyRegisteredException(chatId);
        }
        tgChatRepository.save(new TgChatEntity(chatId));
    }

    @Override
    @Transactional
    public void deleteChat(Long chatId) {
        var tgChat = tgChatRepository.findById(chatId).orElseThrow(() -> new ChatNotFoundException(chatId));
        var forRemove = new ArrayList<LinkEntity>();
        for (var link : tgChat.getLinks()) {
            tgChat.removeLink(link);
            if (link.getTgChats().isEmpty()) {
                forRemove.add(link);
            }
        }
        linkRepository.deleteAll(forRemove);
        tgChatRepository.delete(tgChat);
    }
}
