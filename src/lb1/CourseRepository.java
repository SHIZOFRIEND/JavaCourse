
package lb1;

public class CourseRepository implements CourseReader, CourseWriter {

    @Override
    public Course find(long id) {
        System.out.println("Курс найден");
        return null;
    }

    @Override
    public void save(Course course) {
        System.out.println("Курс сохравнакен");
    }
}

