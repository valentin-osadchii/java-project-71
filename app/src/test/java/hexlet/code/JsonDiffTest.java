package hexlet.code;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.assertEquals;

class JsonDiffTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void testJsonFlatFilesDiff() throws Exception {
        // Загружаем тестовые данные
        String beforeJson = Files.readString(
                Path.of("src/test/resources/file1nested.json")
        );
        String afterJson = Files.readString(
                Path.of("src/test/resources/file2nested.json")
        );

        // Парсим JSON в Map
        Map<String, Object> before = objectMapper.readValue(beforeJson,
                new com.fasterxml.jackson.core.type.TypeReference<Map<String, Object>>() { });
        Map<String, Object> after = objectMapper.readValue(afterJson,
                new com.fasterxml.jackson.core.type.TypeReference<Map<String, Object>>() { });

        // Ожидаемый результат с правильными отступами и форматированием
        String expected = """
            {
                chars1: [a, b, c]
              - chars2: [d, e, f]
              + chars2: false
              - checked: false
              + checked: true
              - default: null
              + default: [value1, value2]
              - id: 45
              + id: null
              - key1: value1
              + key2: value2
                numbers1: [1, 2, 3, 4]
              - numbers2: [2, 3, 4, 5]
              + numbers2: [22, 33, 44, 55]
              - numbers3: [3, 4, 5]
              + numbers4: [4, 5, 6]
              + obj1: {nestedKey=value, isNested=true}
              - setting1: Some value
              + setting1: Another value
              - setting2: 200
              + setting2: 300
              - setting3: true
              + setting3: none
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
