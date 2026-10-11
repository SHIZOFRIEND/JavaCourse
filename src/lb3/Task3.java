package lb3;

import java.util.*;

public class Task3 {

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
        Scanner scanner = new Scanner(System.in);
        Map<Integer, Course> courses = new HashMap<>();

        System.out.println("Индекс CourseId -> lb3.Course ");
        System.out.print("Сколько курсов добавить: ");
        int n = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < n; i++) {
            System.out.print("ID курса: ");
            int id = scanner.nextInt();
            scanner.nextLine();
            System.out.print("Название курса: ");
            String name = scanner.nextLine();
            Course course = new Course(id, name);
            if (courses.containsKey(id)) {
                System.out.println("Ошибка: курс с ID " + id + " уже существует.");
            } else {
                courses.put(id, course);
                System.out.println("Курс добавлен.");
            }
        }
        System.out.println("Индекс курсов");
        for (Map.Entry<Integer, Course> entry : courses.entrySet()) {
            System.out.println(entry.getKey() + " -> " + entry.getValue());
        }
        System.out.println("get()");
        System.out.print("Введите ID для поиска: ");
        int searchId = scanner.nextInt();
        Course found = courses.get(searchId);
        if (found != null) {
            System.out.println("Найден курс: " + found);
        } else {
            System.out.println("Курс не найден.");
        }
        System.out.println("containsKey()");
        System.out.print("Введите ID для проверки: ");
        int checkId = scanner.nextInt();

        if (courses.containsKey(checkId)) {
            System.out.println("Такой ID существует.");
        } else {
            System.out.println("Такого ID нет.");
        }
        System.out.println("putIfAbsent()");
        System.out.print("Введите ID для putIfAbsent: ");
        int putId = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Введите название курса: ");
        String putName = scanner.nextLine();
        Course putCourse = new Course(putId, putName);
        Course oldCourse = courses.putIfAbsent(putId, putCourse);
        if (oldCourse == null) {
            System.out.println("Курс добавлен.");
        } else {
            System.out.println("Курс не добавлен: ID уже существует. " + "Существующий курс: " + oldCourse
            );
        }
        System.out.println("computeIfAbsent()");
        System.out.print("Введите ID для computeIfAbsent: ");
        int computeId = scanner.nextInt();
        scanner.nextLine();
        Course result = courses.computeIfAbsent(computeId, id -> {
                    System.out.println("ID отсутствует, создаём новый курс.");
                    return new Course(id, "Новый курс");
                }
        );
        System.out.println("Результат: " + result);
        scanner.close();
    }
}