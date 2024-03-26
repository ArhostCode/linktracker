package edu.java.bot.util;

import java.util.Collections;
import java.util.Map;

public interface TextResolver {

    String resolve(String messageId, Map<String, String> insertions);

    String resolve(String messageId, Map<String, String> insertions, String defaultValue);

    default String resolve(String messageId) {
        return resolve(messageId, Collections.emptyMap());
    }

}
