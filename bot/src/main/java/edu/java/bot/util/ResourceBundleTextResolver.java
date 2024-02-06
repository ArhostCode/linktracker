package edu.java.bot.util;

import java.util.Locale;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.apache.commons.text.StringSubstitutor;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ResourceBundleTextResolver implements TextResolver {

    private static final String AFFIX = "%"; // Prefix and Suffix
    private final ResourceBundleMessageSource messageSourceResourceBundle;

    @Override
    public String resolve(String messageId, Map<String, String> insertions) {
        try {
            String resolvedMessage = messageSourceResourceBundle.getMessage(messageId, null, Locale.of("ru"));
            return StringSubstitutor.replace(resolvedMessage, insertions, AFFIX, AFFIX);
        } catch (NoSuchMessageException e) {
            return messageId;
        }
    }
}
