package edu.java.configuration;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(name = "app.use-queue", havingValue = "true")
public class KafkaConfiguration {

    @Bean
    public NewTopic newTopic(ApplicationConfig config) {
        return new NewTopic(config.kafka().updatesTopicName(), 1, (short) 1);
    }
}
