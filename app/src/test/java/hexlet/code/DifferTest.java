package hexlet.code;

import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DifferTest {
    @Test
    void testJsonStylishFormat() throws Exception {
        String filepath1 = "src/test/resources/file1.json";
        String filepath2 = "src/test/resources/file2.json";

        String actual = Differ.generate(filepath1, filepath2, "stylish");

        String expected = Files.readString(
                Path.of("src/test/resources/expectedDiffStylishFormat.txt")
        ).trim();

        assertEquals(normalizeSpaces(expected), normalizeSpaces(actual));
    }

    @Test
    void testJsonJsonFormat() throws Exception {
        String filepath1 = "src/test/resources/file1.json";
        String filepath2 = "src/test/resources/file2.json";

        String actual = Differ.generate(filepath1, filepath2, "json");

        String expected = Files.readString(
                Path.of("src/test/resources/expectedDiffJsonFormat.txt")
        ).trim();

        assertEquals(normalizeSpaces(expected), normalizeSpaces(actual));
    }

    @Test
    void testJsonPlainFormat() throws Exception {
        String filepath1 = "src/test/resources/file1.json";
        String filepath2 = "src/test/resources/file2.json";

        String actual = Differ.generate(filepath1, filepath2, "plain");

        String expected = Files.readString(
                Path.of("src/test/resources/expectedDiffPlainFormat.txt")
        ).trim();

        assertEquals(normalizeSpaces(expected), normalizeSpaces(actual));
    }

    @Test
    void testJsonDefaultFormat() throws Exception {
        String filepath1 = "src/test/resources/file1.json";
        String filepath2 = "src/test/resources/file2.json";

        String actual = Differ.generate(filepath1, filepath2);

        String expected = Files.readString(
                Path.of("src/test/resources/expectedDiffStylishFormat.txt")
        ).trim();

        assertEquals(normalizeSpaces(expected), normalizeSpaces(actual));
    }

    @Test
    void testYamlStylishFormat() throws Exception {
        String filepath1 = "src/test/resources/file1.yaml";
        String filepath2 = "src/test/resources/file2.yaml";

        String actual = Differ.generate(filepath1, filepath2, "stylish");

        String expected = Files.readString(
                Path.of("src/test/resources/expectedDiffStylishFormat.txt")
        ).trim();

        assertEquals(normalizeSpaces(expected), normalizeSpaces(actual));
    }

    @Test
    void testYamlJsonFormat() throws Exception {
        String filepath1 = "src/test/resources/file1.yaml";
        String filepath2 = "src/test/resources/file2.yaml";

        String actual = Differ.generate(filepath1, filepath2, "json");

        String expected = Files.readString(
                Path.of("src/test/resources/expectedDiffJsonFormat.txt")
        ).trim();

        assertEquals(normalizeSpaces(expected), normalizeSpaces(actual));
    }

    @Test
    void testYamlPlainFormat() throws Exception {
        String filepath1 = "src/test/resources/file1.yaml";
        String filepath2 = "src/test/resources/file2.yaml";

        String actual = Differ.generate(filepath1, filepath2, "plain");

        String expected = Files.readString(
                Path.of("src/test/resources/expectedDiffPlainFormat.txt")
        ).trim();

        assertEquals(normalizeSpaces(expected), normalizeSpaces(actual));
    }

    @Test
    void testYamlDefaultFormat() throws Exception {
        String filepath1 = "src/test/resources/file1.yaml";
        String filepath2 = "src/test/resources/file2.yaml";

        String actual = Differ.generate(filepath1, filepath2);

        String expected = Files.readString(
                Path.of("src/test/resources/expectedDiffStylishFormat.txt")
        ).trim();

        assertEquals(normalizeSpaces(expected), normalizeSpaces(actual));
    }

    private String normalizeSpaces(String input) {
        return input.replaceAll("\\s+", " ").trim();
    }
}
