package edu.java.scrapper.database;

import edu.java.persitence.common.dto.Link;
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
public class JdbcLinkTest extends IntegrationEnvironment {

    @Autowired
    private JdbcLinkRepository jdbcLinkRepository;

    @Test
    @Transactional
    @Rollback
    void addShouldInsertLinkInDatabase() {
        var link = Link.create("google.com", "Google", OffsetDateTime.MIN, OffsetDateTime.MAX);
        var id = jdbcLinkRepository.add(link);
        var dbLink = jdbcLinkRepository.findById(id);
        Assertions.assertThat(dbLink)
            .extracting(Link::getUrl, Link::getDescription)
            .contains(link.getUrl(), link.getDescription());
    }

    @Test
    @Transactional
    @Rollback
    void removeShouldDeleteLinkFromDatabase() {
        var link = Link.create("google.com", "Google", OffsetDateTime.MIN, OffsetDateTime.MAX);
        jdbcLinkRepository.add(link);
        var dbLink = jdbcLinkRepository.findByUrl(link.getUrl());
        jdbcLinkRepository.remove(dbLink.getId());
        Assertions.assertThatThrownBy(() -> jdbcLinkRepository.findByUrl(link.getUrl()));
    }
}
