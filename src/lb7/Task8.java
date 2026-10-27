package lb7;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.PushbackReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
public class Task8 {

    private static final int MAX_LINE_LENGTH = 4096;
    private static final int MAX_REPORTED_ERRORS = 10;
    private static final int CANCEL_AFTER_ERRORS = 100;

    private enum Status {
        SUCCESS,
        VALIDATION_FAILED,
        CANCELLED
    }

    private static class Line {
        private final String text;
        private final boolean tooLong;

        private Line(String text, boolean tooLong) {
            this.text = text;
            this.tooLong = tooLong;
        }
    }

    private static class ImportResult {
        private final Status status;
        private final long linesRead;
        private final long validRows;
        private final long invalidRows;
        private final long skippedBlankLines;
        private final List<String> errors;
        private final boolean published;
        private final boolean atomicMove;
        private final String message;

        private ImportResult(
                Status status,
                long linesRead,
                long validRows,
                long invalidRows,
                long skippedBlankLines,
                List<String> errors,
                boolean published,
                boolean atomicMove,
                String message) {

            this.status = status;
            this.linesRead = linesRead;
            this.validRows = validRows;
            this.invalidRows = invalidRows;
            this.skippedBlankLines = skippedBlankLines;
            this.errors = Collections.unmodifiableList(
                    new ArrayList<String>(errors));
            this.published = published;
            this.atomicMove = atomicMove;
            this.message = message;
        }

        private void print() {
            System.out.println("\nСтатус: " + status);
            System.out.println("Результат: " + message);
            System.out.println("Прочитано строк: " + linesRead);
            System.out.println("Корректных записей: " + validRows);
            System.out.println("Ошибочных записей: " + invalidRows);
            System.out.println("Пустых строк пропущено: " + skippedBlankLines);
            System.out.println("Сохранён каталог: " + published);

            if (published) {
                System.out.println("Атомарное перемещение: " + atomicMove);
            }

            System.out.println("\nСписок ошибок (максимум "
                    + MAX_REPORTED_ERRORS + "):");

            if (errors.isEmpty()) {
                System.out.println("Ошибок нет.");
            } else {
                for (String error : errors) {
                    System.out.println(error);
                }

                if (invalidRows > errors.size()) {
                    System.out.println("Остальные ошибки не сохранены в списке.");
                }
            }
        }
    }

    private static Line readBoundedLine(PushbackReader reader)
            throws IOException {

        StringBuilder builder = new StringBuilder();
        boolean tooLong = false;
        boolean readSomething = false;

        while (true) {
            int ch = reader.read();

            if (ch == -1) {
                if (!readSomething) {
                    return null;
                }

                break;
            }

            readSomething = true;

            if (ch == '\n') {
                break;
            }

            if (ch == '\r') {
                int next = reader.read();

                if (next != '\n' && next != -1) {
                    reader.unread(next);
                }

                break;
            }

            if (builder.length() < MAX_LINE_LENGTH) {
                builder.append((char) ch);
            } else {
                tooLong = true;
            }
        }

        return new Line(builder.toString(), tooLong);
    }

    private static String shorten(String text) {
        String value = text.strip();

        if (value.length() > 80) {
            return value.substring(0, 80) + "...";
        }

        return value;
    }

    private static void addError(
            List<String> errors,
            long lineNumber,
            String line,
            String reason) {

        if (errors.size() < MAX_REPORTED_ERRORS) {
            errors.add("Строка " + lineNumber + ": "
                    + reason + " [" + shorten(line) + "]");
        }
    }

