package edu.java.bot.service;

import edu.java.bot.dto.request.LinkUpdate;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

@Component
@Log4j2
public class DefaultLinkNotificationService implements LinkNotificationService {
    @Override
    public void notifyLinkUpdate(LinkUpdate link) {
        link.tgChatIds().forEach(chatId -> log.info("{} handle update {} id", chatId, link));
    }
}
