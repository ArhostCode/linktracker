package edu.java.scrapper.database.jdbc;

import edu.java.domain.dto.Link;
import edu.java.domain.dto.TgChat;
import edu.java.persitence.jdbc.repository.JdbcChatLinkRepository;
import edu.java.persitence.jdbc.repository.JdbcChatRepository;
import edu.java.persitence.jdbc.repository.JdbcLinkRepository;
import edu.java.scrapper.IntegrationEnvironment;
import java.time.OffsetDateTime;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
public class JdbcChatLinkTest extends IntegrationEnvironment {

    @Autowired
    private JdbcChatRepository jdbcChatRepository;
    @Autowired
    private JdbcLinkRepository jdbcLinkRepository;
    @Autowired
    private JdbcChatLinkRepository jdbcChatLinkRepository;

    @Test
    @Transactional
    @Rollback
    void findAllByChatIdShouldFindValueByChatId() {
        jdbcChatRepository.add(123L);
        var id = jdbcLinkRepository.add(Link.create("google.com", "Google", OffsetDateTime.MIN, OffsetDateTime.MAX));
        jdbcChatLinkRepository.add(123L, id);
        Assertions.assertThat(jdbcChatLinkRepository.findAllByChatId(123L)).contains(
            new Link(id, "google.com", "Google", OffsetDateTime.MIN, OffsetDateTime.MAX, "")
        );
    }

    @Test
    @Transactional
    @Rollback
    void findAllByLinkIdShouldFindValueByLinkId() {
        jdbcChatRepository.add(123L);
        var id = jdbcLinkRepository.add(Link.create("google.com", "Google", OffsetDateTime.MIN, OffsetDateTime.MAX));
        jdbcChatLinkRepository.add(123L, id);
        Assertions.assertThat(jdbcChatLinkRepository.findAllByLinkId(id)).contains(
            new TgChat(123L)
        );
    }

    @Test
    @Transactional
    @Rollback
    void removeShouldDeleteValueFromDatabase() {
        jdbcChatRepository.add(123L);
        var id = jdbcLinkRepository.add(Link.create("google.com", "Google", OffsetDateTime.MIN, OffsetDateTime.MAX));
        jdbcChatLinkRepository.add(123L, id);
        jdbcChatLinkRepository.remove(123L, id);
        Assertions.assertThat(jdbcChatLinkRepository.findAllByChatId(123L)).doesNotContain(
            new Link(id, "google.com", "Google", OffsetDateTime.MIN, OffsetDateTime.MAX, "")
        );
    }

}
