package lb7;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Task5 {

    private static final int MAGIC = 0x43525345;
    private static final int VERSION = 1;
    private static final int MAX_RECORDS = 100_000;

    private static class Course {
        private final String code;
        private final String title;

        public Course(String code, String title) {
            this.code = Objects.requireNonNull(code);
            this.title = Objects.requireNonNull(title);
        }

        @Override
        public String toString() {
            return code + " — " + title;
        }
    }

    private static void writeCatalog(
            Path path, List<Course> courses) throws IOException {

        if (courses.size() > MAX_RECORDS) {
            throw new IOException("Слишком много записей");
        }

        try (DataOutputStream out = new DataOutputStream(
                new BufferedOutputStream(Files.newOutputStream(path)))) {

            out.writeInt(MAGIC);
            out.writeInt(VERSION);
            out.writeInt(courses.size());

            for (Course course : courses) {
                out.writeUTF(course.code);
                out.writeUTF(course.title);
            }
        }
    }

    private static List<Course> readCatalog(Path path) throws IOException {
        try (DataInputStream in = new DataInputStream(
                new BufferedInputStream(Files.newInputStream(path)))) {

            try {
                int magic = in.readInt();

                if (magic != MAGIC) {
                    throw new IOException("Неверный magic header");
                }

                int version = in.readInt();

                if (version != VERSION) {
                    throw new IOException(
                            "Неподдерживаемая версия формата: " + version);
                }

                int count = in.readInt();

                if (count < 0 || count > MAX_RECORDS) {
                    throw new IOException(
                            "Недопустимое количество записей: " + count);
                }

                List<Course> courses = new ArrayList<Course>();

                for (int i = 0; i < count; i++) {
                    String code = in.readUTF();
                    String title = in.readUTF();

                    courses.add(new Course(code, title));
                }

                if (in.read() != -1) {
                    throw new IOException(
                            "Файл содержит лишние данные после записей");
                }

                return courses;

            } catch (EOFException e) {
                throw new IOException(
                        "Бинарный файл обрезан: данные отсутствуют "
                                + "или запись не завершена",
                        e);
            }
        }
    }

    private static void createUnknownVersionFile(Path path)
            throws IOException {
        try (DataOutputStream out = new DataOutputStream(
                new BufferedOutputStream(Files.newOutputStream(path)))) {

            out.writeInt(MAGIC);
            out.writeInt(VERSION + 1);
            out.writeInt(0);
        }
    }

    private static void createTruncatedFile(Path path)
            throws IOException {
        try (DataOutputStream out = new DataOutputStream(
                new BufferedOutputStream(Files.newOutputStream(path)))) {

            out.writeInt(MAGIC);
            out.writeInt(VERSION);
            out.writeInt(1);
            out.writeUTF("JAVA-101");

        }
    }

    private static void verifyRejected(Path path) throws IOException {
        try {
            readCatalog(path);
            throw new IllegalStateException(
                    "Ошибка: некорректный файл был принят: " + path);
        } catch (IOException e) {
            System.out.println(
                    "Файл отклонён: " + path + " — " + e.getMessage());
        }
    }

    public static void main(String[] args) throws IOException {
        Path catalogPath = Path.of("courses.bin");

        List<Course> courses = Arrays.asList(
                new Course("JAVA-101", "Основы Java"),
                new Course("CSHARP-201", "Разработка на C#"),
                new Course("PYTHON-301", "Программирование на Python")
        );

        writeCatalog(catalogPath, courses);

        System.out.println("Прочитанный каталог:");

        for (Course course : readCatalog(catalogPath)) {
            System.out.println(course);
        }

        Path unknownVersionPath = Path.of("courses-unknown-version.bin");
        createUnknownVersionFile(unknownVersionPath);
        verifyRejected(unknownVersionPath);

        Path truncatedPath = Path.of("courses-truncated.bin");
        createTruncatedFile(truncatedPath);
        verifyRejected(truncatedPath);
    }
}