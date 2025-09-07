package hexlet.code;

import java.util.List;

public class StylishFormatter implements Formatter {
    @Override
    public String format(List<DiffEntry> diffEntries) {
        StringBuilder result = new StringBuilder("{\n");

        for (DiffEntry entry : diffEntries) {
            switch (entry.getStatus()) {
                case UNCHANGED:
                    result.append("    ").append(entry.getKey())
                            .append(": ").append(entry.getNewValue()).append("\n");
                    break;

                case CHANGED:
                    result.append("  - ").append(entry.getKey())
                            .append(": ").append(entry.getOldValue()).append("\n");
                    result.append("  + ").append(entry.getKey())
                            .append(": ").append(entry.getNewValue()).append("\n");
                    break;

                case REMOVED:
                    result.append("  - ").append(entry.getKey())
                            .append(": ").append(entry.getOldValue()).append("\n");
                    break;

                case ADDED:
                    result.append("  + ").append(entry.getKey())
                            .append(": ").append(entry.getNewValue()).append("\n");
                    break;
                default:
                    break;
            }
        }

        result.append("}");
        return result.toString();
    }
}
