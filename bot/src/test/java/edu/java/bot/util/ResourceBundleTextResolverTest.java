package edu.java.bot.util;

import java.util.Locale;
import java.util.Map;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.io.ClassPathResource;

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
    public void resolveShouldReturnCorrectValueFromFile(String key, String expected) {
        ResourceBundleTextResolver resolver = new ResourceBundleTextResolver(
            configuredMessageSource()
        );
        String result = resolver.resolve(key);
        Assertions.assertThat(result).isEqualTo(expected);
    }

    @DisplayName("Тестирование метода ResourceBundleTextResolver#resolve с дополнительными вставками")
    @Test
    public void resolveShouldReturnCorrectValueWithInsertions() {
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
        YamlPropertiesFactoryBean yamlPropertiesFactoryBean = new YamlPropertiesFactoryBean();
        yamlPropertiesFactoryBean.setResources(new ClassPathResource("message-test.yml"));
        messageSource.setCommonMessages(yamlPropertiesFactoryBean.getObject());
        return messageSource;
    }
}
