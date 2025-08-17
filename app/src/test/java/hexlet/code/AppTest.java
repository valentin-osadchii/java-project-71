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
                new com.fasterxml.jackson.core.type.TypeReference<Map<String, Object>>() { });
        Map<String, Object> after = objectMapper.readValue(afterJson,
                new com.fasterxml.jackson.core.type.TypeReference<Map<String, Object>>() { });

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

    // Тест для одинаковых файлов
    @Test
    void testIdenticalFiles() {
        Map<String, Object> data1 = Map.of(
                "host", "hexlet.io",
                "port", 8080
        );

        Map<String, Object> data2 = Map.of(
                "host", "hexlet.io",
                "port", 8080
        );

        String expected = """
            {
                host: hexlet.io
                port: 8080
            }""";

        assertEquals(normalizeSpaces(expected),
                normalizeSpaces(App.generateDiff(data1, data2)));
    }

    // Тест для файлов с разным порядком ключей
    @Test
    void testDifferentOrder() {
        Map<String, Object> data1 = Map.of(
                "c", 3,
                "a", 1,
                "b", 2
        );

        Map<String, Object> data2 = Map.of(
                "b", 2,
                "c", 3,
                "a", 1
        );

        String expected = """
            {
                a: 1
                b: 2
                c: 3
            }""";

        assertEquals(normalizeSpaces(expected),
                normalizeSpaces(App.generateDiff(data1, data2)));
    }

    // Тест для файлов с разными типами данных
    @Test
    void testDifferentDataTypes() {
        Map<String, Object> data1 = Map.of(
                "number", 42,
                "string", "text",
                "boolean", true
        );

        Map<String, Object> data2 = Map.of(
                "number", "42",
                "string", "text",
                "boolean", false
        );

        String expected = """
            {
                - boolean: true
                + boolean: false
                - number: 42
                + number: 42
                  string: text
            }""";

        assertEquals(normalizeSpaces(expected),
                normalizeSpaces(App.generateDiff(data1, data2)));
    }

    // Тест для одного пустого файла
    @Test
    void testEmptyFile() {
        Map<String, Object> data1 = Map.of();
        Map<String, Object> data2 = Map.of(
                "key", "value"
        );

        String expected = """
            {
                + key: value
            }""";

        assertEquals(normalizeSpaces(expected),
                normalizeSpaces(App.generateDiff(data1, data2)));
    }


    private String normalizeSpaces(String input) {
        return input.replaceAll("\\s+", " ").trim();
    }
}
