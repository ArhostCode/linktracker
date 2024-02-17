package edu.java.provider.stackoverflow;

import com.fasterxml.jackson.annotation.JsonProperty;
import edu.java.provider.api.LinkInformation;
import edu.java.provider.api.WebClientInformationProvider;
import java.net.URL;
import java.time.OffsetDateTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import reactor.core.publisher.Mono;

public class StackOverflowInformationProvider extends WebClientInformationProvider {
    private static final Pattern QUESTION_PATTERN = Pattern.compile("https://stackoverflow.com/questions/(\\d+).*");
    public static final String BASE_API_URL = "https://api.stackexchange.com/2.3";
    private static final String STACKOVERFLOW_HOST = "stackoverflow.com";
    public static final String PROVIDER_TYPE = "stackoverflow";

    public StackOverflowInformationProvider(String apiUrl) {
        super(apiUrl);
    }

    public StackOverflowInformationProvider() {
        this(BASE_API_URL);
    }

    @Override
    public boolean isSupported(URL url) {
        return url.getHost().equals(STACKOVERFLOW_HOST);
    }

    @Override
    public LinkInformation fetchInformation(URL url) {
        Matcher matcher = QUESTION_PATTERN.matcher(url.toString());
        if (!matcher.matches()) {
            return null;
        }
        var questionId = matcher.group(1);
        var info = webClient
            .get()
            .uri("/questions/" + questionId + "?site=stackoverflow")
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .onStatus(HttpStatusCode::isError, response -> Mono.empty())
            .bodyToMono(StackOverflowInfoResponse.class)
            .block();
        if (info == null || info.equals(StackOverflowInfoResponse.EMPTY) || info.items.length == 0) {
            return null;
        }
        return new LinkInformation(url, info.items[0].title, null, info.items[0].lastModified);
    }

    private record StackOverflowInfoResponse(StackOverflowItem[] items) {
        public static final StackOverflowInfoResponse EMPTY = new StackOverflowInfoResponse(null);
    }

    private record StackOverflowItem(String title, @JsonProperty("last_activity_date") OffsetDateTime lastModified) {
    }
}
