package kz.iitu.diploma_resource_server.util.sql;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class FieldSetterMapCollector {

    private static final ConcurrentHashMap<Class<?>, ClassSetter> classSetterMap = new ConcurrentHashMap<>();

    public static ClassSetter get(Class<?> theClass) {
        return classSetterMap.computeIfAbsent(theClass, FieldSetterMapCollector::createClassSetter);
    }

    private static ClassSetter createClassSetter(Class<?> theClass) {

        Map<String, FieldSetter> fieldSetters = new HashMap<>();

        for (final Field field : theClass.getFields()) {
            fieldSetters.put(field.getName(), (destination, value) -> {
                try {
                    field.set(destination, convertValue(value, field.getType()));
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            });
        }

        for (final Method method : theClass.getMethods()) {

            if (method.getParameterCount() != 1) {
                continue;
            }

            var methodName = method.getName();

            if (!methodName.startsWith("set")) {
                continue;
            }

            if (methodName.length() == 3) {
                continue;
            }

            var first      = methodName.substring(3, 4);
            var firstLower = first.toLowerCase();
            if (first.equals(firstLower)) {
                continue;
            }

            var name = firstLower + methodName.substring(4);

            Class<?> parameterType = method.getParameterTypes()[0];

            fieldSetters.put(name, (destination, value) -> {
                try {
                    method.invoke(destination, convertValue(value, parameterType));
                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
            });

        }

        return new ClassSetter(theClass, fieldSetters);
    }

    private static Object convertValue(Object value, Class<?> type) {
        if (value == null) {
            if (type == int.class) {
                return 0;
            }
            if (type == long.class) {
                return 0L;
            }
            if (type == double.class) {
                return 0D;
            }
            if (type == float.class) {
                return 0F;
            }
            if (type == short.class) {
                return (short) 0;
            }
            if (type == char.class) {
                return (char) 0;
            }
            if (type == byte.class) {
                return (byte) 0;
            }
            if (type == boolean.class) {
                return false;
            }
        }

        if (value instanceof BigDecimal) {
            var bd = (BigDecimal) value;

            if (type == double.class || type == Double.class) {
                return bd.doubleValue();
            }
        }
        if (value instanceof Double) {
            if (type == BigDecimal.class) {
                return BigDecimal.valueOf((double) value);
            }
        }

        return value;
    }
}

