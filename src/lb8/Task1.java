
package lb8;

import java.util.*;

class Task {
    private int level;
    private int duration;
    private String code;

    public Task(int level, int duration, String code) {
        this.level = level;
        this.duration = duration;
        this.code = code;
    }

    public int getLevel() {
        return level;
    }

    public int getDuration() {
        return duration;
    }

    public String getCode() {
        return code;
    }

    @Override
    public String toString() {
        return "Task{" +
                "level=" + level +
                ", duration=" + duration +
                ", code='" + code + '\'' +
                '}';
    }
}

public class Task1 {

    public static void main(String[] args) {

        List<Task> tasks = new ArrayList<>();

        tasks.add(new Task(2, 30, "JAVA"));
        tasks.add(new Task(1, 20, "SQL"));
        tasks.add(new Task(2, 10, "C++"));
        tasks.add(new Task(1, 20, "JAVA"));
        tasks.add(new Task(2, 30, "C#"));
        tasks.add(new Task(1, 10, "PYTHON"));

        Comparator<Task> comparator = Comparator
                .comparingInt(Task::getLevel)
                .thenComparingInt(Task::getDuration)
                .thenComparing(Task::getCode);

        tasks.sort(comparator);

        for (Task task : tasks) {
            System.out.println(task);
        }
    }
}
