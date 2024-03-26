package edu.java.provider.api;

import java.time.OffsetDateTime;
import java.util.Map;
import lombok.Builder;

@Builder
public record LinkUpdateEvent(
    String type,
    OffsetDateTime lastModified,
    Map<String, String> additionalData
) {
}
