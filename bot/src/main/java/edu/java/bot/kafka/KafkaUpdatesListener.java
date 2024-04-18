package edu.java.bot.kafka;

import edu.java.bot.dto.request.LinkUpdate;
import edu.java.bot.service.LinkNotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.retrytopic.DltStrategy;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KafkaUpdatesListener {
    private final LinkNotificationService linkNotificationService;

    @KafkaListener(topics = "${app.kafka.updates-topic-name}", groupId = "bot")
    @RetryableTopic(attempts = "1", dltStrategy = DltStrategy.FAIL_ON_ERROR, dltTopicSuffix = "_dlq")
    public void listenUpdate(LinkUpdate update) {
        linkNotificationService.notifyLinkUpdate(update);
    }
}
