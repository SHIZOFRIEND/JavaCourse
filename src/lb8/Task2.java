
package lb8;

import java.util.*;
import java.util.stream.Collectors;

class Course {
    private String name;
    private List<String> modules;

    public Course(String name, List<String> modules) {
        this.name = name;
        this.modules = modules;
    }

    public String getName() {
        return name;
    }

    public List<String> getModules() {
        return modules;
    }
}

public class Task2 {

    public static void main(String[] args) {

        List<Course> courses = Arrays.asList(
                new Course("Java", Arrays.asList(" OOP ", "Collections", "Streams")),
                new Course("Python", Arrays.asList(" OOP", "Data Science", "Collections")),
                new Course("C++", Arrays.asList("Algorithms", "OOP ", "Pointers"))
        );


        Set<String> uniqueTopics = courses.stream()
                .flatMap(course -> course.getModules().stream())
                .map(String::trim)
                .map(String::toLowerCase)
                .collect(Collectors.toCollection(TreeSet::new));

        Map<String, Set<String>> topicIndex = courses.stream()
                .flatMap(course -> course.getModules().stream()
                        .map(topic -> new AbstractMap.SimpleEntry<>(
                                topic.trim().toLowerCase(),
                                course.getName()
                        )))
                .collect(Collectors.groupingBy(
                        Map.Entry::getKey,
                        TreeMap::new,
                        Collectors.mapping(
                                Map.Entry::getValue,
                                Collectors.toCollection(TreeSet::new)
                        )
                ));

        System.out.println("Уникальные темы:");
        uniqueTopics.forEach(System.out::println);

        System.out.println("\nОбратный индекс:");
        topicIndex.forEach((topic, courseNames) ->
                System.out.println(topic + " -> " + courseNames)
        );
    }
}
