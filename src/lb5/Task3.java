package lb5;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Task3 {
    private static final Pattern COURSE_PATTERN =
            Pattern.compile("\\A([A-Z]+)-([0-9]+)\\z");

    private static void checkCourse(String code) {
        Matcher matcher = COURSE_PATTERN.matcher(code);

        if (matcher.matches()) {
            String prefix = matcher.group(1);
            String number = matcher.group(2);

            System.out.printf("%-20s | %-10s | %-10s | %-10s%n",
                    "\"" + code + "\"", "Валидный", prefix, number);
        } else {
            System.out.printf("%-20s | %-10s | %-10s | %-10s%n",
                    "\"" + code + "\"", "Невалидный", "-", "-");
        }
    }

    public static void main(String[] args) {
        String[] testCases = {
                "JAVA-101",
                "PYTHON-202",
                "C-1",
                "ABC-99999",
                "java-101",
                "Java-101",
                "JAVA-",
                "-101",
                "101",
                "XJAVA-101Y",
                "JAVA-101 EXTRA",
                " JAVA-101",
                "JAVA-101 ",
                "JAVA -101",
                "JAVA- 101",
                "JАVA-101",
                "JÁVA-101",
                "JAVA-١٠١",
                "JAVA_101",
                "JAVA-101\n"
        };

        System.out.printf("%-20s | %-10s | %-10s | %-10s%n",
                "Код курса", "Результат", "Prefix", "Number");

        for (String code : testCases) {
            checkCourse(code);
        }
    }
}