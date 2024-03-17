package edu.java.persitence.common.repository;

import edu.java.persitence.common.dto.Link;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

public interface LinkRepository {

    List<Link> findAll();

    Long add(Link link);

    Long remove(long linkId);

    Optional<Link> findById(long linkId);

    Optional<Link> findByUrl(String url);

    List<Link> findLinksCheckedAfter(Duration after, int limit);

    void update(long id, OffsetDateTime lastModified);

    void checkNow(long id);
}
