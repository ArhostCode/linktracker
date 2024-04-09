package edu.java.provider.api;

import edu.java.configuration.RetryConfig;
import edu.java.util.retry.RetryFactory;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

public abstract class WebClientInformationProvider implements InformationProvider {

    protected WebClient webClient;

    public WebClientInformationProvider(WebClient webClient) {
        this.webClient = webClient;
    }

    public WebClientInformationProvider(String apiUrl) {
        this(WebClient.create(apiUrl));
    }

    public WebClientInformationProvider(String apiUrl, RetryConfig config, String target) {
        this(WebClient.builder()
            .baseUrl(apiUrl)
            .filter(RetryFactory.createFilter(config, target))
            .build()
        );
    }

    protected <T> T executeRequest(String uri, Class<T> type, T defaultValue) {
        try {
            return webClient
                .get()
                .uri(uri)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(type)
                .block();
        } catch (Exception e) {
            return defaultValue;
        }
    }
}
