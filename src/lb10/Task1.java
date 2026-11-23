
package lb10;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class Task1 {

    public static void main(String[] args) {

        ExecutorService executor = Executors.newFixedThreadPool(3);

        try {
            Runnable runnable = () -> {
                System.out.println("Выполняется Runnable");
            };

            executor.submit(runnable);

            Callable<Integer> callable = () -> {
                System.out.println("Выполняется Callable");
                return 10 + 20;
            };

            Future<Integer> future = executor.submit(callable);

            try {
                Integer result = future.get(3, TimeUnit.SECONDS);
                System.out.println("Результат Callable: " + result);

            } catch (TimeoutException e) {
                System.out.println("Время ожидания результата истекло");
                future.cancel(true);

            } catch (ExecutionException e) {
                System.out.println("Ошибка вычисления: " + e.getCause());

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Главный поток прерван");
            }

            Future<Integer> errorFuture = executor.submit(() -> {
                throw new Exception("Ошибка внутри Callable");
            });

            try {
                System.out.println("Результат: "
                        + errorFuture.get(3, TimeUnit.SECONDS));

            } catch (TimeoutException e) {
                System.out.println("Ошибка: истекло время ожидания");
                errorFuture.cancel(true);

            } catch (ExecutionException e) {
                System.out.println("Ошибка вычисления: " + e.getCause());

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Главный поток прерван");
            }

            Future<String> longTask = executor.submit(() -> {
                try {
                    System.out.println("Долгая задача запущена");

                    while (!Thread.currentThread().isInterrupted()) {
                        TimeUnit.MILLISECONDS.sleep(200);
                    }

                    System.out.println("Долгая задача получила прерывание");
                    return "Задача остановлена";

                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    System.out.println("Долгая задача прервана");
                    return "Задача завершена после прерывания";
                }
            });

            TimeUnit.SECONDS.sleep(1);

            boolean cancelled = longTask.cancel(true);

            System.out.println("Отмена задачи: " + cancelled);

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Главный поток прерван");

        } finally {
            executor.shutdown();

            try {
                if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                    executor.shutdownNow();

                    if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                        System.out.println("Пул не завершился вовремя");
                    }
                }
            } catch (InterruptedException e) {
                executor.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }
    }
}
