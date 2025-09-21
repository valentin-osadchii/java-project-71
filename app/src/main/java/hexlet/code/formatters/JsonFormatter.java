package hexlet.code.formatters;

import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.DiffEntry;
import hexlet.code.Formatter;

import java.util.List;
public class JsonFormatter implements Formatter {
    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Override
    public String format(List<DiffEntry> diffEntries) {
        try {
            return MAPPER.writeValueAsString(diffEntries);
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate JSON", e);
        }
    }
}
