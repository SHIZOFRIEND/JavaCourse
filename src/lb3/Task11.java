package lb3;

import java.util.*;

class Course11 {
    int id;
    String name;

    Course11(int id, String name) {
        this.id = id;
        this.name = name;
    }
}

class Task {
    String name;

    Task(String name) {
        this.name = name;
    }
}

class Player {
    String name;

    Player(String name) {
        this.name = name;
    }
}

public class Task11 {
    public static void main(String[] args) {


        Map<Integer, Course11> catalog = new HashMap<>();
        catalog.put(1, new Course11(1, "Java"));
        Course11 course = catalog.get(1);

        NavigableMap<Integer, List<Player>> leaderboard =
                new TreeMap<>(Comparator.reverseOrder());//SortSet

        leaderboard.put(100, new ArrayList<>());
        leaderboard.get(100).add(new Player("Alex"));

        Queue<Task> queue = new ArrayDeque<>();
        queue.offer(new Task("Задача 1"));
        queue.offer(new Task("Задача 2"));
        Task nextTask = queue.poll();

        Set<String> tags = new LinkedHashSet<>();
        tags.add("Java");
        tags.add("C#");
        tags.add("Java");

        NavigableMap<Integer, Course11> coursesById = new TreeMap<>();
        coursesById.put(10, new Course11(10, "Java"));
        coursesById.put(20, new Course11(20, "C#"));
        coursesById.put(30, new Course11(30, "Python"));

        Map<Integer, Course11> range =
                coursesById.subMap(10, true, 30, true);

        List<Course11> courseList = new ArrayList<>();
        courseList.add(new Course11(1, "Java"));
        courseList.add(new Course11(2, "C#"));

        Course11 first = courseList.get(0);

        System.out.println("Каталог: " + course);
        System.out.println("Следующая задача: " + nextTask);
        System.out.println("Теги: " + tags);
        System.out.println("Диапазон: " + range.keySet());
        System.out.println("Первый курс: " + first.name);
    }
}