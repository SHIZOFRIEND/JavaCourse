
package lb10;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class Task6 {

    static final int DEADLINE_SECONDS = 2;

    static String runTask(String name, int duration)
            throws InterruptedException {

        System.out.println(name + " запущена");

        try {
            TimeUnit.MILLISECONDS.sleep(duration);
            System.out.println(name + " завершена");
            return name + ": успешно";

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println(name + " прервана");
            throw e;
        }
    }

    static void cancelTasks(List<Future<String>> futures) {
        for (Future<String> future : futures) {
            if (!future.isDone()) {
                future.cancel(true);
            }
        }
    }

    public static void main(String[] args) {

        ExecutorService executor = Executors.newFixedThreadPool(3);

        List<Future<String>> futures = new ArrayList<>();

        long deadline = System.nanoTime()
                + TimeUnit.SECONDS.toNanos(DEADLINE_SECONDS);

        boolean completedNormally = false;

        try {
            futures.add(executor.submit(
                    () -> runTask("Задача 1", 500)
            ));

            futures.add(executor.submit(
                    () -> runTask("Задача 2", 900)
            ));

            futures.add(executor.submit(
                    () -> runTask("Задача 3", 3000)
            ));

            for (Future<String> future : futures) {

                long remaining = deadline - System.nanoTime();

                if (remaining <= 0) {
                    System.out.println("Общий deadline истёк");
                    cancelTasks(futures);
                    break;
                }

                try {
                    String result = future.get(
                            remaining,
                            TimeUnit.NANOSECONDS
                    );

                    System.out.println(result);

                } catch (TimeoutException e) {
                    System.out.println("Время ожидания закончилось");
                    cancelTasks(futures);
                    break;

                } catch (ExecutionException e) {
                    System.out.println("Ошибка задачи: "
                            + e.getCause());

                    cancelTasks(futures);
                    break;
                }
            }

            completedNormally = futures.stream()
                    .allMatch(Future::isDone)
                    && futures.stream()
                    .noneMatch(Future::isCancelled);

        } catch (InterruptedException e) {
            cancelTasks(futures);
            Thread.currentThread().interrupt();
            System.out.println("Главный поток прерван");

        } finally {
            if (completedNormally) {
                executor.shutdown();
            } else {
                executor.shutdownNow();
            }

            boolean interrupted = Thread.interrupted();

            try {
                if (!executor.awaitTermination(3, TimeUnit.SECONDS)) {
                    System.out.println("Не все задачи завершились вовремя");
                }
            } catch (InterruptedException e) {
                interrupted = true;
                executor.shutdownNow();
            }

            if (interrupted) {
                Thread.currentThread().interrupt();
            }
        }

        System.out.println("Программа завершена");
    }
}
