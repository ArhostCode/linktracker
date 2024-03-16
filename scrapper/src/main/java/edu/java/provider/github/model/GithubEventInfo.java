package edu.java.provider.github.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import edu.java.provider.github.deserializers.GithubEventInfoDeserializer;
import java.time.OffsetDateTime;
import java.util.Map;

@JsonDeserialize(using = GithubEventInfoDeserializer.class)
public record GithubEventInfo(
    @JsonProperty("created_at")
    OffsetDateTime lastModified,
    String type,
    RepositoryInfo repo,
    Map<String, String> additionalData
) {
    public record RepositoryInfo(String name) {
    }

}
