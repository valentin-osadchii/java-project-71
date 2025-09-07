package hexlet.code.formatters;

import hexlet.code.DiffEntry;
import hexlet.code.Formatter;
import java.util.List;

public class PlainFormatter implements Formatter {
    @Override
    public String format(List<DiffEntry> diffEntries) {
        StringBuilder result = new StringBuilder();

        for (DiffEntry entry : diffEntries) {
            switch (entry.getStatus()) {
                case UNCHANGED:
                    // В требуемом формате нечего выводить для неизмененных свойств
                    break;

                case CHANGED:
                    result.append("Property '").append(entry.getKey())
                            .append("' was updated. From ")
                            .append(formatValue(entry.getOldValue()))
                            .append(" to ")
                            .append(formatValue(entry.getNewValue()))
                            .append("\n");
                    break;

                case REMOVED:
                    result.append("Property '").append(entry.getKey())
                            .append("' was removed\n");
                    break;

                case ADDED:
                    result.append("Property '").append(entry.getKey())
                            .append("' was added with value: ")
                            .append(formatValue(entry.getNewValue()))
                            .append("\n");
                    break;

                default:
                    break;
            }
        }

        // Удаляем последний символ новой строки, если он есть
        if (result.length() > 0) {
            result.setLength(result.length() - 1);
        }

        return result.toString();
    }

    private String formatValue(Object value) {
        if (value == null) {
            return "null";
        }

        // Проверяем, является ли значение сложным (Map или List)
        if (value instanceof java.util.Map || value instanceof java.util.List) {
            return "[complex value]";
        }

        // Строки оборачиваем в кавычки
        if (value instanceof String) {
            return "'" + value + "'";
        }

        // Для всех остальных типов (числа, булевы) возвращаем как есть
        return value.toString();
    }
}
