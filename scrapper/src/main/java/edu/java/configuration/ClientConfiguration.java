package edu.java.configuration;

import edu.java.provider.InformationProviders;
import edu.java.provider.github.GithubInformationProvider;
import edu.java.provider.stackoverflow.StackOverflowInformationProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
public class ClientConfiguration {

    @Bean
    public InformationProviders informationProviders() {
        InformationProviders informationProviders = new InformationProviders();
        informationProviders.registerProvider(
            GithubInformationProvider.PROVIDER_TYPE,
            new GithubInformationProvider()
        );
        informationProviders.registerProvider(
            StackOverflowInformationProvider.PROVIDER_TYPE,
            new StackOverflowInformationProvider()
        );
        return informationProviders;
    }

}
