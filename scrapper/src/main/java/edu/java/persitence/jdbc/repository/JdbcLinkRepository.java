package edu.java.persitence.jdbc.repository;

import edu.java.persitence.common.dto.Link;
import edu.java.persitence.common.repository.LinkRepository;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@SuppressWarnings("checkstyle:MultipleStringLiterals")
public class JdbcLinkRepository implements LinkRepository {

    private final JdbcClient client;

    @Override
    public List<Link> findAll() {
        return client.sql("SELECT * FROM link")
            .query(Link.class)
            .list();
    }

    @Override
    public Long add(Link link) {
        return client.sql("""
                INSERT INTO
                  link(url, description, updated_at, last_checked_at)
                VALUES
                  (:link, :description, :updated_at, :last_checked_at)
                ON CONFLICT (url)
                DO UPDATE SET updated_at = :updated_at, last_checked_at = :last_checked_at
                RETURNING id""")
            .param("link", link.getUrl())
            .param("description", link.getDescription())
            .param("updated_at", link.getUpdatedAt())
            .param("last_checked_at", link.getLastCheckedAt())
            .query(Long.class)
            .single();
    }

    @Override
    public Long remove(long linkId) {
        return client.sql("DELETE FROM link WHERE id = :id RETURNING id")
            .param("id", linkId)
            .query(Long.class)
            .single();
    }

    @Override
    public Optional<Link> findById(long linkId) {
        return client.sql("SELECT * FROM link WHERE id = :id")
            .param("id", linkId)
            .query(Link.class)
            .optional();
    }

    @Override
    public Optional<Link> findByUrl(String url) {
        return client.sql("SELECT * FROM link WHERE url = :url")
            .param("url", url)
            .query(Link.class)
            .optional();
    }

    @Override
    public List<Link> findLinksCheckedAfter(Duration after, int limit) {
        return client.sql("""
                SELECT
                  *
                FROM
                  link
                WHERE
                  last_checked_at < :last_checked_at
                ORDER BY last_checked_at
                LIMIT :limit
                """
            )
            .param("last_checked_at", OffsetDateTime.now().minus(after))
            .param("limit", limit)
            .query(Link.class)
            .list();
    }

    @Override
    public void update(long id, OffsetDateTime lastModified) {
        client.sql("UPDATE link SET last_checked_at = :last_checked_at, updated_at = :updated_at WHERE id = :id")
            .param("last_checked_at", OffsetDateTime.now())
            .param("updated_at", lastModified)
            .param("id", id)
            .update();
    }
}
