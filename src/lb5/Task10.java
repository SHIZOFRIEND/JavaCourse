package lb5;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.Locale;
import java.util.regex.Pattern;

public class Task10 {

    private static final Pattern INPUT_FORMAT =
            Pattern.compile("[0-9]{2}\\.[0-9]{2}\\.[0-9]{4}");

    private static final DateTimeFormatter FORMATTER =
            new DateTimeFormatterBuilder().appendPattern("dd.MM.uuuu").toFormatter(Locale.ROOT).withResolverStyle(ResolverStyle.STRICT);

    public static LocalDate parseDate(String field, String value) {
        if (value == null || !INPUT_FORMAT.matcher(value).matches()) {
            throw new IllegalArgumentException("Поле '" + field + "': ожидается формат dd.MM.uuuu; исходное значение: '" + value + "'");
        }
        try {
            return LocalDate.parse(value, FORMATTER);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Поле '" + field + "': некорректная дата; исходное значение: '" + value + "'", e
            );
        }
    }

    public static void main(String[] args) {
        String[] testDates = {
                "19.10.2026",
                "29.02.2024",
                "29.02.2025",
                "31.04.2026",
                "32.01.2026",
                "15.13.2026",
                "1.01.2026",
                "01.1.2026",
                "01.01.26",
                "01.01.2026 extra",
                " 01.01.2026",
                "01.01.2026 ",
                ""
        };

        for (String value : testDates) {
            try {
                LocalDate date = parseDate("courseStartDate", value);
                System.out.println("Корректно: '" + value + "' -> " + date);
            } catch (IllegalArgumentException e) {
                System.out.println("Ошибка: " + e.getMessage());
                if (e.getCause() != null) {
                    System.out.println("Причина: " + e.getCause().getMessage());
                }
            }
        }
    }
}