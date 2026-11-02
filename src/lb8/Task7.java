
package lb8;

import java.util.*;

class EnrollmentEvent {
    private final String studentId;
    private final String courseId;
    private final int level;
    private final boolean completed;

    public EnrollmentEvent(String studentId, String courseId,
                           int level, boolean completed) {
        this.studentId = studentId;
        this.courseId = courseId;
        this.level = level;
        this.completed = completed;
    }

    public String getStudentId() {
        return studentId;
    }

    public String getCourseId() {
        return courseId;
    }

    public int getLevel() {
        return level;
    }

    public boolean isCompleted() {
        return completed;
    }
}

class EnrollmentKey {
    private final String studentId;
    private final String courseId;

    public EnrollmentKey(String studentId, String courseId) {
        this.studentId = studentId;
        this.courseId = courseId;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof EnrollmentKey)) return false;

        EnrollmentKey other = (EnrollmentKey) obj;

        return studentId.equals(other.studentId)
                && courseId.equals(other.courseId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(studentId, courseId);
    }
}

class CourseAccumulator {
    int enrolled;
    int completed;

    void add(boolean isCompleted) {
        enrolled++;

        if (isCompleted) {
            completed++;
        }
    }
}

class CourseSummary {
    private final String courseId;
    private final int enrolled;
    private final int completed;
    private final double completionRate;

    public CourseSummary(String courseId, int enrolled, int completed) {
        this.courseId = courseId;
        this.enrolled = enrolled;
        this.completed = completed;
        this.completionRate = enrolled == 0
                ? 0
                : completed * 100.0 / enrolled;
    }

    public String getCourseId() {
        return courseId;
    }

    public int getEnrolled() {
        return enrolled;
    }

    public int getCompleted() {
        return completed;
    }

    public double getCompletionRate() {
        return completionRate;
    }

    @Override
    public String toString() {
        return courseId +
                " (регистраций=" + enrolled +
                ", завершено=" + completed +
                ", completion rate=" +
                String.format(Locale.US, "%.2f", completionRate) +
                "%)";
    }
}

class CourseHubReport {
    private final int uniqueStudents;
    private final Map<String, CourseSummary> completionRates;
    private final List<CourseSummary> top3;
    private final Map<Integer, Integer> levelDistribution;
    private final List<String> anomalies;

    public CourseHubReport(
            int uniqueStudents,
            Map<String, CourseSummary> completionRates,
            List<CourseSummary> top3,
            Map<Integer, Integer> levelDistribution,
            List<String> anomalies) {

        this.uniqueStudents = uniqueStudents;

        this.completionRates = Collections.unmodifiableMap(
                new TreeMap<>(completionRates)
        );

        this.top3 = Collections.unmodifiableList(
                new ArrayList<>(top3)
        );

        this.levelDistribution = Collections.unmodifiableMap(
                new TreeMap<>(levelDistribution)
        );

        this.anomalies = Collections.unmodifiableList(
                new ArrayList<>(anomalies)
        );
    }

    @Override
    public String toString() {
        return "Уникальных студентов: " + uniqueStudents +
                "\nCompletion rate по курсам: " + completionRates +
                "\nTop-3 курсов: " + top3 +
                "\nРаспределение по level: " + levelDistribution +
                "\nАномалии: " + anomalies;
    }
}

public class Task7 {

    private static final int MAX_ANOMALIES = 100;

    public static CourseHubReport createReport(
            Iterator<EnrollmentEvent> events) {

        Set<String> students = new HashSet<>();

        Set<EnrollmentKey> processedEnrollments = new HashSet<>();

        Map<String, CourseAccumulator> courses = new HashMap<>();

        Map<Integer, Integer> levelDistribution = new TreeMap<>();

        List<String> anomalies = new ArrayList<>();

        while (events.hasNext()) {

            EnrollmentEvent event = events.next();

            if (event == null) {
                addAnomaly(anomalies, "Пустое событие");
                continue;
            }

            String studentId = event.getStudentId();
            String courseId = event.getCourseId();

            if (studentId == null || studentId.trim().isEmpty()) {
                addAnomaly(anomalies, "Некорректный studentId");
                continue;
            }

            if (courseId == null || courseId.trim().isEmpty()) {
                addAnomaly(anomalies, "Некорректный courseId");
                continue;
            }

            if (event.getLevel() < 1 || event.getLevel() > 5) {
                addAnomaly(anomalies,
                        "Некорректный level у курса " + courseId);
                continue;
            }

            EnrollmentKey key = new EnrollmentKey(
                    studentId, courseId
            );

            if (!processedEnrollments.add(key)) {
                addAnomaly(anomalies,
                        "Повторная регистрация: " +
                                studentId + " -> " + courseId);
                continue;
            }

            students.add(studentId);

            CourseAccumulator accumulator =
                    courses.get(courseId);

            if (accumulator == null) {
                accumulator = new CourseAccumulator();
                courses.put(courseId, accumulator);
            }

            accumulator.add(event.isCompleted());


            levelDistribution.put(
                    event.getLevel(),
                    levelDistribution.getOrDefault(event.getLevel(), 0) + 1
            );
        }

        Map<String, CourseSummary> summaries = new TreeMap<>();

        for (Map.Entry<String, CourseAccumulator> entry
                : courses.entrySet()) {

            CourseAccumulator value = entry.getValue();

            summaries.put(
                    entry.getKey(),
                    new CourseSummary(
                            entry.getKey(),
                            value.enrolled,
                            value.completed
                    )
            );
        }

        List<CourseSummary> sortedCourses =
                new ArrayList<>(summaries.values());

        sortedCourses.sort(
                Comparator.comparingInt(CourseSummary::getEnrolled)
                        .reversed()
                        .thenComparing(CourseSummary::getCourseId)
        );

        List<CourseSummary> top3 = new ArrayList<>(
                sortedCourses.subList(
                        0,
                        Math.min(3, sortedCourses.size())
                )
        );

        return new CourseHubReport(
                students.size(),
                summaries,
                top3,
                levelDistribution,
                anomalies
        );
    }

    private static void addAnomaly(
            List<String> anomalies, String message) {

        if (anomalies.size() < MAX_ANOMALIES) {
            anomalies.add(message);
        }
    }

    public static void main(String[] args) {

        List<EnrollmentEvent> data = Arrays.asList(
                new EnrollmentEvent("S1", "Java", 1, true),
                new EnrollmentEvent("S2", "Java", 1, false),
                new EnrollmentEvent("S3", "Python", 2, true),
                new EnrollmentEvent("S1", "Python", 2, true),
                new EnrollmentEvent("S4", "C++", 3, false),
                new EnrollmentEvent("S5", "C++", 3, true),
                new EnrollmentEvent("S6", "Java", 1, true),
                new EnrollmentEvent("S1", "Java", 1, true),
                new EnrollmentEvent("S7", "SQL", 0, false)
        );

        Iterator<EnrollmentEvent> iterator = data.iterator();

        CourseHubReport report = createReport(iterator);

        System.out.println(report);
    }
}
