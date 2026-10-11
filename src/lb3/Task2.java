package lb3;

import java.util.*;

public class Task2 {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        List<String> courseCodes = new ArrayList<>();
        System.out.println("CourseCode");
        System.out.print("Сколько кодов курсов добавить: ");
        int n = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < n; i++) {
            System.out.print("CourseCode " + (i + 1) + ": ");
            courseCodes.add(scanner.nextLine());
        }
        System.out.println("Исходный список:");
        for (String code : courseCodes) {
            System.out.println(code);
        }
        Set<String> uniqueCourseCodes = new LinkedHashSet<>(courseCodes);
        System.out.println("После удаления дубликатов:");
        for (String code : uniqueCourseCodes) {
            System.out.println(code);
        }
        System.out.println("Количество исходных кодов: " + courseCodes.size());
        System.out.println("Количество уникальных кодов: " + uniqueCourseCodes.size());
        System.out.println("Удалено дубликатов: " + (courseCodes.size() - uniqueCourseCodes.size()));
        System.out.println("Вывод");
        System.out.println("Выбран LinkedHashSet, так как он удаляет дубликаты и сохраняет порядок первого появления элементов.");
        System.out.println("Уникальность определяется с помощью equals() и hashCode(). Если два объекта равны по equals(), они должны иметь одинаковый hashCode().");
        scanner.close();
    }
}