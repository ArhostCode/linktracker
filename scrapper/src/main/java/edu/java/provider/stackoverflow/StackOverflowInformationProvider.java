package edu.java.provider.stackoverflow;

import com.fasterxml.jackson.annotation.JsonProperty;
import edu.java.provider.api.LinkInformation;
import edu.java.provider.api.WebClientInformationProvider;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class StackOverflowInformationProvider extends WebClientInformationProvider {
    private static final Pattern QUESTION_PATTERN = Pattern.compile("https://stackoverflow.com/questions/(\\d+).*");

    @Autowired
    public StackOverflowInformationProvider(
        @Value("${provider.stackoverflow.url}") String apiUrl
    ) {
        super(apiUrl);
    }

    public StackOverflowInformationProvider() {
        super("https://api.stackexchange.com/2.3");
    }

    @Override
    public boolean isSupported(URI url) {
        return QUESTION_PATTERN.matcher(url.toString()).matches();
    }

    @Override
    public String getType() {
        return "stackoverflow.com";
    }

    @Override
    public LinkInformation fetchInformation(URI url) {
        Matcher matcher = QUESTION_PATTERN.matcher(url.toString());
        if (!matcher.matches()) {
            return null;
        }
        var questionId = matcher.group(1);
        var info = executeRequest(
            "/questions/" + questionId + "?site=stackoverflow",
            StackOverflowInfoResponse.class,
            StackOverflowInfoResponse.EMPTY
        );
        if (info == null || info.equals(StackOverflowInfoResponse.EMPTY) || info.items.length == 0) {
            return null;
        }
        return new LinkInformation(url, info.items()[0].title(), null, info.items()[0].lastModified());
    }

    private record StackOverflowInfoResponse(StackOverflowItem[] items) {
        public static final StackOverflowInfoResponse EMPTY = new StackOverflowInfoResponse(null);
    }

    private record StackOverflowItem(String title, @JsonProperty("last_activity_date") OffsetDateTime lastModified) {
    }
}
