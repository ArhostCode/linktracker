package edu.java.provider.stackoverflow;

import com.fasterxml.jackson.annotation.JsonProperty;
import edu.java.provider.api.InformationProvider;
import edu.java.provider.api.LinkInformation;
import java.net.URL;
import java.time.OffsetDateTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public class StackOverflowInformationProvider extends InformationProvider {
    private static final Pattern QUESTION_PATTERN = Pattern.compile("https://stackoverflow.com/questions/(\\d+).*");
    public static final String BASE_API_URL = "https://api.stackexchange.com/2.3";
    private static final String STACKOVERFLOW_HOST = "stackoverflow.com";
    private WebClient client;

    public StackOverflowInformationProvider(String apiUrl) {
        this.client = WebClient.builder().baseUrl(apiUrl).build();
    }

    public StackOverflowInformationProvider() {
        this(BASE_API_URL);
    }

    @Override
    protected boolean isSupported(URL url) {
        return url.getHost().equals(STACKOVERFLOW_HOST);
    }

    @Override
    protected LinkInformation fetchInformation(URL url) {
        Matcher matcher = QUESTION_PATTERN.matcher(url.toString());
        if (!matcher.matches()) {
            return null;
        }
        var questionId = matcher.group(1);
        var info = client
            .get()
            .uri("/questions/" + questionId + "?site=stackoverflow")
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .onStatus(HttpStatusCode::isError, response -> Mono.empty()).bodyToMono(StackOverflowInfoResponse.class)
            .block();
        return new LinkInformation(url, info.items[0].title, null, info.items[0].lastModified);
    }

    private record StackOverflowInfoResponse(@JsonProperty("items") StackOverflowItem[] items) {
    }

    private record StackOverflowItem(String title, @JsonProperty("last_activity_date") OffsetDateTime lastModified) {
    }
}
