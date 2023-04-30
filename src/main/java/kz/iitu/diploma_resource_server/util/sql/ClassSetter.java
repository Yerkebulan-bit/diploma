package kz.iitu.diploma_resource_server.util.sql;

import java.util.Map;
import java.util.Set;

import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toMap;

public class ClassSetter {

    private final Class<?> theClass;

    private final Map<String, FieldSetter> fieldSetters;
    private final Map<String, FieldSetter> fieldSettersLower;

    public ClassSetter(Class<?> theClass, Map<String, FieldSetter> fieldSetters) {
        this.theClass = theClass;
        this.fieldSetters = Map.copyOf(fieldSetters);
        fieldSettersLower = this.fieldSetters.entrySet()
                .stream()
                .collect(toMap(x -> x.getKey().toLowerCase(),
                        Map.Entry::getValue,
                        (a, b) -> b));
    }

    public FieldSetter get(String fieldName) {
        requireNonNull(fieldName);
        {
            var fieldSetter = fieldSetters.get(fieldName);
            if (fieldSetter != null) {
                return fieldSetter;
            }
        }
        {
            var fieldSetter = fieldSettersLower.get(fieldName.toLowerCase());
            if (fieldSetter == null) {
                throw new RuntimeException("352CC3QyAK :: No field setter `" + fieldName + "` in " + theClass);
            }
            return fieldSetter;
        }
    }

    public Set<String> fields() {
        return fieldSetters.keySet();
    }
}

