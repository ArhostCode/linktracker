package edu.java.persitence.jooq.repository;

import edu.java.persitence.common.dto.Link;
import edu.java.persitence.common.repository.LinkRepository;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import org.jooq.DSLContext;
import org.springframework.stereotype.Component;
import static edu.java.domain.jooq.Tables.LINK;

@Component
public class JooqLinkRepository implements LinkRepository {

    private final DSLContext dslContext;

    public JooqLinkRepository(DSLContext dslContext) {
        this.dslContext = dslContext;
    }

    @Override
    public List<Link> findAll() {
        return dslContext.select(LINK.fields())
            .from(LINK)
            .fetchInto(Link.class);
    }

    @Override
    public Long add(Link link) {
        return dslContext.insertInto(
                LINK,
                LINK.URL,
                LINK.DESCRIPTION,
                LINK.UPDATED_AT,
                LINK.LAST_CHECKED_AT,
                LINK.META_INFORMATION
            ).values(
                link.getUrl(),
                link.getDescription(),
                link.getUpdatedAt(),
                link.getLastCheckedAt(),
                link.getMetaInformation()
            ).returning(LINK.ID)
            .fetchOne(LINK.ID);
    }

    @Override
    public Long remove(long linkId) {
        return dslContext.delete(LINK)
            .where(LINK.ID.eq(linkId))
            .returning(LINK.ID)
            .fetchOne(LINK.ID);
    }

    @Override
    public Optional<Link> findById(long linkId) {
        return Optional.ofNullable(
            dslContext.select(LINK.fields())
                .from(LINK)
                .where(LINK.ID.eq(linkId))
                .fetchOneInto(Link.class)
        );
    }

    @Override
    public Optional<Link> findByUrl(String url) {
        return Optional.ofNullable(
            dslContext.select(LINK.fields())
                .from(LINK)
                .where(LINK.URL.eq(url))
                .fetchOneInto(Link.class)
        );
    }

    @Override
    public List<Link> findLinksCheckedAfter(Duration after, int limit) {
        return dslContext.select(LINK.fields())
            .from(LINK)
            .where(LINK.LAST_CHECKED_AT.lt(OffsetDateTime.now().minus(after)))
            .limit(limit)
            .fetchInto(Link.class);
    }

    @Override
    public void update(long id, OffsetDateTime lastModified, String metaInformation) {
        dslContext.update(LINK)
            .set(LINK.UPDATED_AT, lastModified)
            .set(LINK.LAST_CHECKED_AT, OffsetDateTime.now())
            .set(LINK.META_INFORMATION, metaInformation)
            .where(LINK.ID.eq(id))
            .execute();
    }

    @Override
    public void checkNow(long id) {
        dslContext.update(LINK)
            .set(LINK.LAST_CHECKED_AT, OffsetDateTime.now())
            .where(LINK.ID.eq(id))
            .execute();
    }
}
