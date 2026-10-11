package lb3;

import java.util.*;

public class Task4 {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Queue<String> waitlist = new ArrayDeque<>();
        System.out.println("FIFO Waitlist");
        System.out.print("Сколько студентов добавить в очередь: ");
        int n = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < n; i++) {
            System.out.print("Студент " + (i + 1) + ": ");
            String student = scanner.nextLine();
            waitlist.offer(student);
        }
        System.out.println("Очередь:");
        System.out.println(waitlist);
        System.out.println("Следующий студент");
        if (waitlist.peek() != null) {
            System.out.println("Следующий: " + waitlist.peek());
        } else {
            System.out.println("Очередь пуста.");
        }
        System.out.println("Обработка очереди");

        while (!waitlist.isEmpty()) {
            String student = waitlist.poll();
            System.out.println("Обрабатывается: " + student);
        }
        System.out.println("Очередь после обработки:");
        System.out.println(waitlist);

        System.out.println("Проверка пустой очереди");

        if (waitlist.peek() == null) {
            System.out.println("Очередь пуста.");
        }

        String student = waitlist.poll();

        if (student == null) {
            System.out.println("Извлечь элемент нельзя: очередь пуста.");
        }

        scanner.close();
    }
}