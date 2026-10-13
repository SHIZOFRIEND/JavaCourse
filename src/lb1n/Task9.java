package lb1n;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Task9 {

    static class Student {
        private final String id;
        private final int age;
        private final Set<String> completedCourseIds;

        public Student(String id, int age, Set<String> completedCourseIds) {
            this.id = id;
            this.age = age;
            this.completedCourseIds = new HashSet<>(completedCourseIds);
        }

        public String getId() {
            return id;
        }

        public int getAge() {
            return age;
        }

        public Set<String> getCompletedCourseIds() {
            return completedCourseIds;
        }
    }

    static class Course {
        private final String id;
        private final String title;
        private final int minimumAge;
        private final Set<String> prerequisiteCourseIds;
        private final int capacity;
        private final int enrolledCount;

        public Course(
                String id,
                String title,
                int minimumAge,
                Set<String> prerequisiteCourseIds,
                int capacity,
                int enrolledCount) {

            this.id = id;
            this.title = title;
            this.minimumAge = minimumAge;
            this.prerequisiteCourseIds =
                    new HashSet<>(prerequisiteCourseIds);
            this.capacity = capacity;
            this.enrolledCount = enrolledCount;
        }

        public String getId() {
            return id;
        }

        public String getTitle() {
            return title;
        }

        public int getMinimumAge() {
            return minimumAge;
        }

        public Set<String> getPrerequisiteCourseIds() {
            return prerequisiteCourseIds;
        }

        public int getCapacity() {
            return capacity;
        }

        public int getEnrolledCount() {
            return enrolledCount;
        }
    }

    interface EnrollmentPolicy {
        boolean canEnroll(Student student, Course course);
        String getName();
        String getRejectionReason();
    }

    static class AgeEnrollmentPolicy implements EnrollmentPolicy {

        @Override
        public boolean canEnroll(Student student, Course course) {
            return student.getAge() >= course.getMinimumAge();
        }

        @Override
        public String getName() {
            return "Проверка возраста";
        }

        @Override
        public String getRejectionReason() {
            return "Студент не достиг минимального возраста";
        }
    }

    static class PrerequisitesEnrollmentPolicy
            implements EnrollmentPolicy {

        @Override
        public boolean canEnroll(Student student, Course course) {
            return student.getCompletedCourseIds()
                    .containsAll(course.getPrerequisiteCourseIds());
        }

        @Override
        public String getName() {
            return "Проверка предварительных курсов";
        }

        @Override
        public String getRejectionReason() {
            return "Не завершены необходимые предварительные курсы";
        }
    }

    static class CapacityEnrollmentPolicy implements EnrollmentPolicy {

        @Override
        public boolean canEnroll(Student student, Course course) {
            return course.getEnrolledCount() < course.getCapacity();
        }

        @Override
        public String getName() {
            return "Проверка вместимости";
        }

        @Override
        public String getRejectionReason() {
            return "На курсе нет свободных мест";
        }
    }

    static class EnrollmentService {

        private final EnrollmentPolicy policy;

        public EnrollmentService(EnrollmentPolicy policy) {
            if (policy == null) {
                throw new IllegalArgumentException("Политика не должна быть null");
            }
            this.policy = policy;
        }

        public boolean enroll(Student student, Course course) {
            System.out.println("Курс: " + course.getTitle());
            System.out.println("Политика: " + policy.getName());
            if (policy.canEnroll(student, course)) {
                System.out.println("Проверка пройдена.");
                return true;
            }

            System.out.println(
                    "Отказ: " + policy.getRejectionReason());
            return false;
        }
    }

    public static void main(String[] args) {

        Student student = new Student(
                "S-101",
                20,
                new HashSet<>(Arrays.asList("JAVA-101"))
        );

        Course course = new Course(
                "JAVA-201",
                "Продвинутый Java",
                18,
                new HashSet<>(Arrays.asList("JAVA-101")),
                30,
                28
        );

        List<EnrollmentPolicy> policies = Arrays.asList(
                new AgeEnrollmentPolicy(),
                new PrerequisitesEnrollmentPolicy(),
                new CapacityEnrollmentPolicy()
        );

        for (EnrollmentPolicy policy : policies) {
            EnrollmentService service = new EnrollmentService(policy);
            boolean result = service.enroll(student, course);
            System.out.println("Результат: " + (result ? "разрешено" : "отказано"));
        }
    }
}