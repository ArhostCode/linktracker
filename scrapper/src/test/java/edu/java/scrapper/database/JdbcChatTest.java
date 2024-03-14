package edu.java.scrapper.database;

import edu.java.persitence.common.dto.TgChat;
import edu.java.persitence.jdbc.repository.JdbcChatRepository;
import edu.java.scrapper.IntegrationEnvironment;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
public class JdbcChatTest extends IntegrationEnvironment {

    @Autowired
    private JdbcChatRepository jdbcChatRepository;

    @Test
    @Transactional
    @Rollback
    void addShouldInsertChatInDatabase() {
        jdbcChatRepository.add(123L);
        Assertions.assertThat(jdbcChatRepository.findAll()).contains(new TgChat(123L));
    }

    @Test
    @Transactional
    @Rollback
    void removeShouldDeleteChatFromDatabase() {
        jdbcChatRepository.add(123L);
        jdbcChatRepository.remove(123L);
        Assertions.assertThat(jdbcChatRepository.findAll()).doesNotContain(new TgChat(123L));
    }

}
