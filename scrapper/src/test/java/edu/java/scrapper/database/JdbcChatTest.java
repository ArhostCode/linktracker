package edu.java.scrapper.database;

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
        var dbChat = jdbcChatRepository.findAll().get(0);
        Assertions.assertThat(dbChat.getId()).isEqualTo(123L);
    }

}
