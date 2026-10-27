package lb7;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.zip.CRC32;

public class Task9 {

    private static final int MAGIC = 0x43525345;
    private static final int CURRENT_VERSION = 2;
    private static final int MAX_RECORDS = 100_000;
    private static final int MAX_PAYLOAD_SIZE = 64 * 1024 * 1024;
    private static final int HEADER_SIZE = 12;
    private static final int CHECKSUM_SIZE = 8;

    private static class CourseV1 {
        private final String code;
        private final String title;

        private CourseV1(String code, String title) {
            this.code = code;
            this.title = title;
        }
    }

    private static class Course {
        private final String code;
        private final String title;
        private final String description;

        private Course(String code, String title, String description) {
            this.code = code;
            this.title = title;
            this.description = description;
        }

        @Override
        public String toString() {
            return code + " — " + title + " — " + description;
        }
    }

    private static class Catalog {
        private final int sourceVersion;
        private final List<Course> courses;

        private Catalog(int sourceVersion, List<Course> courses) {
            this.sourceVersion = sourceVersion;
            this.courses = Collections.unmodifiableList(
                    new ArrayList<Course>(courses));
        }

        public int getSourceVersion() {
            return sourceVersion;
        }

        public int getSchemaVersion() {
            return CURRENT_VERSION;
        }

        public List<Course> getCourses() {
            return courses;
        }
    }

    private static byte[] encodeV1(List<Course> courses)
            throws IOException {

        validateCourseCount(courses.size());

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();

        try (DataOutputStream out = new DataOutputStream(bytes)) {
            out.writeInt(courses.size());

            for (Course course : courses) {
                out.writeUTF(course.code);
                out.writeUTF(course.title);
            }
        }

        return bytes.toByteArray();
    }

    private static byte[] encodeV2(List<Course> courses)
            throws IOException {

        validateCourseCount(courses.size());

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();

        try (DataOutputStream out = new DataOutputStream(bytes)) {
            out.writeInt(courses.size());

            for (Course course : courses) {
                out.writeUTF(course.code);
                out.writeUTF(course.title);
                out.writeUTF(course.description);
            }
        }

        return bytes.toByteArray();
    }

    private static void writeEnvelope(
            Path path, int version, byte[] payload) throws IOException {

        if (payload.length > MAX_PAYLOAD_SIZE) {
            throw new IOException("Слишком большой payload");
        }

        long checksum = calculateChecksum(
                MAGIC, version, payload.length, payload);

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();

        try (DataOutputStream out = new DataOutputStream(bytes)) {
            out.writeInt(MAGIC);
            out.writeInt(version);
            out.writeInt(payload.length);
            out.write(payload);
            out.writeLong(checksum);
        }

        Files.write(path, bytes.toByteArray());
    }

    public static void writeCatalog(
            Path path, List<Course> courses) throws IOException {

        byte[] payload = encodeV2(courses);
        writeEnvelope(path, CURRENT_VERSION, payload);
    }

    private static long calculateChecksum(
            int magic, int version, int length, byte[] payload) {

        CRC32 crc = new CRC32();

        updateInt(crc, magic);
        updateInt(crc, version);
        updateInt(crc, length);
        crc.update(payload);

        return crc.getValue();
    }

    private static void updateInt(CRC32 crc, int value) {
        crc.update((value >>> 24) & 0xFF);
        crc.update((value >>> 16) & 0xFF);
        crc.update((value >>> 8) & 0xFF);
        crc.update(value & 0xFF);
    }

    private static Catalog readCatalog(Path path) throws IOException {
        if (Files.isDirectory(path)) {
            throw new IOException(
                    "Вместо файла указана директория: " + path);
        }

        long fileSize = Files.size(path);

        if (fileSize > (long) MAX_PAYLOAD_SIZE
                + HEADER_SIZE + CHECKSUM_SIZE) {
            throw new IOException("Файл превышает допустимый размер");
        }

        byte[] fileBytes = Files.readAllBytes(path);

        int version;
        int payloadLength;
        byte[] payload;
        long storedChecksum;

        try (DataInputStream in = new DataInputStream(
                new ByteArrayInputStream(fileBytes))) {

            int magic = in.readInt();

            if (magic != MAGIC) {
                throw new IOException("Неверный magic header");
            }

            version = in.readInt();

            if (version > CURRENT_VERSION) {
                throw new IOException(
                        "Неподдерживаемая будущая версия схемы: " + version);
            }

            if (version < 1) {
                throw new IOException(
                        "Неизвестная версия схемы: " + version);
            }

            payloadLength = in.readInt();

            if (payloadLength < 0
                    || payloadLength > MAX_PAYLOAD_SIZE) {
                throw new IOException(
                        "Недопустимый размер payload: " + payloadLength);
            }

            payload = new byte[payloadLength];
            in.readFully(payload);
            storedChecksum = in.readLong();

            if (in.read() != -1) {
                throw new IOException(
                        "Лишние данные после контрольной суммы");
            }

        } catch (EOFException e) {
            throw new IOException("Файл обрезан: заголовок или данные неполные", e);
        }

        long actualChecksum = calculateChecksum(
                MAGIC, version, payloadLength, payload);

        if (actualChecksum != storedChecksum) {
            throw new IOException(
                    "Контрольная сумма не совпадает: файл повреждён");
        }

        if (version == 1) {
            List<CourseV1> oldCourses = decodeV1(payload);
            List<Course> migratedCourses = migrateV1ToV2(oldCourses);

            return new Catalog(1, migratedCourses);
        }

        List<Course> courses = decodeV2(payload);
        return new Catalog(2, courses);
    }

