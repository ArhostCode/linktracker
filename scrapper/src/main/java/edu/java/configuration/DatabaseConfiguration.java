package edu.java.configuration;

import edu.java.persitence.common.repository.LinkRepository;
import edu.java.persitence.common.repository.TgChatLinkRepository;
import edu.java.persitence.common.repository.TgChatRepository;
import edu.java.persitence.common.service.DefaultChatService;
import edu.java.service.ChatService;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DatabaseConfiguration {

    public ChatService chatService(
        TgChatRepository tgChatRepository,
        TgChatLinkRepository tgChatLinkRepository,
        LinkRepository linkRepository
    ) {
        return new DefaultChatService(tgChatRepository, tgChatLinkRepository, linkRepository);
    }

}
