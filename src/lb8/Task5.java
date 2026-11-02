
package lb8;

import java.util.*;

class Course5 {
    private String name;
    private int duration;

    public Course5(String name, int duration) {
        this.name = name;
        this.duration = duration;
    }

    public String getName() {
        return name;
    }

    public int getDuration() {
        return duration;
    }
}

public class Task5 {

    public static void main(String[] args) {

        List<Course5> courses = Arrays.asList(
                new Course5("Java", 40),
                new Course5("Python", 30),
                new Course5("C++", 50),
                new Course5("SQL", 20)
        );

        Optional<Course5> longestCourse = courses.stream()
                .max(Comparator.comparingInt(Course5::getDuration));

        String result = longestCourse
                .map(course ->
                        course.getName() + " (" +
                                course.getDuration() + " часов)"
                )
                .orElse("Курсы отсутствуют");

        System.out.println("Самый длинный курс: " + result);


        List<Course5> emptyCourses = Collections.emptyList();

        String emptyResult = emptyCourses.stream()
                .max(Comparator.comparingInt(Course5::getDuration))
                .map(Course5::getName)
                .orElse("Курсы отсутствуют");

        System.out.println("Результат для пустого списка: " + emptyResult);
    }
}
