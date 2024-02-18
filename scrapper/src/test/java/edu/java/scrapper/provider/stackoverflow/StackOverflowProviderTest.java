package edu.java.scrapper.provider.stackoverflow;

import com.github.tomakehurst.wiremock.WireMockServer;
import edu.java.provider.api.LinkInformation;
import edu.java.provider.stackoverflow.StackOverflowInformationProvider;
import java.net.URL;
import lombok.SneakyThrows;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathMatching;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

public class StackOverflowProviderTest {
    private static WireMockServer server;

    @BeforeAll
    public static void setUp() {
        server = new WireMockServer(wireMockConfig().dynamicPort());
        server.stubFor(get(urlPathMatching("/questions/100.*"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody("""
                    {
                      "items": [
                        {
                          "last_activity_date": 1705410153,
                          "creation_date": 1256799465,
                          "last_edit_date": 1680185464,
                          "question_id": 100,
                          "link": "https://stackoverflow.com/questions/1642028/what-is-the-operator-in-c-c",
                          "title": "What is the &#39;--&gt;&#39; operator in C/C++?"
                        }
                      ]
                    }""")));
        server.stubFor(get(urlPathMatching("/questions/101.*"))
            .willReturn(aResponse()
                .withStatus(404)));
        server.start();
    }

    @SneakyThrows
    @Test
    public void getInformationShouldReturnCorrectInformation() {
        StackOverflowInformationProvider provider = new StackOverflowInformationProvider(server.baseUrl());
        var info = provider.fetchInformation(new URL("https://stackoverflow.com/questions/100/?hello_world"));
        Assertions.assertThat(info)
            .extracting(LinkInformation::url, LinkInformation::title, LinkInformation::description)
            .contains(
                new URL("https://stackoverflow.com/questions/100/?hello_world"),
                "What is the &#39;--&gt;&#39; operator in C/C++?",
                null
            );
    }

    @SneakyThrows
    @Test
    public void getInformationShouldReturnNullWhenQuestionNotFound() {
        StackOverflowInformationProvider provider = new StackOverflowInformationProvider(server.baseUrl());
        var info = provider.fetchInformation(new URL("https://stackoverflow.com/questions/101/?hello_world"));
        Assertions.assertThat(info).isNull();
    }

    @SneakyThrows
    @Test
    public void isSupportedShouldReturnTrueIfHostIsValid() {
        StackOverflowInformationProvider provider = new StackOverflowInformationProvider(server.baseUrl());
        var info = provider.isSupported(new URL("https://stackoverflow.com/questions/100/?hello_world"));
        Assertions.assertThat(info).isTrue();
    }

    @SneakyThrows
    @Test
    public void isSupportedShouldReturnFalseIfHostIsInValid() {
        StackOverflowInformationProvider provider = new StackOverflowInformationProvider(server.baseUrl());
        var info = provider.isSupported(new URL("https://memoryoutofrange.com/jij/hih"));
        Assertions.assertThat(info).isFalse();
    }
}
