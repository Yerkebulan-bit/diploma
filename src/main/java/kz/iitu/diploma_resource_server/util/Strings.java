package kz.iitu.diploma_resource_server.util;

public class Strings {

    public static void requiresNotNullOrEmpty(String str) {
        if (str == null || str.isEmpty()) {
            throw new NullPointerException();
        }
    }

}
