package edu.java.scrapper.provider.github;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import edu.java.provider.api.LinkInformation;
import edu.java.provider.github.GithubInformationProvider;
import java.net.URL;
import lombok.SneakyThrows;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathMatching;

public class GithubInformationProviderTest {

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(9090);

    @Before
    public void setUp() {
        stubFor(get(urlPathMatching("/repos/arhostcode/linktracker"))
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
    }

    @SneakyThrows
    @Test
    public void getInformationShouldReturnCorrectInformation() {
        GithubInformationProvider provider = new GithubInformationProvider(wireMockRule.baseUrl());
        var info = provider.getLinkInformation(new URL("https://github.com/arhostcode/linktracker"));
        Assertions.assertThat(info)
            .extracting(LinkInformation::url, LinkInformation::title, LinkInformation::description)
            .contains(
                new URL("https://github.com/arhostcode/linktracker"),
                "ArhostCode / linktracker",
                "🛠️ Проект Tinkoff Java Course 2семестр"
            );

    }

}
