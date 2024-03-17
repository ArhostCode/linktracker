package edu.java.persitence.common.dto;

import java.time.OffsetDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Link {
    private long id;
    private String url;
    private String description;
    private OffsetDateTime updatedAt;
    private OffsetDateTime lastCheckedAt;
    private String metaInformation;

    public static Link create(
        String url,
        String description,
        OffsetDateTime updatedAt,
        OffsetDateTime lastCheckedAt,
        String metaInformation
    ) {
        return new Link(0, url, description, updatedAt, lastCheckedAt, metaInformation);
    }

    public static Link create(
        String url,
        String description,
        OffsetDateTime updatedAt,
        OffsetDateTime lastCheckedAt
    ) {
        return create(url, description, updatedAt, lastCheckedAt, "");
    }
}
