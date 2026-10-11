package lb1;

public class DetailedCourseFormatter implements CourseFormatter {

    @Override
    public String format(Course course) {
        return "Наиманоевание курса" + course.title() + "Продолжительность курса: " + course.durationHours() + " Продйено часов " + course.completedHours() + "Часовмс : " + course.remainingHours() + "Статус" + course.isCompleted();
    }
}