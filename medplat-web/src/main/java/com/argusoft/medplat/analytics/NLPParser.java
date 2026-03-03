package com.argusoft.medplat.analytics;

public class NLPParser {

    public static String parse(String input) {

        if (input == null || input.isEmpty()) {
            return "Invalid input";
        }

        input = input.toLowerCase();

        // Patients queries
        if (input.contains("patients")) {

            if (input.contains("above")) {
                int age = extractNumber(input);
                return "SELECT * FROM patients WHERE age > " + age;
            }

            if (input.contains("below")) {
                int age = extractNumber(input);
                return "SELECT * FROM patients WHERE age < " + age;
            }

            return "SELECT * FROM patients";
        }

        // Doctors queries
        if (input.contains("doctors")) {

            if (input.contains("in")) {
                String city = input.substring(input.indexOf("in") + 3).trim();
                return "SELECT * FROM doctors WHERE city = '" + city + "'";
            }

            return "SELECT * FROM doctors";
        }

        return "Unsupported query";
    }

    private static int extractNumber(String input) {
        String number = input.replaceAll("\\D+", "");
        if (number.isEmpty()) {
            return 0;
        }
        return Integer.parseInt(number);
    }
}