package com.automation.framework.reports;

public class TestNGDescription {

    private static ThreadLocal<String> description =
            new ThreadLocal<>();


    public static void setDescription(String value) {

        description.set(value);
    }


    public static String getDescription() {

        String value = description.get();

        if (value == null || value.trim().isEmpty()) {

            return "N/A";
        }

        return value;
    }


    public static void clear() {

        description.remove();
    }
}