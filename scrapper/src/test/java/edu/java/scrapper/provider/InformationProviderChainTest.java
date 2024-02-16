package edu.java.scrapper.provider;

import edu.java.provider.InformationProviderChain;
import edu.java.provider.api.InformationProvider;
import lombok.SneakyThrows;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.Mockito;
import java.net.URL;

public class InformationProviderChainTest {

    @Test
    @SneakyThrows
    @DisplayName("Тестирование InformationProviderChain#link на связывание")
    public void linkShouldCreateChainOfProviders() {
        var infoProvider1 = Mockito.mock(InformationProvider.class, Mockito.CALLS_REAL_METHODS);
        var infoProvider2 = Mockito.mock(InformationProvider.class, Mockito.CALLS_REAL_METHODS);
        var url = new URL("http://localhost");
        Mockito.doReturn(false).when(infoProvider1).isSupported(url);
        Mockito.doReturn(false).when(infoProvider2).isSupported(url);

        var chain = InformationProviderChain.link(
            infoProvider1,
            infoProvider2
        );

        var response = chain.getLinkInformation(url);
        Assertions.assertThat(response).isNull();
        Mockito.verify(infoProvider1).getLinkInformation(url);
        Mockito.verify(infoProvider2).getLinkInformation(url);
    }

}
