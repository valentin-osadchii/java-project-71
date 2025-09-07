package hexlet.code;

import hexlet.code.formatters.JsonFormatter;
import hexlet.code.formatters.PlainFormatter;
import hexlet.code.formatters.StylishFormatter;

public class FormatterFactory {
    public static Formatter create(String formatType) {
        return switch (formatType.toLowerCase()) {
            case "stylish" -> new StylishFormatter();
            case "plain" -> new PlainFormatter();
            case "json" -> new JsonFormatter();
            // В будущем можно добавить поддержку других форматов:
            // case "json" -> new JsonFormatter();
            // case "yaml" -> new YamlFormatter();
            default -> throw new IllegalArgumentException("Unknown format: " + formatType);
        };
    }
}
