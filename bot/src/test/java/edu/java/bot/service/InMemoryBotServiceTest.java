package edu.java.bot.service;

import java.util.UUID;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class InMemoryBotServiceTest {

    @DisplayName("Тестирование метода InMemoryBotService#registerUser через listLinks")
    @Test
    public void registerUser_shouldRegisterUser() {
        InMemoryBotService service = new InMemoryBotService();
        service.registerUser("user", 123L);
        Assertions.assertThat(service.listLinks(123L).links()).hasSize(0);
    }

    @DisplayName("Тестирование метода InMemoryBotService#linkUrlToUser через listLinks")
    @Test
    public void linkUrlToUser_shouldLinkUrlToUser() {
        InMemoryBotService service = new InMemoryBotService();
        service.linkUrlToUser("link", 123L);
        Assertions.assertThat(service.listLinks(123L).links())
            .hasSize(1)
            .element(0)
            .extracting("url")
            .isEqualTo("link");

    }

    @DisplayName("Тестирование метода InMemoryBotService#unlinkUrlFromUser через listLinks")
    @Test
    public void unlinkUrlFromUser_shouldUnlinkUrlFromUser() {
        InMemoryBotService service = new InMemoryBotService();
        service.linkUrlToUser("link", 123L);
        UUID linkId = service.listLinks(123L).links().getFirst().uuid();
        service.unlinkUrlFromUser(linkId, 123L);
        Assertions.assertThat(service.listLinks(123L).links())
            .hasSize(0);

    }

}
