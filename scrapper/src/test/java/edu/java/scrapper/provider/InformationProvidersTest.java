package edu.java.scrapper.provider;

import edu.java.provider.InformationProviders;
import edu.java.provider.api.InformationProvider;
import lombok.SneakyThrows;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class InformationProvidersTest {

    @Test
    @SneakyThrows
    @DisplayName("Тестирование InformationProviders#register на добавление провайдера")
    public void registerShouldAddInformationProvider() {
        var infoProvider1 = Mockito.mock(InformationProvider.class, Mockito.CALLS_REAL_METHODS);

        var infoProviders = new InformationProviders();
        infoProviders.registerProvider("provider1", infoProvider1);

        var provider = infoProviders.getProvider("provider1");
        Assertions.assertThat(provider).isEqualTo(infoProvider1);
    }

    @Test
    @SneakyThrows
    @DisplayName("Тестирование InformationProviders#get на пустой провайдер")
    public void getShouldReturnNullWhenProviderDoesNotExists() {
        var infoProviders = new InformationProviders();

        var provider = infoProviders.getProvider("provider1");
        Assertions.assertThat(provider).isNull();
    }

}
