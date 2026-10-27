package lb7;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

public class Task4 {


    private static void processLines(Path path) throws IOException {
        try (Stream<String> lines =
                     Files.lines(path, StandardCharsets.UTF_8)) {

            long count = lines
                    .filter(line -> !line.isBlank())
                    .count();

            System.out.println("Количество непустых строк: " + count);
        }
    }

    private static Stream<String> brokenRead(Path path) throws IOException {
        try (Stream<String> lines =
                     Files.lines(path, StandardCharsets.UTF_8)) {
            return lines;
        }
    }

    public static void main(String[] args) throws IOException {
        Path path = Path.of("courses.txt");

        Files.writeString(
                path,
                "JAVA-101\nPYTHON-201\n\nCSHARP-301\n",
                StandardCharsets.UTF_8
        );

        System.out.println("1. Правильная обработка:");
        processLines(path);

        System.out.println("\n2. Ошибочный возврат Stream:");

        try (Stream<String> lines = brokenRead(path)) {
            lines.forEach(System.out::println);
        } catch (IllegalStateException e) {
            System.out.println("Ошибка: поток уже закрыт.");
            System.out.println("Причина: " + e.getMessage());
        }
    }
}