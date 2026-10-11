package lb3;

import java.util.*;

public class Task8 {
    public static <T extends Comparable<? super T>> T max(List<T> list) {
        if (list == null || list.isEmpty()) {
            throw new IllegalArgumentException("Список пуст.");
        }
        T maximum = list.get(0);
        for (int i = 1; i < list.size(); i++) {
            if (list.get(i).compareTo(maximum) > 0) {
                maximum = list.get(i);
            }
        }
        return maximum;
    }
    static class CourseScore implements Comparable<CourseScore> {
        private final String course;
        private final int score;

        public CourseScore(String course, int score) {
            this.course = course;
            this.score = score;
        }

        @Override
        public int compareTo(CourseScore other) {
            return Integer.compare(this.score, other.score);
        }

        @Override
        public String toString() {
            return course + " (" + score + ")";
        }
    }

    public static void main(String[] args) {

        System.out.println("Integer");
        List<Integer> numbers = Arrays.asList(15, 42, 7, 91, 28);
        System.out.println("Список: " + numbers);
        System.out.println("Максимум: " + max(numbers));
        System.out.println("String");
        List<String> names = Arrays.asList("Java", "C#", "Python", "C++");
        System.out.println("Список: " + names);
        System.out.println("Максимум: " + max(names));

        System.out.println("CourseScore");
        List<CourseScore> scores = Arrays.asList(
                new CourseScore("Java", 85),
                new CourseScore("C#", 92),
                new CourseScore("Python", 78),
                new CourseScore("C++", 88)
        );
        System.out.println("Список: " + scores);
        System.out.println("Максимальный результат: " + max(scores));
    }
}