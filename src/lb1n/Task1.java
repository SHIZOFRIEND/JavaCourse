package lb1n;

public class Task1 {

    static class Course {
        private String id;
        private String title;

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
    }

    interface CourseFormatter {
        String format(Course course);
    }

    static class CompactCourseFormatter implements CourseFormatter {
        @Override
        public String format(Course course) {
            return course.getId() + " — " + course.getTitle();
        }
    }

    static class DetailedCourseFormatter implements CourseFormatter {
        @Override
        public String format(Course course) {
            return "ID курса: " + course.getId()
                    + "\nНазвание курса: " + course.getTitle();
        }
    }

    public static void main(String[] args) {
        Course course = new Course("JAVA-101", "Основы Java");

        CourseFormatter formatter;

        formatter = new CompactCourseFormatter();
        System.out.println("Краткий формат:");
        System.out.println(formatter.format(course));

        System.out.println();

        formatter = new DetailedCourseFormatter();
        System.out.println("Подробный формат:");
        System.out.println(formatter.format(course));
    }
}