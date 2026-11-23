
package lb9;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class Task4 {

    private static final int QUEUE_SIZE = 5;
    private static final int PRODUCERS = 2;
    private static final int CONSUMERS = 3;
    private static final int REQUESTS_PER_PRODUCER = 5;

    private static final Request STOP = new Request(-1);

    static class Request {
        private final int id;

        public Request(int id) {
            this.id = id;
        }

        public boolean isStop() {
            return id == -1;
        }

        @Override
        public String toString() {
            return "Заявка №" + id;
        }
    }

    static class Producer implements Runnable {

        private final BlockingQueue<Request> queue;
        private final int producerId;
        private final AtomicInteger requestCounter;

        public Producer(BlockingQueue<Request> queue,
                        int producerId,
                        AtomicInteger requestCounter) {
            this.queue = queue;
            this.producerId = producerId;
            this.requestCounter = requestCounter;
        }

        @Override
        public void run() {
            try {
                for (int i = 0; i < REQUESTS_PER_PRODUCER; i++) {
                    Request request =
                            new Request(requestCounter.incrementAndGet());

                    queue.put(request);

                    System.out.println(
                            Thread.currentThread().getName()
                                    + " добавил " + request
                    );

                    Thread.sleep(100);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    static class Consumer implements Runnable {

        private final BlockingQueue<Request> queue;

        public Consumer(BlockingQueue<Request> queue) {
            this.queue = queue;
        }

        @Override
        public void run() {
            try {
                while (true) {
                    Request request = queue.take();

                    if (request.isStop()) {
                        System.out.println(
                                Thread.currentThread().getName()
                                        + " завершил работу"
                        );
                        break;
                    }

                    System.out.println(
                            Thread.currentThread().getName()
                                    + " обрабатывает " + request
                    );

                    // Имитация обработки заявки
                    Thread.sleep(300);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    static void runWithThreads() throws InterruptedException {

        System.out.println("\nОбычные потоки");

        BlockingQueue<Request> queue =
                new ArrayBlockingQueue<>(QUEUE_SIZE);

        AtomicInteger counter = new AtomicInteger(0);

        Thread[] producers = new Thread[PRODUCERS];
        Thread[] consumers = new Thread[CONSUMERS];

        for (int i = 0; i < CONSUMERS; i++) {
            consumers[i] = new Thread(
                    new Consumer(queue),
                    "Consumer-" + (i + 1)
            );

            consumers[i].start();
        }

        for (int i = 0; i < PRODUCERS; i++) {
            producers[i] = new Thread(
                    new Producer(queue, i + 1, counter),
                    "Producer-" + (i + 1)
            );

            producers[i].start();
        }

        for (Thread producer : producers) {
            producer.join();
        }

        for (int i = 0; i < CONSUMERS; i++) {
            queue.put(STOP);
        }

        for (Thread consumer : consumers) {
            consumer.join();
        }

        System.out.println("Все потоки завершены");
    }

    static void runWithExecutor() throws InterruptedException {

        System.out.println("\n=== ExecutorService ===");

        BlockingQueue<Request> queue =
                new ArrayBlockingQueue<>(QUEUE_SIZE);

        AtomicInteger counter = new AtomicInteger(0);

        ExecutorService producerPool =
                Executors.newFixedThreadPool(PRODUCERS, r -> {
                    Thread thread = new Thread(r);
                    thread.setName("ProducerPool-" + thread.getId());
                    return thread;
                });
        ExecutorService consumerPool =
                Executors.newFixedThreadPool(CONSUMERS, r -> {
                    Thread thread = new Thread(r);
                    thread.setName("ConsumerPool-" + thread.getId());
                    return thread;
                });

        try {
            for (int i = 0; i < CONSUMERS; i++) {
                consumerPool.submit(new Consumer(queue));
            }
            for (int i = 0; i < PRODUCERS; i++) {
                producerPool.submit(
                        new Producer(queue, i + 1, counter)
                );
            }
            producerPool.shutdown();

            if (!producerPool.awaitTermination(10, TimeUnit.SECONDS)) {
                producerPool.shutdownNow();
                producerPool.awaitTermination(5, TimeUnit.SECONDS);
            }
            if (!producerPool.isTerminated()) {
                throw new InterruptedException(
                        "Производители не завершились вовремя"
                );
            }

            for (int i = 0; i < CONSUMERS; i++) {
                queue.put(STOP);
            }
            consumerPool.shutdown();

            if (!consumerPool.awaitTermination(10, TimeUnit.SECONDS)) {
                consumerPool.shutdownNow();

                if (!consumerPool.awaitTermination(5, TimeUnit.SECONDS)) {
                    System.out.println(
                            "Пул потребителей не завершился"
                    );
                }
            }

        } finally {
            producerPool.shutdownNow();
            consumerPool.shutdownNow();
        }

        System.out.println("ExecutorService завершил работу");
    }

    public static void main(String[] args) {

        try {
            runWithThreads();
            runWithExecutor();

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Главный поток прерван");
        }
    }
}
