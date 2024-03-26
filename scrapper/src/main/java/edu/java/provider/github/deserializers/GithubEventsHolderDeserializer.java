package edu.java.provider.github.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import edu.java.provider.github.model.GithubEventInfo;
import edu.java.provider.github.model.GithubEventsHolder;
import java.io.IOException;
import java.util.Arrays;

public class GithubEventsHolderDeserializer extends StdDeserializer<GithubEventsHolder> {

    public GithubEventsHolderDeserializer() {
        super(GithubEventsHolder.class);
    }

    @Override
    public GithubEventsHolder deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
        throws IOException {
        ObjectMapper mapper = (ObjectMapper) jsonParser.getCodec();
        return new GithubEventsHolder(
            Arrays.stream(mapper.readValue(jsonParser, GithubEventInfo[].class)).toList()
        );
    }
}
