package edu.java.bot.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.node.ObjectNode;
import edu.java.bot.dto.OptionalAnswer;
import edu.java.bot.dto.response.ApiErrorResponse;
import java.io.IOException;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class OptionalAnswerDeserializer extends JsonDeserializer<OptionalAnswer<?>> implements ContextualDeserializer {
    private JavaType valueType;

    @Override
    public JsonDeserializer<?> createContextual(DeserializationContext ctxt, BeanProperty property) {
        JavaType wrapperType = ctxt.getContextualType();
        JavaType type = wrapperType.containedType(0);
        OptionalAnswerDeserializer deserializer = new OptionalAnswerDeserializer();
        deserializer.valueType = type;
        return deserializer;
    }

    @Override
    public OptionalAnswer<?> deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
        throws IOException {
        ObjectMapper mapper = (ObjectMapper) jsonParser.getCodec();
        ObjectNode objectNode = mapper.readTree(jsonParser);
        if (objectNode.get("exceptionName") != null) {
            return OptionalAnswer.error(mapper.convertValue(objectNode, ApiErrorResponse.class));
        }
        if (valueType.isTypeOrSubTypeOf(Void.TYPE)) {
            return OptionalAnswer.of(null);
        }
        return OptionalAnswer.of(mapper.convertValue(objectNode, valueType));
    }
}
