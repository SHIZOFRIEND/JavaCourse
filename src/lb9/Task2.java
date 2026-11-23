
package lb9;

public class Task2 {

    static boolean running = true;

    static volatile boolean volatileRunning = true;

    public static void main(String[] args) throws InterruptedException {

        System.out.println("Запуск worker без volatile");

        Thread worker1 = new Thread(() -> {
            long counter = 0;

            while (running) {
                counter++;
            }

            System.out.println("Worker без volatile остановлен");
            System.out.println("Итераций: " + counter);
        });

        worker1.start();

        Thread.sleep(1000);

        running = false;

        System.out.println("Отправлена команда остановки без volatile");

        worker1.join(2000);

        if (worker1.isAlive()) {
            System.out.println("Worker без volatile не увидел изменение флага!");
            worker1.interrupt();
        }

        System.out.println("\nЗапуск worker с volatile");

        Thread worker2 = new Thread(() -> {
            long counter = 0;

            while (volatileRunning) {
                counter++;
            }

            System.out.println("Worker с volatile остановлен");
            System.out.println("Итераций: " + counter);
        });

        worker2.start();

        Thread.sleep(1000);

        volatileRunning = false;

        System.out.println("Отправлена команда остановки с volatile");

        worker2.join();

        System.out.println("Программа завершена");
    }
}
