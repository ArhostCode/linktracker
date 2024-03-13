package edu.java.provider.github;

import com.fasterxml.jackson.annotation.JsonProperty;
import edu.java.configuration.ApplicationConfig;
import edu.java.provider.api.LinkInformation;
import edu.java.provider.api.WebClientInformationProvider;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.regex.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class GithubInformationProvider extends WebClientInformationProvider {

    private static final Pattern REPOSITORY_PATTERN = Pattern.compile("https://github.com/(.+)/(.+)");

    @Autowired
    public GithubInformationProvider(@Value("${provider.github.url}") String apiUrl, ApplicationConfig config) {
        super(WebClient.builder()
            .baseUrl(apiUrl)
            .defaultHeaders(headers -> {
                if (config.githubToken() != null) {
                    headers.set("Authorization", "Bearer " + config.githubToken());
                }
            })
            .build()
        );
    }

    public GithubInformationProvider() {
        super("https://api.github.com");
    }

    @Override
    public boolean isSupported(URI url) {
        return REPOSITORY_PATTERN.matcher(url.toString()).matches();
    }

    @Override
    public String getType() {
        return "github.com";
    }

    @Override
    public LinkInformation fetchInformation(URI url) {
        if (!isSupported(url)) {
            return null;
        }
        var info = executeRequest(
            "/repos/" + url.getPath(),
            GithubRepoInfoResponse.class,
            GithubRepoInfoResponse.EMPTY
        );
        if (info == null || info.equals(GithubRepoInfoResponse.EMPTY)) {
            return null;
        }
        return new LinkInformation(url, info.fullName(), info.description(), info.lastModified());
    }

    private record GithubRepoInfoResponse(
        @JsonProperty("full_name")
        String fullName,
        @JsonProperty("pushed_at")
        OffsetDateTime lastModified,
        String description
    ) {
        public static final GithubRepoInfoResponse EMPTY = new GithubRepoInfoResponse(null, null, null);
    }
}
