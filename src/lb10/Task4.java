
package lb10;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class Task4 {

    static long countPrimes(int limit) {
        long count = 0;

        for (int number = 2; number <= limit; number++) {
            boolean prime = true;

            for (int i = 2; i * i <= number; i++) {
                if (number % i == 0) {
                    prime = false;
                    break;
                }
            }

            if (prime) {
                count++;
            }
        }

        return count;
    }

    static String blockingOperation(int id) throws InterruptedException {
        System.out.println("I/O задача " + id + ": "
                + Thread.currentThread());

        TimeUnit.SECONDS.sleep(1);

        return "Результат I/O задачи " + id;
    }

    static void starvationDemo()
            throws InterruptedException, ExecutionException {

        System.out.println("1. Демонстрация starvation");

        ExecutorService executor = Executors.newFixedThreadPool(2);

        CountDownLatch release = new CountDownLatch(1);

        try {
            Future<?> first = executor.submit(() -> {
                release.await();
                return "Первая задача завершена";
            });

            Future<?> second = executor.submit(() -> {
                release.await();
                return "Вторая задача завершена";
            });

            Future<Long> cpuTask = executor.submit(
                    () -> countPrimes(100000)
            );

            TimeUnit.MILLISECONDS.sleep(500);

            System.out.println("CPU-задача завершена: "
                    + cpuTask.isDone());

            release.countDown();

            first.get();
            second.get();

            System.out.println("Результат CPU: " + cpuTask.get());

        } finally {
            release.countDown();
            shutdownExecutor(executor);
        }
    }

    static void separateExecutors()
            throws InterruptedException, ExecutionException {

        System.out.println("\n2. Разделение исполнителей");

        int cpuThreads = Math.max(
                1,
                Runtime.getRuntime().availableProcessors()
        );

        ExecutorService cpuExecutor =
                Executors.newFixedThreadPool(cpuThreads);

        ExecutorService ioExecutor =
                Executors.newVirtualThreadPerTaskExecutor();

        try {
            Future<Long> cpuTask = cpuExecutor.submit(
                    () -> {
                        System.out.println("CPU-задача: "
                                + Thread.currentThread());

                        return countPrimes(100000);
                    }
            );

            List<Future<String>> ioTasks = new ArrayList<>();

            for (int i = 1; i <= 20; i++) {
                final int id = i;

                ioTasks.add(
                        ioExecutor.submit(
                                () -> blockingOperation(id)
                        )
                );
            }

            System.out.println("Количество простых чисел: "
                    + cpuTask.get());

            for (Future<String> future : ioTasks) {
                System.out.println(future.get());
            }

        } finally {
            shutdownExecutor(cpuExecutor);
            shutdownExecutor(ioExecutor);
        }
    }

    static void shutdownExecutor(ExecutorService executor) {
        executor.shutdown();

        try {
            if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                executor.shutdownNow();

                if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                    System.out.println("Исполнитель не завершился вовремя");
                }
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    public static void main(String[] args) {

        try {
            starvationDemo();
            separateExecutors();

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Главный поток прерван");

        } catch (ExecutionException e) {
            System.out.println("Ошибка выполнения: " + e.getCause());
        }
    }
}
