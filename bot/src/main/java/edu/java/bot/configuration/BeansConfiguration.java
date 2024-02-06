package edu.java.bot.configuration;

import java.util.Locale;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;

@Configuration
public class BeansConfiguration {

    @Bean
    public ResourceBundleMessageSource messageSourceResourceBundle() {
        ResourceBundleMessageSource messageSourceResourceBundle = new ResourceBundleMessageSource();
        messageSourceResourceBundle.setBasename("message");
        messageSourceResourceBundle.setDefaultLocale(Locale.of("ru"));
        return messageSourceResourceBundle;
    }

}
