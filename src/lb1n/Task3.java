package lb1n;

public class Task3 {

    static class Course {
        private String id;
        private String title;
        private int duration;

        public Course(String id, String title, int duration) {
            this.id = id;
            this.title = title;
            this.duration = duration;
        }

        public String getId() {
            return id;
        }

        public String getTitle() {
            return title;
        }

        public int getDuration() {
            return duration;
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
            return "ID: " + course.getId()
                    + "\nНазвание: " + course.getTitle()
                    + "\nПродолжительность: "
                    + course.getDuration() + " часов";
        }
    }

    static class CourseIdFormatter implements CourseFormatter {
        @Override
        public String format(Course course) {
            return "Код курса: " + course.getId();
        }
    }

    public static void main(String[] args) {
        Course course = new Course(
                "JAVA-101",
                "Основы Java",
                40
        );

        CourseFormatter[] formatters = {
                new CompactCourseFormatter(),
                new DetailedCourseFormatter(),
                new CourseIdFormatter()
        };

        for (CourseFormatter formatter : formatters) {
            System.out.println(formatter.format(course));

        }
    }
}