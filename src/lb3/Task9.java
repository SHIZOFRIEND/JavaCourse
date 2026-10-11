package lb3;

import java.util.ArrayList;
import java.util.List;


public class Task9 {

    public static void copyCourses(
            List<? extends Course> source,
            List<? super Course> destination) {

        for (Course course : source) {
            destination.add(course);
        }
    }

    public static void main(String[] args) {
        List<Course> courses = new ArrayList<>();
        courses.add(new Course("Java"));
        courses.add(new Course("C#"));
        courses.add(new Course("Python"));

        List<Object> destination = new ArrayList<>();

        copyCourses(courses, destination);

        System.out.println("Исходный список: " + courses);
        System.out.println("Скопированный список: " + destination);
    }

}
class Course {
    private String name;

    public Course(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}
