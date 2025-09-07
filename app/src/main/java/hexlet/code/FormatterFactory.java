package hexlet.code;

public class FormatterFactory {
    public static Formatter create(String formatType) {
        return switch (formatType.toLowerCase()) {
            case "stylish" -> new StylishFormatter();
            // В будущем можно добавить поддержку других форматов:
            // case "json" -> new JsonFormatter();
            // case "yaml" -> new YamlFormatter();
            default -> throw new IllegalArgumentException("Unknown format: " + formatType);
        };
    }
}
