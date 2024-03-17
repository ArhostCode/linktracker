package edu.java.scrapper.database.jooq;

import edu.java.persitence.common.dto.TgChat;
import edu.java.persitence.jooq.repository.JooqChatRepository;
import edu.java.scrapper.IntegrationEnvironment;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
public class JooqChatTest extends IntegrationEnvironment {

    @Autowired
    private JooqChatRepository jooqChatRepository;

    @Test
    @Transactional
    @Rollback
    void addShouldInsertChatInDatabase() {
        jooqChatRepository.add(123L);
        Assertions.assertThat(jooqChatRepository.findAll()).contains(new TgChat(123L));
    }

    @Test
    @Transactional
    @Rollback
    void removeShouldDeleteChatFromDatabase() {
        jooqChatRepository.add(123L);
        jooqChatRepository.remove(123L);
        Assertions.assertThat(jooqChatRepository.findAll()).doesNotContain(new TgChat(123L));
    }

}
