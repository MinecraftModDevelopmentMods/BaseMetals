package zone.moddev.mc.basemetals.content;

import java.util.Objects;
import java.util.function.Supplier;

/** Holds an entry until its Forge registration event supplies the instance. */
public final class RegistryHandle<T> implements Supplier<T> {
    private final String id;
    private T value;

    public RegistryHandle(String id) {
        this.id = Objects.requireNonNull(id, "id");
    }

    public String id() {
        return id;
    }

    public void bind(T registeredValue) {
        if (value != null) {
            throw new IllegalStateException("Registry handle already bound: " + id);
        }
        value = Objects.requireNonNull(registeredValue, "registeredValue");
    }

    @Override
    public T get() {
        if (value == null) {
            throw new IllegalStateException("Registry handle has not been bound: " + id);
        }
        return value;
    }
}
