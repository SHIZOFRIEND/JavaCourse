
package lb8;

import java.util.*;
import java.util.stream.Collectors;

class CourseInfo {
    private String name;
    private int level;
    private int duration;

    public CourseInfo(String name, int level, int duration) {
        this.name = name;
        this.level = level;
        this.duration = duration;
    }

    public String getName() {
        return name;
    }

    public int getLevel() {
        return level;
    }

    public int getDuration() {
        return duration;
    }
}

class Summary {
    private int count;
    private double averageDuration;

    public Summary(int count, double averageDuration) {
        this.count = count;
        this.averageDuration = averageDuration;
    }

    @Override
    public String toString() {
        return "количество=" + count +
                ", средняя длительность=" + averageDuration;
    }
}

public class Task4 {

    public static void main(String[] args) {

        List<CourseInfo> courses = Arrays.asList(
                new CourseInfo("Java", 1, 20),
                new CourseInfo("Python", 1, 30),
                new CourseInfo("C++", 2, 40),
                new CourseInfo("C#", 2, 50),
                new CourseInfo("SQL", 1, 10),
                new CourseInfo("Algorithms", 3, 60)
        );

        Map<Integer, Summary> result = courses.stream()
                .collect(Collectors.groupingBy(
                        CourseInfo::getLevel,
                        TreeMap::new,
                        Collectors.collectingAndThen(
                                Collectors.summarizingInt(
                                        CourseInfo::getDuration
                                ),
                                stats -> new Summary(
                                        (int) stats.getCount(),
                                        stats.getAverage()
                                )
                        )
                ));

        result.forEach((level, summary) ->
                System.out.println("Level " + level + ": " + summary)
        );
    }
}
