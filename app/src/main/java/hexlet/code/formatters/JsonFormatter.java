package hexlet.code.formatters;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.core.util.DefaultIndenter;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import hexlet.code.DiffEntry;
import hexlet.code.Formatter;

import java.lang.reflect.Field;
import java.util.List;

public class JsonFormatter implements Formatter {
    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Override
    public String format(List<DiffEntry> diffEntries) {
        ArrayNode root = MAPPER.createArrayNode();

        for (DiffEntry entry : diffEntries) {
            ObjectNode node = MAPPER.createObjectNode();
            node.put("key", entry.getKey());
            node.put("status", entry.getStatus().name().toLowerCase());

            switch (entry.getStatus()) {
                case UNCHANGED:
                    node.set("value", convertValue(entry.getNewValue()));
                    break;
                case CHANGED:
                    node.set("oldValue", convertValue(entry.getOldValue()));
                    node.set("newValue", convertValue(entry.getNewValue()));
                    break;
                case ADDED:
                    node.set("value", convertValue(entry.getNewValue()));
                    break;
                case REMOVED:
                    node.set("value", convertValue(entry.getOldValue()));
                    break;
                default:
                    break;
            }

            root.add(node);
        }

        try {
            // Создаем кастомный PrettyPrinter
            DefaultPrettyPrinter printer = new DefaultPrettyPrinter();

            // Убираем пробел после двоеточия с помощью рефлексии
            try {
                Field field = DefaultPrettyPrinter.class.getDeclaredField("spacesInObjectEntries");
                field.setAccessible(true);
                field.setBoolean(printer, false);
            } catch (Exception e) {
                // Если не получилось, пытаемся использовать метод withoutSpacesInObjectEntries
                try {
                    java.lang.reflect.Method method;
                    method = DefaultPrettyPrinter.class.getMethod("withoutSpacesInObjectEntries");
                    printer = (DefaultPrettyPrinter) method.invoke(printer);
                } catch (Exception ex) {
                    // Если все методы не сработали, оставляем как есть
                }
            }

            // Настройка отступов
            DefaultIndenter indenter = new DefaultIndenter("    ", "\n");
            printer.indentArraysWith(indenter);
            printer.indentObjectsWith(indenter);

            return MAPPER.writer(printer).writeValueAsString(root);
        } catch (Exception e) {
            throw new RuntimeException("Failed to format JSON", e);
        }
    }

    private com.fasterxml.jackson.databind.JsonNode convertValue(Object value) {
        if (value == null) {
            return MAPPER.nullNode();
        }

        // Для простых типов используем стандартное преобразование
        if (value instanceof String
            || value instanceof Boolean
            || value instanceof Number) {
            return MAPPER.valueToTree(value);
        }

        // Для коллекций и других объектов пытаемся преобразовать в JSON
        try {
            return MAPPER.valueToTree(value);
        } catch (Exception e) {
            // Если не удалось преобразовать, используем toString()
            return MAPPER.createObjectNode().put("value", value.toString());
        }
    }
}
