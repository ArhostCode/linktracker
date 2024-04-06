package edu.java.bot.kafka;

import edu.java.bot.configuration.ApplicationConfig;
import edu.java.bot.dto.request.LinkUpdate;
import edu.java.bot.service.LinkNotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KafkaUpdatesListener {
    private final LinkNotificationService linkNotificationService;
    private final KafkaTemplate<String, LinkUpdate> kafkaTemplate;
    private final ApplicationConfig config;

    @KafkaListener(topics = "${app.kafka.updates-topic-name}", groupId = "bot")
    public void listenUpdate(LinkUpdate update) {
        try {
            linkNotificationService.notifyLinkUpdate(update);
        } catch (Exception e) {
            kafkaTemplate.send(config.kafka().updatesTopicName() + "_dlq", update);
        }
    }
}
