package edu.java.scrapper.database.jdbc;

import edu.java.persitence.common.dto.Link;
import edu.java.persitence.jdbc.repository.JdbcLinkRepository;
import edu.java.scrapper.IntegrationEnvironment;
import java.time.Duration;
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
        Assertions.assertThat(dbLink.get())
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
        jdbcLinkRepository.remove(dbLink.get().getId());
        Assertions.assertThat(jdbcLinkRepository.findByUrl(link.getUrl()).isEmpty()).isTrue();
    }

    @Test
    @Transactional
    @Rollback
    void getAllShouldReturnAllLinks() {
        var link = Link.create("google.com", "Google", OffsetDateTime.MIN, OffsetDateTime.MAX);
        var link2 = Link.create("yandex.ru", "Yandex", OffsetDateTime.MIN, OffsetDateTime.MAX);
        jdbcLinkRepository.add(link);
        jdbcLinkRepository.add(link2);
        var dbLinks = jdbcLinkRepository.findAll();
        Assertions.assertThat(dbLinks).map(Link::getUrl).contains(link.getUrl(), link2.getUrl());
    }

    @Test
    @Transactional
    @Rollback
    void findLinksCheckedAfterShouldReturnOldLinks() {
        var link = Link.create("google.com", "Google", OffsetDateTime.MIN, OffsetDateTime.now());
        var link2 =
            Link.create("yandex.ru", "Yandex", OffsetDateTime.MIN, OffsetDateTime.now().minus(Duration.ofDays(1)));
        jdbcLinkRepository.add(link);
        jdbcLinkRepository.add(link2);
        var dbLinks = jdbcLinkRepository.findLinksCheckedAfter(Duration.ofMinutes(10), 100);
        Assertions.assertThat(dbLinks).map(Link::getUrl).contains(link2.getUrl());
    }
}
