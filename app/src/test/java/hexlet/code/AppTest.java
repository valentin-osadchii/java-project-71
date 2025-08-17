package hexlet.code;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.assertEquals;

class AppTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void testFlatFilesDiff() throws Exception {
        // Загружаем тестовые данные
        String beforeJson = Files.readString(
                Path.of("src/test/resources/file1.json")
        );
        String afterJson = Files.readString(
                Path.of("src/test/resources/file2.json")
        );

        // Парсим JSON в Map
        Map<String, Object> before = objectMapper.readValue(beforeJson,
                new com.fasterxml.jackson.core.type.TypeReference<Map<String, Object>>() {});
        Map<String, Object> after = objectMapper.readValue(afterJson,
                new com.fasterxml.jackson.core.type.TypeReference<Map<String, Object>>() {});

        // Ожидаемый результат
        String expected = """
            {
                - follow: false
                  host: hexlet.io
                - proxy: 123.234.53.22
                - timeout: 50
                + timeout: 20
                + verbose: true
            }""";

        // Получаем реальный результат
        String actual = App.generateDiff(before, after);

        // Сравниваем с нормализацией пробелов
        assertEquals(normalizeSpaces(expected), normalizeSpaces(actual));
    }

    private String normalizeSpaces(String input) {
        return input.replaceAll("\\s+", " ").trim();
    }
}