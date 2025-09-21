package hexlet.code;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.IOException;
import java.util.Map;

public class Parser {

    // Unified method to parse content based on format string
    public static Map<String, Object> toMap(String content, String dataFormat) throws JsonProcessingException {
        return switch (dataFormat) {
            case "json" -> parseJson(content);
            case "yaml", "yml" -> parseYaml(content);
            default -> throw new IllegalArgumentException("Unsupported format: " + dataFormat);
        };
    }

    private static Map<String, Object> parseYaml(String yamlContent) throws JsonProcessingException {
        ObjectMapper yamlMapper = new ObjectMapper(new YAMLFactory());
        return yamlMapper.readValue(yamlContent, new TypeReference<>() {});
    }

    private static Map<String, Object> parseJson(String jsonContent) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(jsonContent, new TypeReference<>() {});
    }
}
