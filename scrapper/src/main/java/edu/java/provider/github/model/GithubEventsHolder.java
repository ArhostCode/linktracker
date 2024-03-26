package edu.java.provider.github.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import edu.java.provider.github.deserializers.GithubEventsHolderDeserializer;
import java.util.List;

@JsonDeserialize(using = GithubEventsHolderDeserializer.class)
public record GithubEventsHolder(
    List<GithubEventInfo> events
) {
    public static final GithubEventsHolder EMPTY = new GithubEventsHolder(null);
}
