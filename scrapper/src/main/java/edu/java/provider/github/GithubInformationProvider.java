package edu.java.provider.github;

import com.fasterxml.jackson.annotation.JsonProperty;
import edu.java.provider.api.LinkInformation;
import edu.java.provider.api.WebClientInformationProvider;
import java.net.URL;
import java.time.OffsetDateTime;
import org.springframework.http.MediaType;

public class GithubInformationProvider extends WebClientInformationProvider {

    private static final String API_URL = "https://api.github.com";
    private static final String GITHUB_HOST = "github.com";

    public GithubInformationProvider(String apiUrl) {
        super(apiUrl);
    }

    public GithubInformationProvider() {
        this(API_URL);
    }

    @Override
    public boolean isSupported(URL url) {
        return url.getHost().equals(GITHUB_HOST);
    }

    @Override
    protected LinkInformation fetchInformation(URL url) {
        var info = webClient.get()
            .uri("/repos" + url.getPath())
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .bodyToMono(GithubRepoInfoResponse.class)
            .onErrorReturn(GithubRepoInfoResponse.EMPTY)
            .block();
        if (info == null || info.equals(GithubRepoInfoResponse.EMPTY)) {
            return null;
        }
        return new LinkInformation(url, info.fullName(), info.description(), info.lastModified());
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