    private static List<CourseV1> decodeV1(byte[] payload)
            throws IOException {

        List<CourseV1> courses = new ArrayList<CourseV1>();

        try (DataInputStream in = new DataInputStream(
                new ByteArrayInputStream(payload))) {

            int count = readCount(in);

            for (int i = 0; i < count; i++) {
                String code = in.readUTF();
                String title = in.readUTF();

                courses.add(new CourseV1(code, title));
            }

            if (in.read() != -1) {
                throw new IOException(
                        "Лишние данные в payload версии 1");
            }

        } catch (EOFException e) {
            throw new IOException(
                    "Повреждён payload версии 1: запись обрезана", e);
        }

        return courses;
    }

    private static List<Course> decodeV2(byte[] payload)
            throws IOException {

        List<Course> courses = new ArrayList<Course>();

        try (DataInputStream in = new DataInputStream(
                new ByteArrayInputStream(payload))) {

            int count = readCount(in);

            for (int i = 0; i < count; i++) {
                String code = in.readUTF();
                String title = in.readUTF();
                String description = in.readUTF();

                courses.add(new Course(code, title, description));
            }

            if (in.read() != -1) {
                throw new IOException(
                        "Лишние данные в payload версии 2");
            }

        } catch (EOFException e) {
            throw new IOException(
                    "Повреждён payload версии 2: запись обрезана", e);
        }

        return courses;
    }

    private static int readCount(DataInputStream in)
            throws IOException {

        int count = in.readInt();

        validateCourseCount(count);

        return count;
    }

    private static void validateCourseCount(int count)
            throws IOException {

        if (count < 0 || count > MAX_RECORDS) {
            throw new IOException(
                    "Недопустимое количество курсов: " + count);
        }
    }

    private static List<Course> migrateV1ToV2(
            List<CourseV1> oldCourses) {

        List<Course> migrated = new ArrayList<Course>();

        for (CourseV1 oldCourse : oldCourses) {
            migrated.add(new Course(
                    oldCourse.code,
                    oldCourse.title,
                    ""
            ));
        }

        return migrated;
    }

    private static void printCatalog(Catalog catalog) {
        System.out.println("Версия исходного файла: "
                + catalog.getSourceVersion());
        System.out.println("Версия модели после чтения: "
                + catalog.getSchemaVersion());

        for (Course course : catalog.getCourses()) {
            System.out.println(course);
        }
    }

    public static void main(String[] args) throws IOException {
        Path v2Path = Path.of("courses-v2.bin");
        Path v1Path = Path.of("courses-v1.bin");
        Path corruptPath = Path.of("courses-corrupt.bin");
        Path futurePath = Path.of("courses-v3.bin");

        List<Course> courses = Arrays.asList(
                new Course("JAVA-101", "Основы Java", "Начальный уровень"),
                new Course("PYTHON-201", "Python", "Работа с данными"),
                new Course("CSHARP-301", "Разработка на C#", "ООП")
        );

        System.out.println("1. Запись и чтение версии 2:");

        writeCatalog(v2Path, courses);
        Catalog catalogV2 = readCatalog(v2Path);
        printCatalog(catalogV2);

        System.out.println("\n2. Миграция версии 1 в версию 2:");

        writeEnvelope(v1Path, 1, encodeV1(courses));
        Catalog migrated = readCatalog(v1Path);
        printCatalog(migrated);

        System.out.println("\n3. Проверка повреждения:");

        byte[] corrupted = Files.readAllBytes(v2Path);
        corrupted[HEADER_SIZE] ^= 0x01;
        Files.write(corruptPath, corrupted);

        try {
            readCatalog(corruptPath);
            throw new IllegalStateException(
                    "Повреждённый файл не был отклонён");
        } catch (IOException e) {
            System.out.println("Файл отклонён: " + e.getMessage());
        }

        System.out.println("\n4. Проверка будущей версии:");

        writeEnvelope(
                futurePath,
                CURRENT_VERSION + 1,
                encodeV2(courses));

        try {
            readCatalog(futurePath);
            throw new IllegalStateException(
                    "Будущая версия не была отклонена");
        } catch (IOException e) {
            System.out.println("Файл отклонён: " + e.getMessage());
        }
    }
}