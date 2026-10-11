package lb1;

public class StatusCourseFormatter implements CourseFormatter {

    @Override
    public String format(Course course) {
        if (course.isCompleted()) {
            return course.title() + " Завершён";
        } else {
            return course.title() + " В процессе";
        }
    }
}
