package lb1;
public class CompactCourseFormatter implements CourseFormatter{
@Override
public String format(Course course)
{
    return "Название: " + course.title() + " Времени пройдено " + course.completedHours()+" Продолжительность: " + course.durationHours();
    
} 
}