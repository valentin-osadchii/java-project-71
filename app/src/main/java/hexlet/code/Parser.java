package hexlet.code;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.IOException;
import java.util.Map;

public class Parser {

    static Map<String, Object> parseFileToMap(String content, String filePath) throws IOException {
        if (filePath.endsWith(".json")) {
            return parseJsonToMap(content, filePath);
        } else if (filePath.endsWith(".yaml") || filePath.endsWith(".yml")) {
            return parseYamlToMap(content, filePath);
        } else {
            throw new IOException("Unsupported file format: " + filePath);
        }
    }

    private static Map<String, Object> parseYamlToMap(String yamlContent, String fileName) throws IOException {
        ObjectMapper yamlMapper = new ObjectMapper(new YAMLFactory());
        try {
            return yamlMapper.readValue(
                    yamlContent,
                    new TypeReference<Map<String, Object>>() { }
            );
        } catch (JsonProcessingException e) {
            throw new IOException("Error parsing YAML in " + fileName + ": " + e.getMessage(), e);
        }
    }

    private static Map<String, Object> parseJsonToMap(String jsonContent, String fileName) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(
                    jsonContent,
                    new TypeReference<Map<String, Object>>() { }
            );
        } catch (JsonProcessingException e) {
            throw new IOException("Error parsing JSON in " + fileName + ": " + e.getMessage(), e);
        }
    }
}
