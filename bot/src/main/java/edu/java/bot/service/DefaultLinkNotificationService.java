package edu.java.bot.service;

import edu.java.bot.model.Link;
import java.util.List;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

@Component
@Log4j2
public class DefaultLinkNotificationService implements LinkNotificationService {
    @Override
    public void notifyLinkUpdate(Link link, List<Long> chatIds) {
        chatIds.forEach(chatId -> {
            log.info("{} handle update {} link", chatId, link);
        });
    }
}
