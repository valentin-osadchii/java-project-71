package hexlet.code;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class YamlDiffTest {
    private final ObjectMapper yamlObjectMapper = new ObjectMapper(
            new com.fasterxml.jackson.dataformat.yaml.YAMLFactory()
    );
    // Базовый тест для YAML файлов из задания
    @Test
    void testYamlFilesDiff() throws Exception {
        // Загружаем тестовые данные
        String beforeYaml = Files.readString(
                Path.of("src/test/resources/file1nested.yaml")
        );
        String afterYaml = Files.readString(
                Path.of("src/test/resources/file2nested.yaml")
        );

        // Парсим JSON в Map
        Map<String, Object> before = yamlObjectMapper.readValue(beforeYaml,
                new com.fasterxml.jackson.core.type.TypeReference<Map<String, Object>>() { });
        Map<String, Object> after = yamlObjectMapper.readValue(afterYaml,
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

        assertEquals(normalizeSpaces(expected),  normalizeSpaces(actual));
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
