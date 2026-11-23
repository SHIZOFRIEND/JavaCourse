
package lb9;

import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class Task5 {

    @Test
    void testConcurrentCounter() throws InterruptedException {

        int threadCount = 10;
        int increments = 10000;

        int[] counter = {0};

        ExecutorService executor =
                Executors.newFixedThreadPool(threadCount);

        CountDownLatch ready = new CountDownLatch(threadCount);
        CountDownLatch start = new CountDownLatch(1);
        CountDownLatch finish = new CountDownLatch(threadCount);

        try {
            for (int i = 0; i < threadCount; i++) {

                executor.submit(() -> {
                    ready.countDown();

                    try {
                        start.await();

                        for (int j = 0; j < increments; j++) {
                            counter[0]++;
                        }

                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    } finally {
                        finish.countDown();
                    }
                });
            }

            assertTrue(ready.await(5, TimeUnit.SECONDS));

            start.countDown();

            assertTrue(finish.await(10, TimeUnit.SECONDS));

            System.out.println("Ожидаемый результат: "
                    + (threadCount * increments));

            System.out.println("Фактический результат: "
                    + counter[0]);

            assertEquals(threadCount * increments, counter[0]);

        } finally {
            start.countDown();
            executor.shutdown();

            if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                executor.shutdownNow();

                executor.awaitTermination(5, TimeUnit.SECONDS);
            }
        }
    }

    @Test
    void testAtomicCounter() throws InterruptedException {

        int threadCount = 10;
        int increments = 10000;

        AtomicInteger counter = new AtomicInteger(0);

        ExecutorService executor =
                Executors.newFixedThreadPool(threadCount);

        CountDownLatch ready = new CountDownLatch(threadCount);
        CountDownLatch start = new CountDownLatch(1);
        CountDownLatch finish = new CountDownLatch(threadCount);

        try {
            for (int i = 0; i < threadCount; i++) {

                executor.submit(() -> {
                    ready.countDown();

                    try {
                        start.await();

                        for (int j = 0; j < increments; j++) {
                            counter.incrementAndGet();
                        }

                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    } finally {
                        finish.countDown();
                    }
                });
            }

            assertTrue(ready.await(5, TimeUnit.SECONDS));

            start.countDown();

            assertTrue(finish.await(10, TimeUnit.SECONDS));

            assertEquals(threadCount * increments, counter.get());

            System.out.println("AtomicInteger: " + counter.get());

        } finally {
            start.countDown();
            executor.shutdown();

            if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                executor.shutdownNow();

                executor.awaitTermination(5, TimeUnit.SECONDS);
            }
        }
    }
}
