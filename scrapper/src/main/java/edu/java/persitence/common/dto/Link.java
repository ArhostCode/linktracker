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

    public static Link create(String url, String description, OffsetDateTime updatedAt, OffsetDateTime lastCheckedAt) {
        return new Link(0, url, description, updatedAt, lastCheckedAt);
    }
}
