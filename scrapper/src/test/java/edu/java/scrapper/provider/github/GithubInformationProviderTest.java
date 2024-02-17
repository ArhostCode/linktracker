package edu.java.scrapper.provider.github;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.common.ConsoleNotifier;
import edu.java.provider.api.LinkInformation;
import edu.java.provider.github.GithubInformationProvider;
import java.net.URL;
import lombok.SneakyThrows;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathMatching;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

public class GithubInformationProviderTest {

    private static WireMockServer server;

    @BeforeAll
    public static void setUp() {
        server = new WireMockServer(wireMockConfig().dynamicPort());
        server.stubFor(get(urlPathMatching("/repos/arhostcode/linktracker"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody("""
                    {
                      "id": 753034583,
                      "full_name": "ArhostCode/linktracker",
                      "created_at": "2024-02-05T10:42:09Z",
                      "updated_at": "2024-02-05T10:45:31Z",
                      "pushed_at": "2024-02-12T20:42:03Z",
                      "language": "Java",
                      "description": "🛠️ Проект Tinkoff Java Course 2 семестр"
                    }""")));
        server.stubFor(get(urlPathMatching("/repos/jij/hih"))
            .willReturn(aResponse()
                .withStatus(404)));
        server.start();
    }

    @SneakyThrows
    @Test
    public void getInformationShouldReturnCorrectInformation() {
        GithubInformationProvider provider = new GithubInformationProvider(server.baseUrl());
        var info = provider.fetchInformation(new URL("https://github.com/arhostcode/linktracker"));
        Assertions.assertThat(info)
            .extracting(LinkInformation::url, LinkInformation::title, LinkInformation::description)
            .contains(
                new URL("https://github.com/arhostcode/linktracker"),
                "ArhostCode/linktracker",
                "🛠️ Проект Tinkoff Java Course 2 семестр"
            );
    }

    @SneakyThrows
    @Test
    public void getInformationShouldReturnNullWhenRepositoryNotFound() {
        GithubInformationProvider provider = new GithubInformationProvider(server.baseUrl());
        var info = provider.fetchInformation(new URL("https://github.com/jij/hih"));
        Assertions.assertThat(info).isNull();
    }

    @SneakyThrows
    @Test
    public void isSupportedShouldReturnTrueIfHostIsValid() {
        GithubInformationProvider provider = new GithubInformationProvider(server.baseUrl());
        var info = provider.isSupported(new URL("https://github.com/jij/hih"));
        Assertions.assertThat(info).isTrue();
    }

    @SneakyThrows
    @Test
    public void isSupportedShouldReturnFalseIfHostIsInValid() {
        GithubInformationProvider provider = new GithubInformationProvider(server.baseUrl());
        var info = provider.isSupported(new URL("https://gitlab.com/jij/hih"));
        Assertions.assertThat(info).isFalse();
    }

}
