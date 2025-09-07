package hexlet.code.formatters;

import hexlet.code.DiffEntry;
import hexlet.code.DiffStatus;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Collections;
import java.util.LinkedHashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;

class JsonFormatterTest {

    @Test
    void testFormatWithAllStatuses()
            throws IOException, URISyntaxException {
        // Создаем тестовые данные с разными статусами

        Map<String, Object> obj1Map = new LinkedHashMap<>();
        obj1Map.put("nestedKey", "value");
        obj1Map.put("isNested", true);

        List<DiffEntry> diffEntries = Arrays.asList(
                new DiffEntry("chars1",
                        DiffStatus.UNCHANGED,
                        Arrays.asList("a", "b", "c"),
                        Arrays.asList("a", "b", "c")),
                new DiffEntry("chars2", DiffStatus.CHANGED, Arrays.asList("d", "e", "f"), false),
                new DiffEntry("checked", DiffStatus.CHANGED, false, true),
                new DiffEntry("default", DiffStatus.CHANGED, null, Arrays.asList("value1", "value2")),
                new DiffEntry("id", DiffStatus.CHANGED, 45, null),
                new DiffEntry("key1", DiffStatus.REMOVED, "value1", null),
                new DiffEntry("key2", DiffStatus.ADDED, null, "value2"),
                new DiffEntry("numbers1", DiffStatus.UNCHANGED, Arrays.asList(1, 2, 3, 4), Arrays.asList(1, 2, 3, 4)),
                new DiffEntry("numbers2", DiffStatus.CHANGED, Arrays.asList(2, 3, 4, 5), Arrays.asList(22, 33, 44, 55)),
                new DiffEntry("numbers3", DiffStatus.REMOVED, Arrays.asList(3, 4, 5), null),
                new DiffEntry("numbers4", DiffStatus.ADDED, null, Arrays.asList(4, 5, 6)),
                new DiffEntry("obj1", DiffStatus.ADDED, null, obj1Map),
                new DiffEntry("setting1", DiffStatus.CHANGED, "Some value", "Another value"),
                new DiffEntry("setting2", DiffStatus.CHANGED, 200, 300),
                new DiffEntry("setting3", DiffStatus.CHANGED, true, "none")
        );

        // Загружаем ожидаемый результат из файла ресурсов
        Path expectedFilePath = Paths.get(
                Objects.requireNonNull(getClass().getClassLoader().getResource("expectedJsonDiff.json")).toURI()
        );
        String expected = Files.readString(expectedFilePath);


        JsonFormatter formatter = new JsonFormatter();
        String actual = formatter.format(diffEntries);

        // Сравниваем результаты, удаляя все пробелы и переносы строк
        assertEquals(expected.replaceAll("\\s", ""), actual.replaceAll("\\s", ""));
    }


    @Test
    void testFormatEmptyDiff() {
        List<DiffEntry> diffEntries = Collections.emptyList();
        String expected = "[]";

        JsonFormatter formatter = new JsonFormatter();
        String actual = formatter.format(diffEntries);

        assertEquals(expected.replaceAll("\\s", ""), actual.replaceAll("\\s", ""));
    }

    @Test
    void testFormatUnchangedStatus() {
        List<DiffEntry> diffEntries = Collections.singletonList(
                new DiffEntry("unchangedKey", DiffStatus.UNCHANGED, "value", "value")
        );

        String expected = """
            [
                {
                    "key":"unchangedKey",
                    "status":"unchanged",
                    "value":"value"
                }
            ]""";

        JsonFormatter formatter = new JsonFormatter();
        String actual = formatter.format(diffEntries);

        assertEquals(expected.replaceAll("\\s", ""), actual.replaceAll("\\s", ""));
    }

    @Test
    void testFormatAddedStatus() {
        List<DiffEntry> diffEntries = Collections.singletonList(
                new DiffEntry("addedKey", DiffStatus.ADDED, null, "newValue")
        );

        String expected = """
            [
                {
                    "key":"addedKey",
                    "status":"added",
                    "value":"newValue"
                }
            ]""";

        JsonFormatter formatter = new JsonFormatter();
        String actual = formatter.format(diffEntries);

        assertEquals(expected.replaceAll("\\s", ""), actual.replaceAll("\\s", ""));
    }

    @Test
    void testFormatRemovedStatus() {
        List<DiffEntry> diffEntries = Collections.singletonList(
                new DiffEntry("removedKey", DiffStatus.REMOVED, "oldValue", null)
        );

        String expected = """
            [
                {
                    "key":"removedKey",
                    "status":"removed",
                    "value":"oldValue"
                }
            ]""";

        JsonFormatter formatter = new JsonFormatter();
        String actual = formatter.format(diffEntries);

        assertEquals(expected.replaceAll("\\s", ""), actual.replaceAll("\\s", ""));
    }

    @Test
    void testFormatChangedStatus() {
        List<DiffEntry> diffEntries = Collections.singletonList(
                new DiffEntry("changedKey", DiffStatus.CHANGED, "oldValue", "newValue")
        );

        String expected = """
            [
                {
                    "key":"changedKey",
                    "status":"changed",
                    "oldValue":"oldValue",
                    "newValue":"newValue"
                }
            ]""";

        JsonFormatter formatter = new JsonFormatter();
        String actual = formatter.format(diffEntries);

        assertEquals(expected.replaceAll("\\s", ""), actual.replaceAll("\\s", ""));
    }
}
