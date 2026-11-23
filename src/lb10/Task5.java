
package lb10;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class Task5 {

    static final int TASKS = 1000;
    static final int PLATFORM_THREADS = 100;
    static final int WAIT_TIME = 200;

    static class ExperimentResult {
        long time;
        int threadsCreated;
        int maxActive;
        long memoryDifference;

        ExperimentResult(
                long time,
                int threadsCreated,
                int maxActive,
                long memoryDifference
        ) {
            this.time = time;
            this.threadsCreated = threadsCreated;
            this.maxActive = maxActive;
            this.memoryDifference = memoryDifference;
        }
    }

    static ExperimentResult runExperiment(
            String name,
            ExecutorService executor,
            AtomicInteger threadsCreated
    ) throws InterruptedException, ExecutionException {

        AtomicInteger active = new AtomicInteger(0);
        AtomicInteger maxActive = new AtomicInteger(0);

        List<Future<?>> futures = new ArrayList<>();

        Runtime runtime = Runtime.getRuntime();

        long memoryBefore =
                runtime.totalMemory() - runtime.freeMemory();

        long start = System.nanoTime();

        try {
            for (int i = 0; i < TASKS; i++) {

                futures.add(executor.submit(() -> {
                    int current = active.incrementAndGet();

                    maxActive.updateAndGet(
                            value -> Math.max(value, current)
                    );

                    try {
                        CountDownLatch latch = new CountDownLatch(1);
                        latch.await(WAIT_TIME, TimeUnit.MILLISECONDS);

                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();

                    } finally {
                        active.decrementAndGet();
                    }
                }));
            }

            for (Future<?> future : futures) {
                future.get();
            }

        } finally {
            executor.shutdown();

            if (!executor.awaitTermination(10, TimeUnit.SECONDS)) {
                executor.shutdownNow();

                if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                    System.out.println("Исполнитель не завершился");
                }
            }
        }

        long end = System.nanoTime();

        long memoryAfter =
                runtime.totalMemory() - runtime.freeMemory();

        long time = TimeUnit.NANOSECONDS.toMillis(end - start);

        long memoryDifference =
                (memoryAfter - memoryBefore) / 1024;

        System.out.println("\n" + name);
        System.out.println("Количество задач: " + TASKS);
        System.out.println("Время выполнения: " + time + " мс");
        System.out.println("Создано platform threads: "
                + threadsCreated.get());
        System.out.println("Максимум одновременно выполняющихся задач: "
                + maxActive.get());
        System.out.println("Изменение heap-памяти: "
                + memoryDifference + " КБ");

        return new ExperimentResult(
                time,
                threadsCreated.get(),
                maxActive.get(),
                memoryDifference
        );
    }

    public static void main(String[] args) {

        try {
            AtomicInteger platformCreated = new AtomicInteger(0);

            ThreadFactory platformFactory = task -> {
                platformCreated.incrementAndGet();
                return new Thread(task);
            };

            ExecutorService platformExecutor =
                    Executors.newFixedThreadPool(
                            PLATFORM_THREADS,
                            platformFactory
                    );

            ExperimentResult platformResult = runExperiment(
                    "Platform threads",
                    platformExecutor,
                    platformCreated
            );

            AtomicInteger virtualCreated = new AtomicInteger(0);

            ExecutorService virtualExecutor =
                    Executors.newVirtualThreadPerTaskExecutor();

            ExperimentResult virtualResult = runExperiment(
                    "Virtual threads",
                    virtualExecutor,
                    virtualCreated
            );

            System.out.println("\nСравнение:");

            System.out.println("Platform threads: "
                    + platformResult.time + " мс");

            System.out.println("Virtual threads: "
                    + virtualResult.time + " мс");

            System.out.println("Создано platform threads: "
                    + platformResult.threadsCreated);

            System.out.println("Создано virtual threads: "
                    + TASKS);

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Главный поток прерван");

        } catch (ExecutionException e) {
            System.out.println("Ошибка задачи: " + e.getCause());
        }
    }
}
