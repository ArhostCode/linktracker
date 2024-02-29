package edu.java.provider.api;

import java.net.URL;
import java.time.OffsetDateTime;

public record LinkInformation(
    URL url,
    String title,
    String description,
    OffsetDateTime lastModified
) {
}
