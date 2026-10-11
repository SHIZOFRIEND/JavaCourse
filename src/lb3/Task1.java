package lb3;

import java.util.*;
    public class Task1 {
        public static void main(String[] args) {
            Scanner scanner = new Scanner(System.in);
            List<String> registrations = new ArrayList<>();
            System.out.println("Регистрации пользователей");
            System.out.print("Сколько регистраций добавить: ");
            int n = scanner.nextInt();
            scanner.nextLine();
            for (int i = 0; i < n; i++) {
                System.out.print("Регистрация " + (i + 1) + ": ");
                registrations.add(scanner.nextLine());
            }
            System.out.println("Исходный список:");
            printList(registrations);
            System.out.print("Введите регистрацию для вставки: ");
            String newRegistration = scanner.nextLine();
            System.out.print("На какую позицию вставить (0-" + registrations.size() + "): ");
            int position = scanner.nextInt();
            scanner.nextLine();
            if (position >= 0 && position <= registrations.size()) {
                registrations.add(position, newRegistration);
                System.out.println("После вставки:");
                printList(registrations);
            } else {
                System.out.println("Неверная позиция.");
            }
            System.out.print("Введите регистрацию для удаления: ");
            String deleteRegistration = scanner.nextLine();
            if (registrations.remove(deleteRegistration)) {
                System.out.println("Регистрация удалена.");
            } else {
                System.out.println("Такая регистрация не найдена.");
            }
            System.out.println("После удаления:");
            printList(registrations);
            Set<String> unique = new HashSet<>();
            Set<String> duplicates = new LinkedHashSet<>();
            for (String registration : registrations) {
                if (!unique.add(registration)) {
                    duplicates.add(registration);
                }
            }
            System.out.println("Дубликаты:");
            if (duplicates.isEmpty()) {
                System.out.println("Дубликатов нет.");
            } else {
                for (String duplicate : duplicates) {
                    System.out.println(duplicate);
                }
            }
            int size = 100000;
            List<Integer> arrayList = new ArrayList<>();
            List<Integer> linkedList = new LinkedList<>();
            for (int i = 0; i < size; i++) {
                arrayList.add(i);
                linkedList.add(i);
            }
            long start = System.nanoTime();
            long sumArray = 0;
            for (int i = 0; i < size; i++) {
                sumArray += arrayList.get(i);
            }
            long arrayIndexTime = System.nanoTime() - start;
            start = System.nanoTime();
            long sumLinked = 0;
            for (int i = 0; i < size; i++) {
                sumLinked += linkedList.get(i);
            }
            long linkedIndexTime = System.nanoTime() - start;
            start = System.nanoTime();
            long arrayIterationSum = 0;
            for (Integer value : arrayList) {
                arrayIterationSum += value;
            }
            long arrayIterationTime = System.nanoTime() - start;
            start = System.nanoTime();
            long linkedIterationSum = 0;
            for (Integer value : linkedList) {
                linkedIterationSum += value;
            }
            long linkedIterationTime = System.nanoTime() - start;
            System.out.println("Количество элементов: " + size);
            System.out.println("Чтение по индексу:");
            System.out.println("ArrayList:  " + arrayIndexTime + " нс");
            System.out.println("LinkedList: " + linkedIndexTime + " нс");
            System.out.println("Обычный обход через for-each:");
            System.out.println("ArrayList:  " + arrayIterationTime + " нс");
            System.out.println("LinkedList: " + linkedIterationTime + " нс");
            System.out.println("Суммы проверок:");
            System.out.println("ArrayList index:  " + sumArray);
            System.out.println("LinkedList index: " + sumLinked);
            System.out.println("ArrayList обход:  " + arrayIterationSum);
            System.out.println("LinkedList обход: " + linkedIterationSum);
            System.out.println("Вывод");
            if (arrayIndexTime < linkedIndexTime) {
                System.out.println("Для чтения по индексу в данном измерении быстрее ArrayList.");
            } else {
                System.out.println("Для чтения по индексу в данном измерении быстрее LinkedList.");
            }
            if (arrayIterationTime < linkedIterationTime) {
                System.out.println("Для последовательного обхода в данном измерении быстрее ArrayList.");
            } else {
                System.out.println("Для последовательного обхода в данном измерении быстрее LinkedList.");
            }
            System.out.println("Для хранения последовательности регистраций выбран ArrayList, " +
                            "так как нужен быстрый доступ по индексу и удобный последовательный обход."
            );

            scanner.close();
        }
        static void printList(List<String> list) {
            for (int i = 0; i < list.size(); i++) {
                System.out.println(i + ": " + list.get(i));
            }
        }
    }

