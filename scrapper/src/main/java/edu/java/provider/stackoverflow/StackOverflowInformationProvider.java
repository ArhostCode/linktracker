package edu.java.provider.stackoverflow;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.java.provider.api.EventCollectableInformationProvider;
import edu.java.provider.api.LinkInformation;
import edu.java.provider.api.LinkUpdateEvent;
import edu.java.provider.stackoverflow.model.StackOverflowItem;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class StackOverflowInformationProvider extends EventCollectableInformationProvider<StackOverflowItem> {
    private static final Pattern QUESTION_PATTERN = Pattern.compile("https://stackoverflow.com/questions/(\\d+).*");
    private static final TypeReference<HashMap<String, String>> STRING_HASHMAP = new TypeReference<>() {
    };
    private final ObjectMapper mapper;

    @Autowired
    public StackOverflowInformationProvider(
        @Value("${provider.stackoverflow.url}") String apiUrl,
        ObjectMapper mapper
    ) {
        super(apiUrl);
        this.mapper = mapper;
        registerCollector(
            "AnswerEvent",
            item -> new LinkUpdateEvent(
                "stackoverflow.answers_event",
                item.lastModified(),
                Map.of("count", String.valueOf(item.answersCount()))
            )
        );
        registerCollector(
            "ScoreEvent",
            item -> new LinkUpdateEvent(
                "stackoverflow.score_event",
                item.lastModified(),
                Map.of("score", String.valueOf(item.score()))
            )
        );
    }

    public StackOverflowInformationProvider() {
        this("https://api.stackexchange.com/2.3", new ObjectMapper());
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
        return new LinkInformation(
            url,
            info.items()[0].title(),
            getLinkUpdateEventsCollector().values().stream()
                .map(stackOverflowItemLinkUpdateEventFunction ->
                    stackOverflowItemLinkUpdateEventFunction.apply(info.items()[0]))
                .toList()
        );
    }

    @SneakyThrows
    @Override
    public LinkInformation filter(LinkInformation info, OffsetDateTime after, String optionalContext) {
        Map<String, String> optionalData = new HashMap<>();
        if (optionalContext != null && !optionalContext.isEmpty()) {
            optionalData = mapper.readValue(optionalContext, STRING_HASHMAP);
        }
        List<LinkUpdateEvent> realEvents = new ArrayList<>();
        List<LinkUpdateEvent> filteredEvents =
            info.events()
                .stream()
                .filter(event -> event.lastModified().isAfter(after))
                .toList();
        for (LinkUpdateEvent event : filteredEvents) {
            for (Map.Entry<String, String> entry : event.additionalData().entrySet()) {
                if (optionalData.containsKey(entry.getKey())) {
                    String value = optionalData.get(entry.getKey());
                    if (value.equals(entry.getValue())) {
                        continue;
                    }
                }
                optionalData.put(entry.getKey(), entry.getValue());
                if (!realEvents.contains(event)) {
                    realEvents.add(event);
                }
            }
        }
        return new LinkInformation(info.url(), info.title(), realEvents, mapper.writeValueAsString(optionalData));
    }

    private record StackOverflowInfoResponse(StackOverflowItem[] items) {
        public static final StackOverflowInfoResponse EMPTY = new StackOverflowInfoResponse(null);
    }

}
