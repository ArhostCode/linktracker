package edu.java.provider.api;

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

    protected <T> T executeRequest(String uri, Class<T> type, T defaultValue) {
        return webClient
            .get()
            .uri(uri)
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .bodyToMono(type)
            .onErrorReturn(defaultValue)
            .block();
    }
}
