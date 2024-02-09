package edu.java.bot.util;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.context.support.ResourceBundleMessageSource;

import java.util.Locale;
import java.util.Map;

public class ResourceBundleTextResolverTest {

    @DisplayName("Тестирование метода ResourceBundleTextResolver#resolve на получение сообщения из ресурсного файла")
    @ParameterizedTest
    @CsvSource(
            value = {
                    "message.sample,Пример сообщения",
                    "next,Другое сообщение",
                    "message.not_found,message.not_found"

            }
    )
    public void resolve_shouldReturnCorrectValueFromFile(String key, String expected) {
        ResourceBundleTextResolver resolver = new ResourceBundleTextResolver(
                configuredMessageSource()
        );
        String result = resolver.resolve(key);
        Assertions.assertThat(result).isEqualTo(expected);
    }

    @DisplayName("Тестирование метода ResourceBundleTextResolver#resolve с дополнительными вставками")
    @Test
    public void resolve_shouldReturnCorrectValueWithInsertions() {
        ResourceBundleTextResolver resolver = new ResourceBundleTextResolver(
                configuredMessageSource()
        );
        String result = resolver.resolve(
                "message.insertions",
                Map.of(
                        "user_name", "Егор"
                )
        );
        Assertions.assertThat(result).isEqualTo("Добро пожаловать, Егор");
    }

    private ResourceBundleMessageSource configuredMessageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("message-test");
        messageSource.setDefaultLocale(Locale.of("ru"));
        return messageSource;
    }
}
