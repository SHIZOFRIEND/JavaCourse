
package lb1;

public class ReadOnlyCourseRepository implements CourseReader {

    @Override
    public Course find(long id) {
        System.out.println("Course found");
        return null;
    }
}
