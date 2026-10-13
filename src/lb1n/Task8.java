package lb1n;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class Task8 {

    static class Course {
        private final String id;
        private final String title;
        public Course(String id, String title) {
            this.id = id;
            this.title = title;
        }
        public String getId() {
            return id;
        }
        public String getTitle() {
            return title;
        }
        @Override
        public String toString() {
            return id + " — " + title;
        }
    }

    interface CourseReader {
        Optional<Course> findById(String id);

        List<Course> findAll();
    }
    interface CourseWriter {
        void save(Course course);
        boolean deleteById(String id);
    }

    interface CourseRepository extends CourseReader, CourseWriter {
    }
    static class InMemoryCourseRepository implements CourseRepository {
        private final Map<String, Course> courses =
                new LinkedHashMap<>();
        @Override
        public Optional<Course> findById(String id) {
            return Optional.ofNullable(courses.get(id));
        }
        @Override
        public List<Course> findAll() {
            return new ArrayList<>(courses.values());
        }
        @Override
        public void save(Course course) {
            if (course == null) {
                throw new IllegalArgumentException("Курс не должен быть null");
            }
            courses.put(course.getId(), course);
        }

        @Override
        public boolean deleteById(String id) {
            return courses.remove(id) != null;
        }
    }


    static class ReadOnlyCourseRepository implements CourseReader {

        private final CourseReader source;

        public ReadOnlyCourseRepository(CourseReader source) {
            if (source == null) {
                throw new IllegalArgumentException("Источник курсов не должен быть null");
            }
            this.source = source;
        }

        @Override
        public Optional<Course> findById(String id) {
            return source.findById(id);
        }

        @Override
        public List<Course> findAll() {
            return Collections.unmodifiableList(
                    new ArrayList<>(source.findAll()));
        }
    }

    static class CourseCatalogService {
        private final CourseReader repository;
        public CourseCatalogService(CourseReader repository) {
            this.repository = repository;
        }
        public void printCourses() {
            for (Course course : repository.findAll()) {
                System.out.println(course);
            }
        }
    }

    static class CourseAdminService {
        private final CourseWriter repository;
        public CourseAdminService(CourseWriter repository) {
            this.repository = repository;
        }
        public void addCourse(Course course) {
            repository.save(course);
            System.out.println("Добавлен курс: " + course);
        }
    }

    public static void main(String[] args) {
        InMemoryCourseRepository storage = new InMemoryCourseRepository();
        storage.save(new Course("JAVA-101", "Основы Java"));
        storage.save(new Course("JAVA-201", "Продвинутый Java"));
        ReadOnlyCourseRepository readOnly = new ReadOnlyCourseRepository(storage);
        CourseCatalogService catalog = new CourseCatalogService(readOnly);
        System.out.println("Каталог курсов");
        catalog.printCourses();
        System.out.println();
        CourseAdminService admin = new CourseAdminService(storage);
        admin.addCourse(new Course("JAVA-301", "Java для профессионалов"));
        System.out.println();
        System.out.println("Обновлённый каталог");
        catalog.printCourses();
    }
}