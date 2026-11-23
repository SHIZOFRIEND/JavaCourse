
package lb9;

public class Task1 {

    static int counter = 0;

    static final int THREADS = 10;
    static final int INCREMENTS = 100000;

    public static void main(String[] args) throws InterruptedException {

        Thread[] threads = new Thread[THREADS];

        counter = 0;

        for (int i = 0; i < THREADS; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < INCREMENTS; j++) {
                    counter++;
                }
            });

            threads[i].start();
        }

        for (Thread thread : threads) {
            thread.join();
        }

        System.out.println("Без synchronized: " + counter);
        System.out.println("Ожидаемый результат: " + (THREADS * INCREMENTS));


        counter = 0;

        for (int i = 0; i < THREADS; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < INCREMENTS; j++) {
                    increment();
                }
            });

            threads[i].start();
        }

        for (Thread thread : threads) {
            thread.join();
        }

        System.out.println("С synchronized методом: " + counter);
        counter = 0;

        for (int i = 0; i < THREADS; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < INCREMENTS; j++) {
                    synchronized (Task1.class) {
                        counter++;
                    }
                }
            });

            threads[i].start();
        }

        for (Thread thread : threads) {
            thread.join();
        }

        System.out.println("С synchronized блоком: " + counter);
    }

    static synchronized void increment() {
        counter++;
    }
}
