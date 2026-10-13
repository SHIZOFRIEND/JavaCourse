package lb1n;

import java.time.Clock;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;

public class Task5 {

    interface Identifiable {
        String getId();
    }

    static abstract class BaseEntity implements Identifiable {
        private final String id;

        public BaseEntity(String id) {
            this.id = id;
        }

        @Override
        public String getId() {
            return id;
        }
    }

    static class Student extends BaseEntity {
        private final String name;
        private final int age;

        public Student(String id, String name, int age) {
            super(id);
            this.name = name;
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public int getAge() {
            return age;
        }
    }

    static class Course extends BaseEntity {
        private final String title;
        private final int minimumAge;
        private final boolean open;

        public Course(
                String id,
                String title,
                int minimumAge,
                boolean open) {
            super(id);
            this.title = title;
            this.minimumAge = minimumAge;
            this.open = open;
        }

        public String getTitle() {
            return title;
        }

        public int getMinimumAge() {
            return minimumAge;
        }

        public boolean isOpen() {
            return open;
        }
    }

    interface CoursePolicy {
        boolean isAllowed(Student student, Course course);

        String getDescription();
    }

    static class CourseOpenPolicy implements CoursePolicy {
        @Override
        public boolean isAllowed(Student student, Course course) {
            return course.isOpen();
        }

        @Override
        public String getDescription() {
            return "Курс открыт для записи";
        }
    }

    static class MinimumAgePolicy implements CoursePolicy {
        @Override
        public boolean isAllowed(Student student, Course course) {
            return student.getAge() >= course.getMinimumAge();
        }

        @Override
        public String getDescription() {
            return "Возраст студента соответствует требованиям";
        }
    }

    interface Formatter<T> {
        String format(T value);
    }

    static class CompactCourseFormatter implements Formatter<Course> {
        @Override
        public String format(Course course) {
            return course.getId() + " — " + course.getTitle();
        }
    }

    static class DetailedCourseFormatter implements Formatter<Course> {
        @Override
        public String format(Course course) {
            return "ID: " + course.getId()
                    + "\nНазвание: " + course.getTitle()
                    + "\nМинимальный возраст: "
                    + course.getMinimumAge()
                    + "\nЗапись открыта: " + course.isOpen();
        }
    }

    static class TimeService {
        private final Clock clock;

        public TimeService(Clock clock) {
            this.clock = clock;
        }

        public Instant getCurrentTime() {
            return clock.instant();
        }
    }

    public static void main(String[] args) {
        Student student = new Student(
                "S-101",
                "Иван",
                20
        );

        Course course = new Course(
                "JAVA-101",
                "Основы Java",
                16,
                true
        );

        System.out.println("Идентификаторы");
        Identifiable studentEntity = student;
        Identifiable courseEntity = course;

        System.out.println("Студент: " + studentEntity.getId());
        System.out.println("Курс: " + courseEntity.getId());

        System.out.println("Проверка политик");

        List<CoursePolicy> policies = Arrays.asList(
                new CourseOpenPolicy(),
                new MinimumAgePolicy()
        );

        boolean allowed = true;

        for (CoursePolicy policy : policies) {
            boolean result = policy.isAllowed(student, course);

            System.out.println(
                    policy.getDescription() + ": "
                            + (result ? "пройдена" : "не пройдена"));

            if (!result) {
                allowed = false;
            }
        }

        System.out.println("Итог записи: " + (allowed ? "разрешена" : "запрещена"));

        System.out.println("Форматирование курса");

        List<Formatter<Course>> formatters = Arrays.asList(
                new CompactCourseFormatter(),
                new DetailedCourseFormatter()
        );

        for (Formatter<Course> formatter : formatters) {
            System.out.println(formatter.format(course));

        }

        System.out.println("Текущее время");
        TimeService timeService = new TimeService(Clock.systemUTC());
        System.out.println("Время UTC: " + timeService.getCurrentTime());
    }
}