package lb3;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Objects;

class Course10 {
    private final int id;
    private String name;
    private String description;

    public Course10(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Course10)) return false;

        Course10 course = (Course10) obj;
        return id == course.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "lb3.Course{id=" + id + ", name='" + name + "'}";
    }
}

public class Task10 {
    public static void main(String[] args) {
        Course10 course1 = new Course10(1, "Java", "Основы Java");
        Course10 course2 = new Course10(1, "Java для начинающих", "Другой текст");

        Set<Course10> courses = new HashSet<>();
        courses.add(course1);
        courses.add(course2);

        System.out.println("Размер HashSet: " + courses.size());
        System.out.println("Содержит course2: " + courses.contains(course2));

        Map<Course10, String> map = new HashMap<>();
        map.put(course1, "Первый курс");

        System.out.println("Значение по course2: " + map.get(course2));

        course1.setDescription("Новое описание");

        System.out.println("После изменения описания:");
        System.out.println("Содержит course1: " + courses.contains(course1));
        System.out.println("Значение в HashMap: " + map.get(course1));

        course1.setName("C#");

        System.out.println("После изменения названия:");
        System.out.println("Содержит course1: " + courses.contains(course1));
        System.out.println("Значение в HashMap: " + map.get(course1));
    }
}