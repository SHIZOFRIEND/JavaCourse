
package lb9;

import java.util.Set;
import java.util.HashSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.Semaphore;

public class Task6 {

    static final int CAPACITY = 100;
    static final int STUDENTS = 1000;
    static final int THREADS = 20;

    static class CourseWithLock {

        private final int capacity;
        private final Set<Integer> students = new HashSet<>();

        public CourseWithLock(int capacity) {
            this.capacity = capacity;
        }

        public synchronized boolean enroll(int studentId) {

            if (students.contains(studentId)) {
                return false;
            }

            if (students.size() >= capacity) {
                return false;
            }

            students.add(studentId);
            return true;
        }

        public synchronized int getCount() {
            return students.size();
        }
    }

    static class CourseWithAtomic {

        private final int capacity;
        private final AtomicInteger count = new AtomicInteger(0);
        private final Set<Integer> students = ConcurrentHashMap.newKeySet();

        public CourseWithAtomic(int capacity) {
            this.capacity = capacity;
        }

        public boolean enroll(int studentId) {

            if (!students.add(studentId)) {
                return false;
            }

            while (true) {

                int current = count.get();

                if (current >= capacity) {
                    students.remove(studentId);
                    return false;
                }

                if (count.compareAndSet(current, current + 1)) {
                    return true;
                }
            }
        }

        public int getCount() {
            return count.get();
        }
    }

    static class CourseWithConcurrentMap {

        private final Semaphore places;
        private final ConcurrentHashMap<Integer, Boolean> students =
                new ConcurrentHashMap<>();

        public CourseWithConcurrentMap(int capacity) {
            places = new Semaphore(capacity);
        }

        public boolean enroll(int studentId) {

            if (students.containsKey(studentId)) {
                return false;
            }

            if (!places.tryAcquire()) {
                return false;
            }

            Boolean previous = students.putIfAbsent(studentId, true);

            if (previous != null) {
                places.release();
                return false;
            }

            return true;
        }

        public int getCount() {
            return students.size();
        }
    }

    public static void main(String[] args) throws InterruptedException {

        CourseWithLock course1 = new CourseWithLock(CAPACITY);
        CourseWithAtomic course2 = new CourseWithAtomic(CAPACITY);
        CourseWithConcurrentMap course3 =
                new CourseWithConcurrentMap(CAPACITY);

        testCourse("Synchronized", course1::enroll, course1::getCount);
        testCourse("AtomicInteger", course2::enroll, course2::getCount);
        testCourse("ConcurrentHashMap + Semaphore",
                course3::enroll, course3::getCount);
    }

    interface EnrollFunction {
        boolean enroll(int studentId);
    }

    interface CountFunction {
        int getCount();
    }

    static void testCourse(
            String name,
            EnrollFunction enroll,
            CountFunction getCount
    ) throws InterruptedException {

        ExecutorService executor = Executors.newFixedThreadPool(THREADS);

        CountDownLatch ready = new CountDownLatch(THREADS);
        CountDownLatch start = new CountDownLatch(1);
        CountDownLatch finish = new CountDownLatch(STUDENTS);

        AtomicInteger successful = new AtomicInteger(0);

        try {
            for (int i = 0; i < STUDENTS; i++) {

                final int studentId = i;

                executor.submit(() -> {
                    try {
                        if (studentId < THREADS) {
                            ready.countDown();
                        }

                        start.await();

                        if (enroll.enroll(studentId)) {
                            successful.incrementAndGet();
                        }

                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    } finally {
                        finish.countDown();
                    }
                });
            }

            if (!ready.await(10, TimeUnit.SECONDS)) {
                throw new RuntimeException("Потоки не готовы");
            }

            start.countDown();

            if (!finish.await(30, TimeUnit.SECONDS)) {
                throw new RuntimeException("Тест превысил время ожидания");
            }

            System.out.println("\n" + name);
            System.out.println("Успешных записей: " + successful.get());
            System.out.println("Мест занято: " + getCount.getCount());
            System.out.println("Вместимость: " + CAPACITY);

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
