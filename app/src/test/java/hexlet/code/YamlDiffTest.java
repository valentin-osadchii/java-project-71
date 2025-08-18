package hexlet.code;

import org.junit.jupiter.api.Test;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class YamlDiffTest {

    // Базовый тест для YAML файлов из задания
    @Test
    void testYamlFilesDiff() {
        Map<String, Object> yaml1 = Map.of(
                "host", "hexlet.io",
                "timeout", 50,
                "proxy", "123.234.53.22",
                "follow", false
        );

        Map<String, Object> yaml2 = Map.of(
                "timeout", 20,
                "verbose", true,
                "host", "hexlet.io"
        );

        String expected = """
            {
                - follow: false
                  host: hexlet.io
                - proxy: 123.234.53.22
                - timeout: 50
                + timeout: 20
                + verbose: true
            }""";

        assertEquals(normalizeSpaces(expected),
                normalizeSpaces(App.generateDiff(yaml1, yaml2)));
    }

    // Тест для одинаковых YAML файлов
    @Test
    void testIdenticalYamlFiles() {
        Map<String, Object> data1 = Map.of(
                "server", "localhost",
                "port", 8080,
                "enabled", true
        );

        Map<String, Object> data2 = Map.of(
                "server", "localhost",
                "port", 8080,
                "enabled", true
        );

        String expected = """
            {
                enabled: true
                port: 8080
                server: localhost
            }""";

        assertEquals(normalizeSpaces(expected),
                normalizeSpaces(App.generateDiff(data1, data2)));
    }

    // Тест для YAML файлов с разным порядком ключей
    @Test
    void testYamlDifferentOrder() {
        Map<String, Object> data1 = Map.of(
                "z", "last",
                "a", "first",
                "m", "middle"
        );

        Map<String, Object> data2 = Map.of(
                "m", "middle",
                "z", "last",
                "a", "first"
        );

        String expected = """
            {
                a: first
                m: middle
                z: last
            }""";

        assertEquals(normalizeSpaces(expected),
                normalizeSpaces(App.generateDiff(data1, data2)));
    }

    // Тест для YAML файлов с разными типами данных
    @Test
    void testYamlDifferentDataTypes() {
        Map<String, Object> data1 = Map.of(
                "count", 10,
                "active", true,
                "name", "test"
        );

        Map<String, Object> data2 = Map.of(
                "count", "ten",
                "active", false,
                "name", "test"
        );

        String expected = """
            {
                - active: true
                + active: false
                - count: 10
                + count: ten
                  name: test
            }""";

        assertEquals(normalizeSpaces(expected),
                normalizeSpaces(App.generateDiff(data1, data2)));
    }

    // Тест для одного пустого YAML файла
    @Test
    void testEmptyYamlFile() {
        Map<String, Object> data1 = Map.of();
        Map<String, Object> data2 = Map.of(
                "config", "value",
                "debug", true
        );

        String expected = """
            {
                + config: value
                + debug: true
            }""";

        assertEquals(normalizeSpaces(expected),
                normalizeSpaces(App.generateDiff(data1, data2)));
    }

    // Тест для YAML с вложенными структурами (плоские файлы, но проверка обработки простых структур)
    @Test
    void testYamlSimpleStructures() {
        Map<String, Object> data1 = Map.of(
                "user", Map.of("name", "John", "age", 30),
                "active", true
        );

        Map<String, Object> data2 = Map.of(
                "user", Map.of("name", "John", "age", 31),
                "active", true
        );

        String actual = normalizeSpaces(App.generateDiff(data1, data2));

        // Проверяем основные компоненты, не завися от порядка
        assertTrue(actual.contains("- user: {"));
        assertTrue(actual.contains("+ user: {"));
        assertTrue(actual.contains("name=John"));
        assertTrue(actual.contains("age=30"));
        assertTrue(actual.contains("age=31"));
        assertTrue(actual.contains("active: true"));
    }

    // Вспомогательный метод для нормализации пробелов
    private String normalizeSpaces(String input) {
        return input.replaceAll("\\s+", " ").trim();
    }
}
