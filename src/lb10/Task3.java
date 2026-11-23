
package lb10;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Task3 {

    static class Course {
        int id;
        String name;

        Course(int id, String name) {
            this.id = id;
            this.name = name;
        }

        @Override
        public String toString() {
            return "Course{id=" + id + ", name='" + name + "'}";
        }
    }

    static class LoadResult {
        Course course;
        String error;

        LoadResult(Course course, String error) {
            this.course = course;
            this.error = error;
        }
    }

    static class PartialResult {
        List<Course> courses;
        List<String> errors;

        PartialResult(List<Course> courses, List<String> errors) {
            this.courses = courses;
            this.errors = errors;
        }
    }

    static Course loadCourse(int id) {
        try {
            TimeUnit.MILLISECONDS.sleep((5 - id) * 300L);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }

        if (id == 3) {
            throw new RuntimeException("Курс " + id + " не найден");
        }

        return new Course(id, "Курс " + id);
    }

    static List<Course> loadFailFast(
            List<Integer> ids,
            ExecutorService executor
    ) {

        List<CompletableFuture<Course>> futures = new ArrayList<>();

        for (int id : ids) {
            futures.add(
                    CompletableFuture.supplyAsync(
                            () -> loadCourse(id),
                            executor
                    )
            );
        }

        CompletableFuture<Void> all = CompletableFuture.allOf(
                futures.toArray(new CompletableFuture[0])
        );

        return all.thenApply(v -> {
            List<Course> courses = new ArrayList<>();

            for (CompletableFuture<Course> future : futures) {
                courses.add(future.join());
            }

            return courses;
        }).join();
    }

    static PartialResult loadPartial(
            List<Integer> ids,
            ExecutorService executor
    ) {

        List<CompletableFuture<LoadResult>> futures = new ArrayList<>();

        for (int id : ids) {
            CompletableFuture<LoadResult> future =
                    CompletableFuture.supplyAsync(
                            () -> loadCourse(id),
                            executor
                    ).handle((course, error) -> {
                        if (error != null) {
                            return new LoadResult(
                                    null,
                                    error.getCause() != null
                                            ? error.getCause().getMessage()
                                            : error.getMessage()
                            );
                        }

                        return new LoadResult(course, null);
                    });

            futures.add(future);
        }

        CompletableFuture<Void> all = CompletableFuture.allOf(
                futures.toArray(new CompletableFuture[0])
        );

        return all.thenApply(v -> {
            List<Course> courses = new ArrayList<>();
            List<String> errors = new ArrayList<>();

            for (CompletableFuture<LoadResult> future : futures) {
                LoadResult result = future.join();

                if (result.course != null) {
                    courses.add(result.course);
                }

                if (result.error != null) {
                    errors.add(result.error);
                }
            }

            return new PartialResult(courses, errors);
        }).join();
    }

    public static void main(String[] args) {

        List<Integer> ids = Arrays.asList(1, 2, 3, 4, 5);

        ExecutorService executor = Executors.newFixedThreadPool(3);

        try {
            System.out.println("Политика fail-fast:");

            try {
                List<Course> courses = loadFailFast(ids, executor);

                for (Course course : courses) {
                    System.out.println(course);
                }

            } catch (CompletionException e) {
                System.out.println("Ошибка загрузки: "
                        + e.getCause().getMessage());
            }

            System.out.println("\nПолитика частичного результата:");

            PartialResult result = loadPartial(ids, executor);

            System.out.println("Загруженные курсы:");

            for (Course course : result.courses) {
                System.out.println(course);
            }

            System.out.println("Ошибки:");

            for (String error : result.errors) {
                System.out.println(error);
            }

        } finally {
            executor.shutdown();
        }
    }
}
