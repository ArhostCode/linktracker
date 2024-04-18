package edu.java.provider.api;

import edu.java.configuration.RetryConfig;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import lombok.Getter;
import org.springframework.web.reactive.function.client.WebClient;

@Getter
public abstract class EventCollectableInformationProvider<T> extends WebClientInformationProvider {

    private final Map<String, Function<T, LinkUpdateEvent>> linkUpdateEventsCollector = new HashMap<>();

    public EventCollectableInformationProvider(WebClient webClient) {
        super(webClient);
    }

    public EventCollectableInformationProvider(String apiUrl) {
        super(apiUrl);
    }

    public EventCollectableInformationProvider(String apiUrl, RetryConfig config, String target) {
        super(apiUrl, config, target);
    }

    public void registerCollector(String type, Function<T, LinkUpdateEvent> collector) {
        linkUpdateEventsCollector.put(type, collector);
    }

    public Function<T, LinkUpdateEvent> getCollector(String type) {
        return linkUpdateEventsCollector.get(type);
    }
}
