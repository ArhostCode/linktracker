package edu.java.provider.api;

import java.net.URI;
import java.util.List;

public record LinkInformation(
    URI url,
    String title,
    List<LinkUpdateEvent> events,
    String currentContext
) {

    public LinkInformation(URI url, String title, List<LinkUpdateEvent> events) {
        this(url, title, events, "");
    }
}
