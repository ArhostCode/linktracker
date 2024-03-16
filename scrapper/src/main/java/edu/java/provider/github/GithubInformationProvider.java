package edu.java.provider.github;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import edu.java.configuration.ApplicationConfig;
import edu.java.provider.api.LinkInformation;
import edu.java.provider.api.LinkUpdateEvent;
import edu.java.provider.api.WebClientInformationProvider;
import edu.java.provider.github.deserializers.GithubEventInfoDeserializer;
import edu.java.provider.github.deserializers.GithubEventsHolderDeserializer;
import edu.java.provider.github.model.GithubEventInfo;
import edu.java.provider.github.model.GithubEventsHolder;
import jakarta.annotation.PostConstruct;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class GithubInformationProvider extends WebClientInformationProvider {

    private static final Pattern REPOSITORY_PATTERN = Pattern.compile("https://github.com/(.+)/(.+)");
    private static final int MAX_PER_UPDATE = 5;

    @Autowired
    public GithubInformationProvider(
        @Value("${provider.github.url}") String apiUrl,
        ApplicationConfig config,
        ObjectMapper mapper
    ) {
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
            "/repos/" + url.getPath() + "/events?per_page=" + MAX_PER_UPDATE,
            GithubEventsHolder.class,
            GithubEventsHolder.EMPTY
        );

        if (info == null || info.equals(GithubEventsHolder.EMPTY)) {
            return null;
        }
        return new LinkInformation(
            url,
            info.events().getFirst().repo().name(),
            info.events().stream()
                .map(event -> new LinkUpdateEvent(event.type(), event.lastModified(), event.additionalData()))
                .toList()
        );
    }

    @Override
    public LinkInformation filter(LinkInformation info, OffsetDateTime after, String optionalContext) {
        List<LinkUpdateEvent> realUpdates =
            info.events().stream().filter(event -> event.lastModified().isAfter(after)).toList();
        return new LinkInformation(
            info.url(),
            info.title(),
            realUpdates
        );
    }

    @PostConstruct
    public void test() {
        System.out.println(fetchInformation(URI.create("https://github.com/arhostcode/linktracker")));
    }
}
