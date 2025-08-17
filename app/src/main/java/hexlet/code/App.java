package hexlet.code;

import picocli.CommandLine;
import picocli.CommandLine.Option;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;

@Command(
        name = "gendiff",
        description = "Compares two configuration files and shows a difference.",
        mixinStandardHelpOptions = true,  // Автоматически добавляет --help и --version
        version = "gendiff 1.0"           // Версия для опции --version
)

public class App implements Callable<Integer> {

    @Parameters(index = "0", description = "path to first file")
    private String filepath1;

    @Parameters(index = "1", description = "path to second file")
    private String filepath2;

    @Option(names = { "-f", "--format" },
            description = "output format [default: stylish]",
            defaultValue = "stylish"
    )
    private String format;

    public static void main(String[] args) {
        int exitCode = new CommandLine(new App()).execute(args);
        System.exit(exitCode);
    }


    @Override
    public Integer call() throws Exception {
        try {
            System.out.println("Reading file 1: " + filepath1);
            String file1Content = readFileContent(filepath1);
            System.out.println("Reading file 2: " + filepath2);
            String file2Content = readFileContent(filepath2);

            System.out.println("Using format: " + format);
            System.out.println("\nFile 1 content:\n" + file1Content);
            System.out.println("File 2 content:\n" + file2Content);

            // Парсинг происходит внутри try-блока
            Map<String, Object> data1 = parseJsonToMap(file1Content, filepath1);
            Map<String, Object> data2 = parseJsonToMap(file2Content, filepath2);

            String diff = generateDiff(data1, data2);
            System.out.println(diff);
            return 0; // Успешное завершение

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            return 1; // Код ошибки
        }

    }

    public static String generateDiff(Map<String, Object> data1, Map<String, Object> data2) {
        Set<String> uniqueKeys = new HashSet<>();
        uniqueKeys.addAll(data1.keySet());
        uniqueKeys.addAll(data2.keySet());

        StringBuilder result = new StringBuilder("{\n");

        for (String key : uniqueKeys.stream().sorted().toList()) {
            boolean inFirst = data1.containsKey(key);
            boolean inSecond = data2.containsKey(key);

            if (inFirst && inSecond) {
                Object value1 = data1.get(key);
                Object value2 = data2.get(key);
                if (value1.equals(value2)) {
                    result.append("    ").append(key).append(": ").append(value1).append("\n");
                } else {
                    result.append("  - ").append(key).append(": ").append(value1).append("\n");
                    result.append("  + ").append(key).append(": ").append(value2).append("\n");
                }
            } else if (inFirst) {
                result.append("  - ").append(key).append(": ").append(data1.get(key)).append("\n");
            } else {
                result.append("  + ").append(key).append(": ").append(data2.get(key)).append("\n");
            }
        }
        result.append("}");
        return result.toString();
    }

    private String readFileContent(String filePath) throws IOException {
        // Преобразуем путь в абсолютный и нормализуем (разрешаем .. и .)
        Path path = Paths.get(filePath).toAbsolutePath().normalize();

        // Проверка существования файла
        if (!Files.exists(path)) {
            throw new IOException("File not found: " + filePath);
        }

        // Проверка, что это файл, а не директория
        if (!Files.isRegularFile(path)) {
            throw new IOException("Path is not a file: " + filePath);
        }

        // Чтение содержимого с явным указанием кодировки
        return new String(Files.readAllBytes(path), StandardCharsets.UTF_8);
    }

    private Map<String, Object> parseJsonToMap(String jsonContent, String fileName) throws IOException {
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
