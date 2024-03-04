package edu.java.bot.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import edu.java.bot.client.scrapper.ScrapperClient;
import edu.java.bot.client.scrapper.dto.request.AddLinkRequest;
import edu.java.bot.client.scrapper.dto.request.RemoveLinkRequest;
import edu.java.bot.client.scrapper.dto.response.LinkResponse;
import edu.java.bot.client.scrapper.dto.response.ListLinksResponse;
import edu.java.bot.dto.response.ApiErrorResponse;
import java.net.URI;
import java.util.List;
import lombok.SneakyThrows;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;
import reactor.core.publisher.Mono;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.delete;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathMatching;

@WireMockTest(httpPort = 9090)
public class ScrapperClientTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @SneakyThrows
    @Test
    public void linksShouldReturnCorrectValue() {
        stubFor(
            get(urlPathMatching("/links"))
                .withHeader("Tg-Chat-Id", equalTo("100"))
                .willReturn(aResponse()
                    .withStatus(200)
                    .withHeader("Content-Type", "application/json")
                    .withBody(objectMapper.writeValueAsString(new ListLinksResponse(
                        List.of(new LinkResponse(100L, URI.create("https://article.com"))),
                        1
                    )))
                )
        );

        ScrapperClient scrapperClient = scrapperClient("http://localhost:9090");
        ListLinksResponse response = scrapperClient.listLinks(100L).answer();
        Assertions.assertThat(response)
            .extracting(ListLinksResponse::links)
            .isEqualTo(List.of(new LinkResponse(100L, URI.create("https://article.com"))));
    }

    @SneakyThrows
    @Test
    public void linksPostShouldReturnAddedLink() {
        stubFor(
            post(urlPathMatching("/links"))
                .withHeader("Tg-Chat-Id", equalTo("100"))
                .willReturn(aResponse()
                    .withStatus(200)
                    .withHeader("Content-Type", "application/json")
                    .withBody(objectMapper.writeValueAsString(new LinkResponse(
                        100L,
                        URI.create("https://article.com")
                    )))
                )
        );

        ScrapperClient scrapperClient = scrapperClient("http://localhost:9090");
        LinkResponse response =
            scrapperClient.addLink(100L, new AddLinkRequest(URI.create("https://article.com"))).answer();
        Assertions.assertThat(response)
            .extracting(LinkResponse::id, LinkResponse::url)
            .contains(100L, URI.create("https://article.com"));
    }

    @SneakyThrows
    @Test
    public void linksDeleteShouldReturnAddedLink() {
        stubFor(
            delete(urlPathMatching("/links"))
                .withHeader("Tg-Chat-Id", equalTo("100"))
                .willReturn(aResponse()
                    .withStatus(200)
                    .withHeader("Content-Type", "application/json")
                    .withBody(objectMapper.writeValueAsString(new LinkResponse(
                        100L,
                        URI.create("https://article.com")
                    )))
                )
        );

        ScrapperClient scrapperClient = scrapperClient("http://localhost:9090");
        LinkResponse response =
            scrapperClient.removeLink(100L, new RemoveLinkRequest(100L)).answer();
        Assertions.assertThat(response)
            .extracting(LinkResponse::id, LinkResponse::url)
            .contains(100L, URI.create("https://article.com"));
    }

    @SneakyThrows
    @Test
    public void linksShouldReturnErrorWhenResponseIsNotOk() {
        stubFor(
            get(urlPathMatching("/links"))
                .withHeader("Tg-Chat-Id", equalTo("100"))
                .willReturn(aResponse()
                    .withStatus(404)
                    .withBody(objectMapper.writeValueAsString(
                        new ApiErrorResponse("Not found", "404", "Not found", "Not found", List.of())
                    ))
                    .withHeader("Content-Type", "application/json")
                )
        );

        ScrapperClient scrapperClient = scrapperClient("http://localhost:9090");
        ApiErrorResponse response = scrapperClient.listLinks(100L).apiErrorResponse();
        Assertions.assertThat(response)
            .extracting(ApiErrorResponse::code)
            .isEqualTo("404");
    }

    private static ScrapperClient scrapperClient(String baseUrl) {
        WebClient webClient = WebClient.builder()
            .defaultStatusHandler(httpStatusCode -> true, clientResponse -> Mono.empty())
            .baseUrl(baseUrl).build();

        HttpServiceProxyFactory httpServiceProxyFactory = HttpServiceProxyFactory
            .builderFor(WebClientAdapter.create(webClient))
            .build();
        return httpServiceProxyFactory.createClient(ScrapperClient.class);
    }

}
