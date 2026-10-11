package lb3;

import java.util.*;

class CourseIndex {
    private final Map<String, Set<String>> index = new HashMap<>();
    private final Map<String, Set<String>> courseTags = new HashMap<>();

    public void addCourse(String courseId, Set<String> tags) {
        Objects.requireNonNull(courseId, "ID курса не может быть null");
        Objects.requireNonNull(tags, "Теги не могут быть null");

        removeCourse(courseId);

        Set<String> tagsCopy = new HashSet<>();
        for (String tag : tags) {
            tagsCopy.add(Objects.requireNonNull(tag, "Тег не может быть null"));
        }

        courseTags.put(courseId, tagsCopy);

        for (String tag : tagsCopy) {
            index.computeIfAbsent(tag, key -> new HashSet<>())
                    .add(courseId);
        }
    }

    public void removeCourse(String courseId) {
        Objects.requireNonNull(courseId, "ID курса не может быть null");

        Set<String> tags = courseTags.remove(courseId);

        if (tags == null) {
            return;
        }

        for (String tag : tags) {
            Set<String> courses = index.get(tag);

            if (courses != null) {
                courses.remove(courseId);

                if (courses.isEmpty()) {
                    index.remove(tag);
                }
            }
        }
    }

    public Set<String> findByTag(String tag) {
        Objects.requireNonNull(tag, "Тег не может быть null");

        Set<String> courses = index.get(tag);

        if (courses == null) {
            return new HashSet<>();
        }

        return new HashSet<>(courses);
    }

    public Set<String> findByAllTags(Set<String> tags) {
        Objects.requireNonNull(tags, "Теги не могут быть null");

        if (tags.isEmpty()) {
            return new HashSet<>();
        }

        Set<String> result = null;

        for (String tag : tags) {
            Objects.requireNonNull(tag, "Тег не может быть null");

            Set<String> courses = index.get(tag);

            if (courses == null) {
                return new HashSet<>();
            }

            if (result == null) {
                result = new HashSet<>(courses);
            } else {
                result.retainAll(courses);
            }

            if (result.isEmpty()) {
                return result;
            }
        }

        return result;
    }

    public Map<String, Set<String>> getIndex() {
        Map<String, Set<String>> copy = new HashMap<>();

        for (Map.Entry<String, Set<String>> entry : index.entrySet()) {
            copy.put(entry.getKey(), new HashSet<>(entry.getValue()));
        }

        return copy;
    }
}

public class Task13 {
    public static void main(String[] args) {
        CourseIndex index = new CourseIndex();

        index.addCourse("C1", new HashSet<>(
                Arrays.asList("Java", "Backend", "Programming")));

        index.addCourse("C2", new HashSet<>(
                Arrays.asList("Java", "Frontend")));

        index.addCourse("C3", new HashSet<>(
                Arrays.asList("Java", "Backend")));

        index.addCourse("C4", new HashSet<>(
                Arrays.asList("Python", "Backend")));

        System.out.println("Все теги и курсы:");
        System.out.println(index.getIndex());

        System.out.println("\nКурсы с тегом Java:");
        System.out.println(index.findByTag("Java"));

        System.out.println("\nКурсы с тегами Java и Backend:");
        System.out.println(index.findByAllTags(
                new HashSet<>(Arrays.asList("Java", "Backend"))));

        System.out.println("\nКурсы с тегами Java и Python:");
        System.out.println(index.findByAllTags(
                new HashSet<>(Arrays.asList("Java", "Python"))));

        index.removeCourse("C3");

        System.out.println("\nПосле удаления курса C3:");
        System.out.println(index.getIndex());

        index.removeCourse("C2");

        System.out.println("\nПосле удаления курса C2:");
        System.out.println(index.getIndex());
    }
}
