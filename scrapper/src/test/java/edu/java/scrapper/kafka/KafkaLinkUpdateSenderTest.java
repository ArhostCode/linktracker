package edu.java.scrapper.kafka;

import edu.java.client.bot.request.LinkUpdate;
import edu.java.configuration.ApplicationConfig;
import edu.java.scrapper.IntegrationEnvironment;
import edu.java.sender.KafkaLinkUpdateSender;
import java.net.URI;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import static org.awaitility.Awaitility.await;

@SpringBootTest
public class KafkaLinkUpdateSenderTest extends IntegrationEnvironment {

    @Autowired
    private KafkaTemplate<String, LinkUpdate> kafkaTemplate;

    @Autowired
    private ApplicationConfig config;

    @Test
    @DisplayName("Тестирование KafkaLinkUpdateSender#sendUpdate")
    public void sendUpdateShouldSendUpdateInKafka() {
        var linkUpdateSender = new KafkaLinkUpdateSender(kafkaTemplate, config);
        var linkUpdate = new LinkUpdate(
            1L,
            URI.create("http://test.com"),
            "test",
            List.of(1L),
            Map.of()
        );
        var kafkaConsumer = new KafkaConsumer<String, LinkUpdate>(
            Map.of(
                "bootstrap.servers", KAFKA.getBootstrapServers(),
                "group.id", "scrapper",
                "key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer",
                "value.deserializer", "org.springframework.kafka.support.serializer.JsonDeserializer",
                "properties.spring.json.trusted.packages", "*",
                "spring.json.value.default.type", "edu.java.client.bot.request.LinkUpdate",
                "auto.offset.reset", "earliest"
            )
        );
        kafkaConsumer.subscribe(List.of(config.kafka().updatesTopicName()));
        linkUpdateSender.sendUpdate(linkUpdate);
        await()
            .pollInterval(Duration.ofMillis(100))
            .atMost(Duration.ofSeconds(5))
            .untilAsserted(() -> {
                var records = kafkaConsumer.poll(Duration.ofMillis(100));
                Assertions.assertThat(records).hasSize(1);
                Assertions.assertThat(records.iterator().next().value()).isEqualTo(linkUpdate);
            });
    }

}
