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

import java.util.*;
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
            // Добавляем проверку на одинаковое расширение файлов
            validateFileExtensions();

            System.out.println("Reading file 1: " + filepath1);
            String file1Content = readFileContent(filepath1);
            System.out.println("Reading file 2: " + filepath2);
            String file2Content = readFileContent(filepath2);

            System.out.println("Using format: " + format);
            System.out.println("\nFile 1 content:\n" + file1Content);
            System.out.println("File 2 content:\n" + file2Content);

            // Парсинг происходит внутри try-блока
            Map<String, Object> data1 = Parser.parseFileToMap(file1Content, filepath1);
            Map<String, Object> data2 = Parser.parseFileToMap(file2Content, filepath2);

            String diff = generateDiff(data1, data2, format);
            System.out.println(diff);
            return 0; // Успешное завершение

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            return 1; // Код ошибки
        }

    }

    /**
     * Проверяет, что оба файла имеют одинаковое расширение.
     */
    private void validateFileExtensions() throws IOException {
        String ext1 = getFileExtension(filepath1);
        String ext2 = getFileExtension(filepath2);

        if (!ext1.equals(ext2)) {
            throw new IOException("Files must have the same extension. "
                    + "First file has ." + ext1 + " extension, "
                    + "second file has ." + ext2 + " extension");
        }

        // Дополнительная проверка на поддерживаемые форматы
        if (!ext1.equals("json") && !ext1.equals("yaml") && !ext1.equals("yml")) {
            throw new IOException("Unsupported file format: ." + ext1
                    + ". Supported formats are: json, yaml, yml");
        }
    }

    /**
     * @param filePath Путь к файлу.
     * @return Расширение файла в нижнем регистре.
     */
    private String getFileExtension(String filePath) {
        Path path = Paths.get(filePath);
        String fileName = path.getFileName().toString();

        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex > 0 && dotIndex < fileName.length() - 1) {
            return fileName.substring(dotIndex + 1).toLowerCase();
        }
        return "";
    }


    public static String generateDiff(Map<String, Object> data1, Map<String, Object> data2) {
        return generateDiff(data1, data2, "stylish");
    }

    public static String generateDiff(Map<String, Object> data1, Map<String, Object> data2, String formatType) {
        DiffBuilder diffBuilder = new DiffBuilder();
        List<DiffEntry> diffEntries = diffBuilder.buildDiff(data1, data2);

        Formatter formatter = FormatterFactory.create(formatType);
        return formatter.format(diffEntries);
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

}
