package lb7;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.List;

public class Task2 {

    private static void writeLines(Path path, List<String> lines)
            throws IOException {

        if (Files.isDirectory(path)) {
            throw new IOException("Вместо файла указана директория: " + path);
        }

        try (BufferedWriter writer = Files.newBufferedWriter(
                path,
                StandardCharsets.UTF_8,
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING,
                StandardOpenOption.WRITE)) {

            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
        }
    }

    private static void processText(Path input, Path output)
            throws IOException {

        if (input.toAbsolutePath().normalize()
                .equals(output.toAbsolutePath().normalize())) {
            throw new IOException("Входной и выходной файлы должны различаться");
        }

        if (Files.isDirectory(input)) {
            throw new IOException("Вместо входного файла указана директория: "
                    + input);
        }

        if (Files.isDirectory(output)) {
            throw new IOException("Вместо выходного файла указана директория: "
                    + output);
        }

        long lineCount = 0;
        long charCount = 0;
        long codePointCount = 0;

        try (BufferedReader reader = Files.newBufferedReader(
                input, StandardCharsets.UTF_8);
             BufferedWriter writer = Files.newBufferedWriter(
                     output,
                     StandardCharsets.UTF_8,
                     StandardOpenOption.CREATE,
                     StandardOpenOption.TRUNCATE_EXISTING,
                     StandardOpenOption.WRITE)) {

            String line;

            while ((line = reader.readLine()) != null) {
                lineCount++;

                charCount += line.length();

                codePointCount += line.codePointCount(0, line.length());

                String processedLine = line.strip();

                writer.write(processedLine);
                writer.newLine();
            }
        }

        System.out.println("Обработка завершена.");
        System.out.println("Количество строк: " + lineCount);
        System.out.println("Количество UTF-16 кодовых единиц: " + charCount);
        System.out.println("Количество Unicode-символов: " + codePointCount);
        System.out.println("Результат сохранён: " + output);
    }

    public static void main(String[] args) throws IOException {
        Path input = Path.of("courses.txt");
        Path output = Path.of("processed-courses.txt");

        List<String> courses = Arrays.asList(
                " JAVA-101 — Основы Java ",
                "CSHARP-201 — Разработка на C#",
                "PYTHON-301 — Программирование на Python",
                " WEB-401 — Веб-разработка "
        );

        writeLines(input, courses);
        processText(input, output);
    }
}