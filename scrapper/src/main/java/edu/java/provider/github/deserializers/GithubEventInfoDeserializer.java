package edu.java.provider.github.deserializers;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.ObjectNode;
import edu.java.provider.github.model.GithubEventInfo;
import edu.java.provider.github.model.RepositoryInfo;
import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import lombok.Data;
import org.apache.commons.lang3.tuple.Pair;

public class GithubEventInfoDeserializer extends StdDeserializer<GithubEventInfo> {

    private final Map<String, Function<ObjectNode, List<Pair<String, String>>>> extractors = new HashMap<>();

    @SuppressWarnings("checkstyle:MultipleStringLiterals")
    public GithubEventInfoDeserializer() {
        super(GithubEventInfo.class);
        registerExtractor(
            "github.pushevent", node ->
                List.of(Pair.of("count", String.valueOf(node.get("payload").get("size").asInt()))));
        registerExtractor(
            "github.issuecommentevent",
            node -> List.of(Pair.of("title", node.get("payload").get("issue").get("title").asText()))
        );
        registerExtractor(
            "github.pullrequestevent",
            node -> List.of(Pair.of("title", node.get("payload").get("pull_request").get("title").asText()))
        );
        registerExtractor(
            "github.createevent",
            node -> List.of(
                Pair.of("ref", node.get("payload").get("ref").asText()),
                Pair.of("ref_type", node.get("payload").get("ref_type").asText())
            )
        );
    }

    @Override
    public GithubEventInfo deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
        throws IOException {
        ObjectMapper mapper = (ObjectMapper) jsonParser.getCodec();
        ObjectNode objectNode = mapper.readTree(jsonParser);
        InnerGithubEventInfo innerGithubEventInfo = mapper.convertValue(objectNode, InnerGithubEventInfo.class);
        GithubEventInfo info = new GithubEventInfo(
            innerGithubEventInfo.getLastModified(),
            "github." + innerGithubEventInfo.getType().toLowerCase(),
            innerGithubEventInfo.getRepo(),
            new HashMap<>()
        );
        if (extractors.containsKey(info.type())) {
            extractors.get(info.type()).apply(objectNode).forEach(it ->
                info.additionalData().put(it.getLeft(), it.getRight()));
        }
        return info;
    }

    private void registerExtractor(String type, Function<ObjectNode, List<Pair<String, String>>> extractor) {
        extractors.put(type, extractor);
    }

    @Data
    private static final class InnerGithubEventInfo {
        @JsonProperty("created_at")
        private OffsetDateTime lastModified;
        private String type;
        private RepositoryInfo repo;
    }
}
