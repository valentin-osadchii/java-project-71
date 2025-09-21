package hexlet.code;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

public class Differ {
    /**
     * Проверяет, что оба файла имеют одинаковое расширение.
     * @param filepath1
     * @param filepath2
     */
    static void validateFileExtensions(String filepath1, String filepath2) throws IOException {
        String ext1 = getFileExtension(filepath1);
        String ext2 = getFileExtension(filepath2);

        if (!ext1.equals(ext2)) {
            throw new IOException("Files must have the same extension. "
                    + "First file has ." + ext1 + " extension, "
                    + "second file has ." + ext2 + " extension");
        }

        isSupportedFormat(ext1);
    }

    private static void isSupportedFormat(String ext1) throws IOException {
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
    private static String getFileExtension(String filePath) {
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

    static String readFileContent(String filePath) throws IOException {
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

    static void generate(String filepath1, String filepath2, String format) throws IOException {
        validateFileExtensions(filepath1, filepath2);

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
    }
}
