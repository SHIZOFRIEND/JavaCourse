package lb3;

import java.util.*;
import java.util.function.Function;

public class Task7 {
    public static <T> T firstOrThrow(List<T> list) {
        if (list == null || list.isEmpty()) {
            throw new IllegalArgumentException("Список пуст.");
        }
        return list.get(0);
    }
    public static <T> void swap(List<T> list, int i, int j) {
        if (list == null) {
            throw new IllegalArgumentException("Список не может быть null.");
        }
        if (i < 0 || i >= list.size() ||
                j < 0 || j >= list.size()) {
            throw new IndexOutOfBoundsException("Индекс находится вне границ списка.");
        }
        T temp = list.get(i);
        list.set(i, list.get(j));
        list.set(j, temp);
    }
    public static <T, K> Map<K, T> indexBy(List<T> list, Function<T, K> keyFunction) {
        if (list == null) {
            throw new IllegalArgumentException("Список не может быть null.");
        }
        if (keyFunction == null) {
            throw new IllegalArgumentException("Функция получения ключа не может быть null.");
        }
        Map<K, T> result = new HashMap<>();
        for (T item : list) {
            K key = keyFunction.apply(item);
            if (result.containsKey(key)) {
                throw new IllegalArgumentException("Конфликт ключа: " + key);
            }
            result.put(key, item);
        }
        return result;
    }

    static class Course {
        int id;
        String name;
        Course(int id, String name) {
            this.id = id;
            this.name = name;
        }
        @Override
        public String toString() {
            return "lb3.Course{id=" + id + ", name='" + name + "'}";
        }
    }

    public static void main(String[] args) {
        System.out.println("firstOrThrow");
        List<String> names = new ArrayList<>();
        names.add("Иван");
        names.add("Пётр");
        names.add("Анна");
        String first = firstOrThrow(names);
        System.out.println("Первый элемент: " + first);
        System.out.println("swap");
        System.out.println("До: " + names);
        swap(names, 0, 2);
        System.out.println("После: " + names);
        System.out.println("indexBy");
        List<Course> courses = new ArrayList<>();
        courses.add(new Course(101, "Java"));
        courses.add(new Course(102, "C#"));
        courses.add(new Course(103, "Python"));
        Map<Integer, Course> courseIndex = indexBy(courses, course -> course.id);
        for (Map.Entry<Integer, Course> entry : courseIndex.entrySet()) {
            System.out.println(entry.getKey() + " -> " + entry.getValue());
        }
        System.out.println("Проверка конфликта ключей");
        courses.add(new Course(102, "C++"));
        try {
            indexBy(courses, course -> course.id);
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }
}
