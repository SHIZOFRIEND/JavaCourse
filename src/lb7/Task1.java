package lb7;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class Task1 {

    private static void writeCatalog(Path path, List<String> courses)
            throws IOException {

        if (Files.isDirectory(path)) {
            throw new IOException(
                    "Невозможно сохранить каталог: путь указывает на директорию: "
                            + path);
        }

        String content = String.join(System.lineSeparator(), courses)
                + System.lineSeparator();

        Files.writeString(
                path,
                content,
                StandardCharsets.UTF_8,
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING,
                StandardOpenOption.WRITE
        );
    }

    private static String readCatalog(Path path) throws IOException {
        if (Files.isDirectory(path)) {
            throw new IOException(
                    "Невозможно прочитать каталог: вместо файла указана директория: "
                            + path);
        }

        return Files.readString(path, StandardCharsets.UTF_8);
    }

    private static Optional<String> readCatalogIfExists(Path path)
            throws IOException {

        try {
            return Optional.of(readCatalog(path));
        } catch (NoSuchFileException e) {
            return Optional.empty();
        }
    }

    public static void main(String[] args) throws IOException {
        Path catalogPath = args.length > 0
                ? Path.of(args[0])
                : Path.of("courses.txt");

        List<String> courses = Arrays.asList(
                "JAVA-101 — Основы Java",
                "CSHARP-201 — Разработка на C#",
                "PYTHON-301 — Программирование на Python",
                "WEB-401 — Веб-разработка"
        );

        writeCatalog(catalogPath, courses);
        System.out.println("Каталог сохранён в UTF-8: " + catalogPath);

        String content = readCatalog(catalogPath);

        System.out.println("\nСодержимое каталога:");
        System.out.println(content);

        long courseCount = content.lines()
                .filter(line -> !line.isBlank())
                .count();

        System.out.println("Количество курсов: " + courseCount);

        Path missingPath = Path.of("missing-courses.txt");
        Optional<String> missingCatalog = readCatalogIfExists(missingPath);

        if (missingCatalog.isPresent()) {
            System.out.println("\nСодержимое дополнительного каталога:");
            System.out.println(missingCatalog.get());
        } else {
            System.out.println("\nДополнительный каталог отсутствует: "
                    + missingPath);
        }

        System.out.println("\nЧтение завершено успешно.");
    }
}