package edu.java.configuration;

import edu.java.provider.InformationProviderChain;
import edu.java.provider.github.GithubInformationProvider;
import edu.java.provider.stackoverflow.StackOverflowInformationProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
public class ClientConfiguration {

    @Bean
    public InformationProviderChain informationProviderChain() {
        return InformationProviderChain.link(
            new GithubInformationProvider(),
            new StackOverflowInformationProvider()
        );
    }

}
