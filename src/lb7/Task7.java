package lb7;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Task7 {

    private static class Course {
        private final String code;
        private final String title;

        public Course(String code, String title) {
            this.code = code;
            this.title = title;
        }

        @Override
        public String toString() {
            return code + " — " + title;
        }
    }

    private static class ImportError {
        private final int lineNumber;
        private final String line;
        private final String message;

        public ImportError(int lineNumber, String line, String message) {
            this.lineNumber = lineNumber;
            this.line = line;
            this.message = message;
        }

        @Override
        public String toString() {
            return "Строка " + lineNumber + ": " + message
                    + " [" + line + "]";
        }
    }
    private interface ImportResult {
    }

    private static class ImportSuccess implements ImportResult {
        private final List<Course> courses;

        public ImportSuccess(List<Course> courses) {
            this.courses = Collections.unmodifiableList(
                    new ArrayList<Course>(courses));
        }

        public List<Course> getCourses() {
            return courses;
        }
    }

    private static class ImportFailure implements ImportResult {
        private final List<ImportError> errors;

        public ImportFailure(List<ImportError> errors) {
            this.errors = Collections.unmodifiableList(
                    new ArrayList<ImportError>(errors));
        }

        public List<ImportError> getErrors() {
            return errors;
        }
    }

    public static ImportResult importCatalog(Path path) throws IOException {
        if (Files.isDirectory(path)) {
            throw new IOException(
                    "Вместо файла импорта указана директория: " + path);
        }

        List<Course> temporaryCourses = new ArrayList<Course>();
        List<ImportError> errors = new ArrayList<ImportError>();
        Set<String> codes = new HashSet<String>();

        int lineNumber = 0;

        try (BufferedReader reader = Files.newBufferedReader(
                path, StandardCharsets.UTF_8)) {

            String line;

            while ((line = reader.readLine()) != null) {
                lineNumber++;

                if (line.isBlank()) {
                    continue;
                }

                String[] parts = line.split(";", -1);

                if (parts.length != 2) {
                    errors.add(new ImportError(
                            lineNumber,
                            line,
                            "Ожидаются код и название через ';'"));
                    continue;
                }

                String code = parts[0].strip();
                String title = parts[1].strip();
                boolean valid = true;

                if (!code.matches("[A-Z]+-[0-9]+")) {
                    errors.add(new ImportError(
                            lineNumber, line, "Некорректный код курса"));
                    valid = false;
                } else if (!codes.add(code)) {
                    errors.add(new ImportError(
                            lineNumber, line, "Повторяющийся код курса: " + code));
                    valid = false;
                }

                if (title.isEmpty()) {
                    errors.add(new ImportError(
                            lineNumber, line, "Название курса пустое"));
                    valid = false;
                }

                if (valid) {
                    temporaryCourses.add(new Course(code, title));
                }
            }
        }

        if (!errors.isEmpty()) {
            return new ImportFailure(errors);
        }

        return new ImportSuccess(temporaryCourses);
    }

    public static void main(String[] args) throws IOException {
        Path path = Path.of("courses-import.txt");

        Files.writeString(
                path,
                "JAVA-101;Основы Java\n"
                        + "PYTHON-201;Python\n"
                        + "Некорректная строка\n"
                        + "CSHARP-301;\n"
                        + "JAVA-101;Повтор Java\n"
                        + "WEB-401;Веб-разработка\n",
                StandardCharsets.UTF_8
        );

        ImportResult result = importCatalog(path);

        if (result instanceof ImportSuccess) {
            ImportSuccess success = (ImportSuccess) result;

            System.out.println("Импорт завершён успешно.");
            System.out.println("Опубликованный каталог:");

            for (Course course : success.getCourses()) {
                System.out.println(course);
            }
        } else {
            ImportFailure failure = (ImportFailure) result;

            System.out.println("Импорт отклонён. Каталог не опубликован.");

            for (ImportError error : failure.getErrors()) {
                System.out.println(error);
            }
        }
    }
}