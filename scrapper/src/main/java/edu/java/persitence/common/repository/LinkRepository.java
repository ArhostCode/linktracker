package edu.java.persitence.common.repository;

import edu.java.persitence.common.dto.Link;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.List;

public interface LinkRepository {

    List<Link> findAll();

    Long add(Link link);

    Long remove(long linkId);

    Link findById(long linkId);

    Link findByUrl(String url);

    List<Link> findOldLinks(Duration after, int limit);

    void update(long id, OffsetDateTime lastModified);
}
