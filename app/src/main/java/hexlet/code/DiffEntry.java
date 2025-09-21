package hexlet.code;

import java.util.Objects;

public final class DiffEntry {
    private final String key;
    private final DiffStatus status;
    private final Object oldValue;
    private final Object newValue;

    public DiffEntry(String key, DiffStatus status, Object oldValue, Object newValue) {
        this.key = key;
        this.status = status;
        this.oldValue = oldValue;
        this.newValue = newValue;
    }

    // Геттеры
    public String getKey() {
        return key;
    }

    public DiffStatus getStatus() {
        return status;
    }

    public Object getOldValue() {
        return oldValue;
    }

    public Object getNewValue() {
        return newValue;
    }

    // Переопределение equals
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DiffEntry diffEntry = (DiffEntry) o;
        return Objects.equals(key, diffEntry.key)
                && status == diffEntry.status
                && Objects.equals(oldValue, diffEntry.oldValue)
                && Objects.equals(newValue, diffEntry.newValue);
    }

    // Переопределение hashCode
    @Override
    public int hashCode() {
        return Objects.hash(key, status, oldValue, newValue);
    }

}
