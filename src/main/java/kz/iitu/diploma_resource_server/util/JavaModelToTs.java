package kz.iitu.diploma_resource_server.util;

import kz.iitu.diploma_resource_server.model.*;
import kz.iitu.diploma_resource_server.model.event.Event;
import kz.iitu.diploma_resource_server.model.event.EventDetail;
import kz.iitu.diploma_resource_server.model.event.EventToSave;
import kz.iitu.diploma_resource_server.model.event.EventType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

public class JavaModelToTs {

    public static String convert(Class<?> modelClass) {
        var tsModelStringBuilder = new StringBuilder();
        tsModelStringBuilder.append("export interface ")
                .append(modelClass.getSimpleName())
                .append(" {\n");

        for (var field : modelClass.getDeclaredFields()) {
            tsModelStringBuilder.append("  ")
                    .append(field.getName())
                    .append(": ")
                    .append(defineDataType(field.getType()))
                    .append(";\n");
        }

        tsModelStringBuilder.append("}");
        return tsModelStringBuilder.toString();
    }

    private static <T> String defineDataType(T dataTypeClass) {
        if (dataTypeClass == String.class || dataTypeClass == char.class) {
            return "string";
        }
        if (dataTypeClass == boolean.class) {
            return "boolean";
        }
        if (dataTypeClass == int.class
                || dataTypeClass == byte.class
                || dataTypeClass == short.class
                || dataTypeClass == long.class
                || dataTypeClass == double.class
                || dataTypeClass == float.class
                || dataTypeClass == Integer.class
                || dataTypeClass == Byte.class
                || dataTypeClass == Short.class
                || dataTypeClass == Long.class
                || dataTypeClass == Double.class
                || dataTypeClass == Float.class) {
            return "number";
        }
        if (dataTypeClass == Date.class
                || dataTypeClass == Timestamp.class
                || dataTypeClass == LocalDate.class
                || dataTypeClass == LocalDateTime.class) {
            return "Date";
        }
        var split = dataTypeClass.toString().split("\\.");
        return split[split.length - 1];
    }

    public static void main(String[] args) {
        System.out.println(JavaModelToTs.convert(User.class));
    }
}
