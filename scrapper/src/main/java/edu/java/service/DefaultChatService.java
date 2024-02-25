package edu.java.service;

import edu.java.exception.ChatNotFoundException;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

@Component
@Log4j2
public class DefaultChatService implements ChatService {
    @Override
    public void registerChat(Long chatId) {
        log.info("Registering chat {}", chatId);
    }

    @Override
    public void deleteChat(Long chatId) {
        log.info("Deleting chat {}", chatId);
//        throw new ChatNotFoundException(chatId);
    }
}
