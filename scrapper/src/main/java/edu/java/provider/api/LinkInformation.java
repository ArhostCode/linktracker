package edu.java.provider.api;

import java.net.URI;
import java.time.OffsetDateTime;

public record LinkInformation(
    URI url,
    String title,
    String description,
    OffsetDateTime lastModified
) {
}
