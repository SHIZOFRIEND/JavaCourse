
package lb10;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Task2 {

    static class Course {
        int id;
        String name;
        String teacher;

        Course(int id, String name, String teacher) {
            this.id = id;
            this.name = name;
            this.teacher = teacher;
        }
    }

    static class User {
        int id;
        String name;

        User(int id, String name) {
            this.id = id;
            this.name = name;
        }
    }

    static class Enrollment {
        int courseId;
        String courseName;

        Enrollment(int courseId, String courseName) {
            this.courseId = courseId;
            this.courseName = courseName;
        }

        @Override
        public String toString() {
            return "Курс: " + courseName + ", ID: " + courseId;
        }
    }

    static Course loadCourse(int id) {
        System.out.println("Загрузка курса: " + Thread.currentThread().getName());

        return new Course(id, "Java", "Иванов");
    }

    static User loadUser(int id) {
        System.out.println("Загрузка пользователя: " + Thread.currentThread().getName());

        return new User(id, "Александр");
    }

    static List<Enrollment> loadEnrollments(User user) {
        System.out.println("Загрузка записей пользователя "
                + user.name + ": " + Thread.currentThread().getName());

        return Arrays.asList(
                new Enrollment(1, "Java"),
                new Enrollment(2, "C++"),
                new Enrollment(3, "Python")
        );
    }

    public static void main(String[] args) {

        ExecutorService executor = Executors.newFixedThreadPool(3);

        try {
            CompletableFuture<Void> courseFuture =
                    CompletableFuture.supplyAsync(
                                    () -> loadCourse(1),
                                    executor
                            )
                            .thenApply(course -> {
                                System.out.println("Форматирование: "
                                        + Thread.currentThread().getName());

                                return "Карточка курса\n"
                                        + "Название: " + course.name + "\n"
                                        + "Преподаватель: " + course.teacher + "\n"
                                        + "ID: " + course.id;
                            })
                            .thenAccept(card -> {
                                System.out.println("Вывод карточки: "
                                        + Thread.currentThread().getName());

                                System.out.println(card);
                            });

            CompletableFuture<List<Enrollment>> enrollmentFuture =
                    CompletableFuture.supplyAsync(
                                    () -> loadUser(1),
                                    executor
                            )
                            .thenCompose(user ->
                                    CompletableFuture.supplyAsync(
                                            () -> loadEnrollments(user),
                                            executor
                                    )
                            );

            enrollmentFuture.thenAccept(enrollments -> {
                System.out.println("\nЗаписи пользователя:");

                for (Enrollment enrollment : enrollments) {
                    System.out.println(enrollment);
                }
            });

            CompletableFuture.allOf(
                    courseFuture,
                    enrollmentFuture
            ).join();

        } finally {
            executor.shutdown();
        }
    }
}
