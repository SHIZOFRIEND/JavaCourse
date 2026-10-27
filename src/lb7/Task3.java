package lb7;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class Task3 {

    private static class LoggingReader extends BufferedReader {
        public LoggingReader(Reader reader) {
            super(reader);
        }

        @Override
        public void close() throws IOException {
            System.out.println("Закрывается Reader");
            super.close();
            System.out.println("Reader закрыт");
        }
    }

    private static class LoggingWriter extends BufferedWriter {
        public LoggingWriter(Writer writer) {
            super(writer);
        }

        @Override
        public void close() throws IOException {
            System.out.println("Закрывается Writer");
            super.close();
            System.out.println("Writer закрыт");
        }
    }

    private static void process(Path input, Path output)
            throws IOException {

        try (LoggingReader reader = new LoggingReader(
                Files.newBufferedReader(input, StandardCharsets.UTF_8));
             LoggingWriter writer = new LoggingWriter(
                     Files.newBufferedWriter(
                             output,
                             StandardCharsets.UTF_8,
                             StandardOpenOption.CREATE,
                             StandardOpenOption.TRUNCATE_EXISTING))) {

            String line;
            int lineNumber = 0;

            while ((line = reader.readLine()) != null) {
                lineNumber++;

                writer.write(line);
                writer.newLine();

                if (lineNumber == 2) {
                    throw new IOException(
                            "Искусственная ошибка при обработке второй строки");
                }
            }
        }
    }

    public static void main(String[] args) throws IOException {
        Path input = Path.of("input.txt");
        Path output = Path.of("output.txt");

        Files.writeString(
                input,
                "Первая строка\nВторая строка\nТретья строка\n",
                StandardCharsets.UTF_8
        );

        try {
            process(input, output);
            System.out.println("Обработка успешно завершена");
        } catch (IOException e) {
            System.out.println("Обработка прервана: " + e.getMessage());
        }

        System.out.println("\nПроверка результата:");
        System.out.println("Входной файл существует: " + Files.exists(input));
        System.out.println("Выходной файл существует: " + Files.exists(output));
        System.out.println("Выходной файл содержит:");
        System.out.println(Files.readString(output, StandardCharsets.UTF_8));
    }
}