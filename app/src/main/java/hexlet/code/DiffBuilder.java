package hexlet.code;


import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.ArrayList;
import java.util.Objects;

public final class DiffBuilder {
    public List<DiffEntry> buildDiff(Map<String, Object> data1, Map<String, Object> data2) {
        Set<String> uniqueKeys = new TreeSet<>(data1.keySet());
        uniqueKeys.addAll(data2.keySet());

        List<DiffEntry> diffEntries = new ArrayList<>();

        for (String key : uniqueKeys) {
            boolean inFirst = data1.containsKey(key);
            boolean inSecond = data2.containsKey(key);

            if (inFirst && inSecond) {
                Object value1 = data1.get(key);
                Object value2 = data2.get(key);
                if (Objects.equals(value1, value2)) {
                    diffEntries.add(new DiffEntry(key, DiffStatus.UNCHANGED, value1, value2));
                } else {
                    diffEntries.add(new DiffEntry(key, DiffStatus.CHANGED, value1, value2));
                }
            } else if (inFirst) {
                diffEntries.add(new DiffEntry(key, DiffStatus.REMOVED, data1.get(key), null));
            } else {
                diffEntries.add(new DiffEntry(key, DiffStatus.ADDED, null, data2.get(key)));
            }
        }

        return diffEntries;
    }
}
