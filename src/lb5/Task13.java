package lb5;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Task13 {

    private static class Title {
        private final String sourceValue;
        private final String displayValue;
        private final String searchKey;

        public Title(String sourceValue) {
            if (sourceValue == null) {
                throw new IllegalArgumentException("Название не должно быть null");
            }

            this.sourceValue = sourceValue;
            this.displayValue = normalizeDisplay(sourceValue);
            this.searchKey = displayValue.toLowerCase(Locale.ROOT);
        }

        private static String normalizeDisplay(String value) {
            return value.strip().replaceAll("(?U)\\s+", " ");
        }

        public String getSourceValue() {
            return sourceValue;
        }

        public String getDisplayValue() {
            return displayValue;
        }

        public String getSearchKey() {
            return searchKey;
        }
    }

    public static void main(String[] args) {
        List<String> sourceTitles = Arrays.asList(
                "  JAVA   101 ",
                "java 101",
                "Java   102",
                "Python Basics",
                " PYTHON   BASICS ",
                "Java  Programming",
                "java programming"
        );

        Map<String, Title> searchIndex =
                new LinkedHashMap<String, Title>();

        System.out.printf("%-28s | %-22s | %-22s%n",
                "Исходное значение", "Display value", "Search key");


        for (String source : sourceTitles) {
            Title current = new Title(source);
            System.out.printf("%-28s | %-22s | %-22s%n",
                    "\"" + current.getSourceValue() + "\"",
                    "\"" + current.getDisplayValue() + "\"",
                    "\"" + current.getSearchKey() + "\"");

            Title existing = searchIndex.get(current.getSearchKey());
            if (existing == null) {
                searchIndex.put(current.getSearchKey(), current);
            } else if (!existing.getSourceValue().equals(
                    current.getSourceValue())) {
                System.out.println("КОНФЛИКТ: разные исходные строки имеют " + "одинаковый search key.");
                System.out.println("  Первое название: \"" + existing.getSourceValue() + "\"");
                System.out.println("  Второе название: \"" + current.getSourceValue() + "\"");
                System.out.println("  Общий ключ: \"" + current.getSearchKey() + "\"");
            }
        }

        System.out.println("\nПоиск названия:");

        String query = "  JaVa    101 ";
        Title normalizedQuery = new Title(query);

        System.out.println("Запрос: \"" + query + "\"");
        System.out.println("Ключ запроса: "
                + normalizedQuery.getSearchKey());

        Title found = searchIndex.get(normalizedQuery.getSearchKey());

        if (found != null) {
            System.out.println("Найденное display value: " + found.getDisplayValue());
        } else {
            System.out.println("Название не найдено.");
        }
    }
}