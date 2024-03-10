package edu.java.persitence.jdbc.repository;

import edu.java.persitence.common.dto.Link;
import edu.java.persitence.common.repository.LinkRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
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
                          (?, ?, ?, ?) ON CONFLICT DO NOTHING RETURNING id""")
            .params(List.of(link.getUrl(), link.getDescription(), link.getUpdatedAt(), link.getLastCheckedAt()))
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
    public Link findById(long linkId) {
        return client.sql("SELECT * FROM link WHERE id = :id")
            .param("id", linkId)
            .query(Link.class)
            .single();
    }

    @Override
    public Link findByUrl(String url) {
        return client.sql("SELECT * FROM link WHERE url = :url")
            .param("url", url)
            .query(Link.class)
            .single();
    }
}
