package edu.java.provider.github.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;
import java.util.Map;

public record GithubEventInfo(
    @JsonProperty("created_at")
    OffsetDateTime lastModified,
    String type,
    RepositoryInfo repo,
    EventPayload payload,
    Map<String, String> additionalData
) {
    public record RepositoryInfo(String name) {
    }

    public record EventPayload(
        String size,
        String ref,
        IssueCommentEventPayload issue,
        @JsonProperty("pull_request") PullRequestEventPayload pullRequest,
        @JsonProperty("ref_type") String refType
    ) {
    }

    public record IssueCommentEventPayload(
        String title
    ) {
    }

    public record PullRequestEventPayload(
        String title
    ) {
    }

}
