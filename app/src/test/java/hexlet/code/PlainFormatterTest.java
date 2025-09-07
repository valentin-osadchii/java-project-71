package hexlet.code;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.assertEquals;

class PlainTextFormatterTest {

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

        // Ожидаемый результат в формате PlainTextFormatter
        String expected = """
                Property 'chars2' was updated. From [complex value] to false
                Property 'checked' was updated. From false to true
                Property 'default' was updated. From null to [complex value]
                Property 'id' was updated. From 45 to null
                Property 'key1' was removed
                Property 'key2' was added with value: 'value2'
                Property 'numbers2' was updated. From [complex value] to [complex value]
                Property 'numbers3' was removed
                Property 'numbers4' was added with value: [complex value]
                Property 'obj1' was added with value: [complex value]
                Property 'setting1' was updated. From 'Some value' to 'Another value'
                Property 'setting2' was updated. From 200 to 300
                Property 'setting3' was updated. From true to 'none'""";

        // Получаем реальный результат, указывая тип форматтера "plain-text"
        String actual = App.generateDiff(before, after, "plain");

        // Сравниваем результаты
        assertEquals(expected, actual);
    }

    // Тест для одинаковых файлов (ничего не должно выводиться)
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

        String expected = "";
        String actual = App.generateDiff(data1, data2, "plain");
        assertEquals(expected, actual);
    }

    // Тест для файлов с разным порядком ключей (но одинаковыми данными)
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

        String expected = "";
        String actual = App.generateDiff(data1, data2, "plain");
        assertEquals(expected, actual);
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
            Property 'boolean' was updated. From true to false
            Property 'number' was updated. From 42 to '42'""";

        String actual = App.generateDiff(data1, data2, "plain");
        assertEquals(expected, actual);
    }

    // Тест для одного пустого файла
    @Test
    void testEmptyFile() {
        Map<String, Object> data1 = Map.of();
        Map<String, Object> data2 = Map.of(
                "key", "value"
        );

        String expected = "Property 'key' was added with value: 'value'";
        String actual = App.generateDiff(data1, data2, "plain");
        assertEquals(expected, actual);
    }

    // Тест для сложных значений (объектов и массивов)
    @Test
    void testComplexValues() {
        Map<String, Object> data1 = Map.of(
                "simple", "value",
                "array", List.of(1, 2, 3),
                "object", Map.of("key", "value")
        );

        Map<String, Object> data2 = Map.of(
                "simple", "new_value",
                "array", List.of(4, 5, 6),
                "object", Map.of("key", "new_value"),
                "new_key", "new_value"
        );

        String expected = """
            Property 'array' was updated. From [complex value] to [complex value]
            Property 'new_key' was added with value: 'new_value'
            Property 'object' was updated. From [complex value] to [complex value]
            Property 'simple' was updated. From 'value' to 'new_value'""";

        String actual = App.generateDiff(data1, data2, "plain");
        assertEquals(expected, actual);
    }

}