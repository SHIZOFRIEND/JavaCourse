package lb7;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Task6 {

    private static final int MAGIC = 0x43525345;
    private static final int VERSION = 1;

    private static class Course {
        private final String code;
        private final String title;

        public Course(String code, String title) {
            this.code = Objects.requireNonNull(code);
            this.title = Objects.requireNonNull(title);
        }
    }

    private static void saveCatalogAtomically(
            Path target, List<Course> courses) throws IOException {

        Objects.requireNonNull(target, "Путь не должен быть null");
        Objects.requireNonNull(courses, "Список курсов не должен быть null");

        Path absoluteTarget = target.toAbsolutePath().normalize();
        Path directory = absoluteTarget.getParent();

        if (Files.isDirectory(absoluteTarget)) {
            throw new IOException(
                    "Вместо файла указана директория: " + absoluteTarget);
        }

        for (Course course : courses) {
            Objects.requireNonNull(course, "Курс не должен быть null");
        }

        String prefix = absoluteTarget.getFileName().toString();

        while (prefix.length() < 3) {
            prefix += "_";
        }

        Path tempFile = Files.createTempFile(
                directory, prefix, ".tmp");

        boolean moved = false;
        Throwable failure = null;

        try {

            try (DataOutputStream out = new DataOutputStream(
                    new BufferedOutputStream(
                            Files.newOutputStream(tempFile)))) {

                out.writeInt(MAGIC);
                out.writeInt(VERSION);
                out.writeInt(courses.size());

                for (Course course : courses) {
                    out.writeUTF(course.code);
                    out.writeUTF(course.title);
                }


                out.flush();
            }

            try {
                Files.move(
                        tempFile,
                        absoluteTarget,
                        StandardCopyOption.ATOMIC_MOVE,
                        StandardCopyOption.REPLACE_EXISTING
                );

            } catch (AtomicMoveNotSupportedException e) {

                System.out.println(
                        "Atomic move не поддерживается. Используется обычный move.");

                Files.move(
                        tempFile,
                        absoluteTarget,
                        StandardCopyOption.REPLACE_EXISTING
                );
            }

            moved = true;

        } catch (IOException | RuntimeException | Error e) {
            failure = e;
            throw e;

        } finally {

            if (!moved) {
                try {
                    Files.deleteIfExists(tempFile);
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
        Path target = Path.of("courses.bin");

        List<Course> courses = Arrays.asList(
                new Course("JAVA-101", "Основы Java"),
                new Course("CSHARP-201", "Разработка на C#"),
                new Course("PYTHON-301", "Программирование на Python")
        );

        saveCatalogAtomically(target, courses);

        System.out.println("Каталог успешно сохранён: " + target);
        System.out.println("Количество записей: " + courses.size());
    }
}