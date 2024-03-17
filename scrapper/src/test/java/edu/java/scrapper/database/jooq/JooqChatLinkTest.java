package edu.java.scrapper.database.jooq;

import edu.java.persitence.common.dto.Link;
import edu.java.persitence.common.dto.TgChat;
import edu.java.persitence.jooq.repository.JooqChatLinkRepository;
import edu.java.persitence.jooq.repository.JooqChatRepository;
import edu.java.persitence.jooq.repository.JooqLinkRepository;
import edu.java.scrapper.IntegrationEnvironment;
import java.time.OffsetDateTime;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
public class JooqChatLinkTest extends IntegrationEnvironment {
    private static final OffsetDateTime MIN =
        OffsetDateTime.of(2024, 1, 1, 0, 0, 0, 0, OffsetDateTime.now().getOffset());

    private static final OffsetDateTime MAX =
        OffsetDateTime.of(2025, 1, 1, 0, 0, 0, 0, OffsetDateTime.now().getOffset());

    @Autowired
    private JooqChatRepository jooqChatRepository;
    @Autowired
    private JooqLinkRepository jooqLinkRepository;
    @Autowired
    private JooqChatLinkRepository jooqChatLinkRepository;

    @Test
    @Transactional
    @Rollback
    void findAllByChatIdShouldFindValueByChatId() {
        jooqChatRepository.add(123L);
        var id = jooqLinkRepository.add(Link.create("google.com", "Google", MIN, MAX));
        jooqChatLinkRepository.add(123L, id);
        Assertions.assertThat(jooqChatLinkRepository.findAllByChatId(123L)).contains(
            new Link(id, "google.com", "Google", MIN, MAX, "")
        );
    }

    @Test
    @Transactional
    @Rollback
    void findAllByLinkIdShouldFindValueByLinkId() {
        jooqChatRepository.add(123L);
        var id = jooqLinkRepository.add(Link.create("google.com", "Google", MIN, MAX));
        jooqChatLinkRepository.add(123L, id);
        Assertions.assertThat(jooqChatLinkRepository.findAllByLinkId(id)).contains(
            new TgChat(123L)
        );
    }

    @Test
    @Transactional
    @Rollback
    void removeShouldDeleteValueFromDatabase() {
        jooqChatRepository.add(123L);
        var id = jooqLinkRepository.add(Link.create("google.com", "Google", MIN, MAX));
        jooqChatLinkRepository.add(123L, id);
        jooqChatLinkRepository.remove(123L, id);
        Assertions.assertThat(jooqChatLinkRepository.findAllByChatId(123L)).doesNotContain(
            new Link(id, "google.com", "Google", MIN, MAX,"")
        );
    }

}