    public static ImportResult importCatalog(
            Path input, Path target) throws IOException {

        Path absoluteInput = input.toAbsolutePath().normalize();
        Path absoluteTarget = target.toAbsolutePath().normalize();

        if (absoluteInput.equals(absoluteTarget)) {
            throw new IOException(
                    "Исходный и целевой файлы должны различаться");
        }

        if (Files.isDirectory(absoluteInput)) {
            throw new IOException(
                    "Вместо входного файла указана директория: " + absoluteInput);
        }

        if (Files.isDirectory(absoluteTarget)) {
            throw new IOException(
                    "Вместо целевого файла указана директория: " + absoluteTarget);
        }

        Path directory = absoluteTarget.getParent();
        String prefix = absoluteTarget.getFileName().toString();

        while (prefix.length() < 3) {
            prefix += "_";
        }

        Path temporaryFile = Files.createTempFile(
                directory, prefix, ".tmp");

        boolean published = false;
        Throwable failure = null;

        try {
            long linesRead = 0;
            long validRows = 0;
            long invalidRows = 0;
            long skippedBlankLines = 0;
            boolean cancelled = false;

            List<String> errors = new ArrayList<String>();

            try (PushbackReader reader = new PushbackReader(
                    Files.newBufferedReader(
                            absoluteInput, StandardCharsets.UTF_8), 1);
                 BufferedWriter writer = Files.newBufferedWriter(
                         temporaryFile, StandardCharsets.UTF_8)) {

                Line current;

                while ((current = readBoundedLine(reader)) != null) {
                    linesRead++;

                    if (current.tooLong) {
                        invalidRows++;

                        addError(errors, linesRead, current.text,
                                "Строка превышает " + MAX_LINE_LENGTH
                                        + " символов");

                        if (invalidRows >= CANCEL_AFTER_ERRORS) {
                            cancelled = true;
                            break;
                        }

                        continue;
                    }

                    String line = current.text.strip();

                    if (line.isEmpty()) {
                        skippedBlankLines++;
                        continue;
                    }

                    int separator = line.indexOf(';');

                    String error = null;
                    String code = "";
                    String title = "";

                    if (separator <= 0
                            || separator != line.lastIndexOf(';')) {
                        error = "Ожидается формат CODE;TITLE";
                    } else {
                        code = line.substring(0, separator).strip();
                        title = line.substring(separator + 1).strip();

                        if (!code.matches("[A-Z]+-[0-9]+")) {
                            error = "Некорректный код курса";
                        } else if (title.isEmpty()) {
                            error = "Название курса пустое";
                        }
                    }

                    if (error != null) {
                        invalidRows++;
                        addError(errors, linesRead, current.text, error);

                        if (invalidRows >= CANCEL_AFTER_ERRORS) {
                            cancelled = true;
                            break;
                        }

                        continue;
                    }
                    writer.write(code);
                    writer.write(';');
                    writer.write(title);
                    writer.newLine();

                    validRows++;
                }

                writer.flush();
            }

            if (cancelled) {
                return new ImportResult(
                        Status.CANCELLED,
                        linesRead,
                        validRows,
                        invalidRows,
                        skippedBlankLines,
                        errors,
                        false,
                        false,
                        "Импорт отменён: достигнут порог ошибок "
                                + CANCEL_AFTER_ERRORS);
            }

            if (invalidRows > 0) {
                return new ImportResult(
                        Status.VALIDATION_FAILED,
                        linesRead,
                        validRows,
                        invalidRows,
                        skippedBlankLines,
                        errors,
                        false,
                        false,
                        "Каталог не опубликован из-за ошибок");
            }

            boolean atomicMove;

            try {
                Files.move(
                        temporaryFile,
                        absoluteTarget,
                        StandardCopyOption.ATOMIC_MOVE,
                        StandardCopyOption.REPLACE_EXISTING);

                atomicMove = true;

            } catch (AtomicMoveNotSupportedException e) {
                Files.move(
                        temporaryFile,
                        absoluteTarget,
                        StandardCopyOption.REPLACE_EXISTING);

                atomicMove = false;
            }

            published = true;

            return new ImportResult(
                    Status.SUCCESS,
                    linesRead,
                    validRows,
                    invalidRows,
                    skippedBlankLines,
                    errors,
                    true,
                    atomicMove,
                    "Импорт завершён успешно");

        } catch (IOException | RuntimeException | Error e) {
            failure = e;
            throw e;

        } finally {
            if (!published) {
                try {
                    Files.deleteIfExists(temporaryFile);
                } catch (IOException cleanupError) {
                    if (failure != null) {
                        failure.addSuppressed(cleanupError);
                    } else {
                        throw cleanupError;
                    }
                }
            }
        }
    }

    public static void main(String[] args) throws IOException {
        Path input = args.length > 0
                ? Path.of(args[0])
                : Path.of("courses-import.txt");

        Path target = args.length > 1
                ? Path.of(args[1])
                : Path.of("courses.txt");

        ImportResult result = importCatalog(input, target);
        result.print();
    }
}