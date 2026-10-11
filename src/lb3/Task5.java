package lb3;

import java.util.*;

public class Task5 {

    public static void main(String[] args) {

        System.out.println("Ошибка при удалении в enhanced for");

        List<String> students = new ArrayList<>();
        students.add("Иван");
        students.add("Пётр");
        students.add("Анна");
        students.add("Сергей");
        students.add("Мария");

        System.out.println("Исходный список: " + students);

        try {
            for (String student : students) {
                if (student.equals("Анна")) {
                    students.remove(student);
                }
            }
        } catch (ConcurrentModificationException e) {
            System.out.println("Возникло исключение: ConcurrentModificationException");
        }

        System.out.println("Список после ошибки: " + students);


        System.out.println("2. Удаление через Iterator");

        students.clear();
        students.add("Иван");
        students.add("Пётр");
        students.add("Анна");
        students.add("Сергей");
        students.add("Мария");

        Iterator<String> iterator = students.iterator();

        while (iterator.hasNext()) {
            String student = iterator.next();

            if (student.equals("Анна")) {
                iterator.remove();
            }
        }
        System.out.println("После удаления через Iterator: " + students);

        System.out.println("\3. Удаление через removeIf");

        students.clear();
        students.add("Иван");
        students.add("Пётр");
        students.add("Анна");
        students.add("Сергей");
        students.add("Мария");

        students.removeIf(student -> student.equals("Анна"));

        System.out.println("После удаления через removeIf: " + students);


        System.out.println("4. Удаление нескольких элементов");

        students.clear();
        students.add("Иван");
        students.add("Пётр");
        students.add("Анна");
        students.add("Алексей");
        students.add("Сергей");

        students.removeIf(student -> student.startsWith("А"));

        System.out.println("Удалены студенты на 'А': " + students);


        System.out.println("Вывод");
        System.out.println("Удалять элементы напрямую из списка во время enhanced for нельзя, " +
                        "потому что это изменяет коллекцию во время работы её итератора."
        );

        System.out.println("Iterator.remove() безопасно удаляет текущий элемент через сам итератор.");

        System.out.println("removeIf() удобен, когда нужно удалить все элементы, соответствующие условию.");

        System.out.println("Копирование всей коллекции не всегда является лучшим решением, " +
                        "потому что требует дополнительной памяти и создаёт лишнюю копию данных."
        );
    }
}