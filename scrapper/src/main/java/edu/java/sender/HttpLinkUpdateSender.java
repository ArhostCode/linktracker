package edu.java.sender;

import edu.java.client.bot.BotClient;
import edu.java.client.bot.request.LinkUpdate;
import edu.java.service.LinkUpdateSender;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@ConditionalOnProperty(name = "app.use-queue", havingValue = "false")
public class HttpLinkUpdateSender implements LinkUpdateSender {
    private final BotClient botClient;

    @Override
    public void sendUpdate(LinkUpdate linkUpdate) {
        botClient.handleUpdates(linkUpdate);
    }
}
