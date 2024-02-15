package edu.java.provider.github;

import com.fasterxml.jackson.annotation.JsonProperty;
import edu.java.provider.api.InformationProvider;
import edu.java.provider.api.LinkInformation;
import java.net.URL;
import java.time.OffsetDateTime;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public class GithubInformationProvider extends InformationProvider {

    private static final String API_URL = "https://api.github.com";
    private static final String GITHUB_HOST = "github.com";
    private final WebClient client;

    public GithubInformationProvider(String apiUrl) {
        client = WebClient.builder()
            .baseUrl(apiUrl)
            .build();
    }

    public GithubInformationProvider() {
        this(API_URL);
    }

    @Override
    protected boolean isSupported(URL url) {
        return url.getHost().equals(GITHUB_HOST);
    }

    @Override
    protected LinkInformation fetchInformation(URL url) {
        var info = client.get()
            .uri("/repos" + url.getPath())
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .onStatus(HttpStatusCode::isError, response -> Mono.empty())
            .bodyToMono(GithubRepoInfoResponse.class)
            .block();
        if (info != null && info != GithubRepoInfoResponse.EMPTY) {
            return new LinkInformation(url, info.fullName(), info.description(), info.lastModified());
        }
        return null;
    }

    private record GithubRepoInfoResponse(
        @JsonProperty("full_name")
        String fullName,
        @JsonProperty("updated_at")
        OffsetDateTime lastModified,
        String description
    ) {
        public static final GithubRepoInfoResponse EMPTY = new GithubRepoInfoResponse(null, null, null);
    }
}
