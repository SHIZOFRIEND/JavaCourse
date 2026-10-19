package lb2;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class CourseTest {
    static class Course {
        private int hours;
        private String code;
        private String status;
        private int places;
        private boolean prerequisite;
        Course(int hours) {
            if (hours < 1 || hours > 100) {
                throw new IllegalArgumentException("INVALID_HOURS");}
            this.hours = hours;
        }
        Course(String code) {
            this.code = normalizeCode(code);
        }
        Course(String code, String status, int places, boolean prerequisite) {
            this.code = normalizeCode(code);
            this.status = status;
            this.places = places;
            this.prerequisite = prerequisite;
        }
        void completeHours(int completed) {
            if (completed < 0 || completed > hours) {
                throw new IllegalArgumentException("INVALID_COMPLETED_HOURS");
            }
            hours -= completed;
        }
        int getHours() {
            return hours;
        }
        String getCode() {
            return code;
        }
        String getStatus() {
            return status;
        }
        int getPlaces() {
            return places;
        }
        boolean hasPrerequisite() {
            return prerequisite;
        }
        static String normalizeCode(String code) {

            if (code == null || code.trim().isEmpty()) {
                throw new IllegalArgumentException("INVALID_CODE");
            }
            String result = code.trim().toUpperCase();
            if (!result.matches("[A-Z]{2,4}-[0-9]{3}")) {
                throw new IllegalArgumentException("INVALID_CODE");
            }
            return result;
        }
    }


    @Test
    void hoursBelowMinimumShouldBeRejectedTask9() {
        assertThrows(IllegalArgumentException.class,() -> new Course(0));}
    @Test
    void minimumHoursShouldBeAcceptedTask9() {
        Course course = new Course(1);
        assertEquals(1, course.getHours());
    }
    @Test
    void maximumHoursShouldBeAcceptedTask9() {
        Course course = new Course(100);
        assertEquals(100, course.getHours());
    }
    @Test
    void hoursAboveMaximumShouldBeRejectedTask9() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new Course(101));}

    @ParameterizedTest(name = "normalizeCode({0}) = {1}")
    @CsvSource({
            "' java-101 ', JAVA-101",
            "'Java-101', JAVA-101",
            "' java-202', JAVA-202",
            "'MATH-100', MATH-100"
    })
    void normalizeCourseCode(String input, String expected) {
        String result = Course.normalizeCode(input);
        assertEquals(expected, result);
    }
    @ParameterizedTest(name = "invalid code {0} should be rejected")
    @CsvSource({
            "'java'",
            "'123-123'",
            "'JAVA'",
            "'JAVA-12'",
            "'JAVA-1234'"
    })
    void invalidCourseCodeShouldBeRejected(String input) {
        assertThrows(
                IllegalArgumentException.class,
                () -> Course.normalizeCode(input) );}
 

    @Test
    void invalidCompleteHoursShouldNotChangeCourseTask11() {

        Course course = new Course(100);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,() -> course.completeHours(150));
        assertEquals("INVALID_COMPLETED_HOURS",exception.getMessage()
        );
        assertEquals(100, course.getHours());
    }
    @Test
    void negativeCompletedHoursShouldNotChangeCourseTask11() {
        Course course = new Course(50);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,() -> course.completeHours(-1));
        assertEquals("INVALID_COMPLETED_HOURS", exception.getMessage());
        assertEquals(50, course.getHours());
    }
    @Test
    void courseShouldHaveIndependentStateTask12() {
        Course course = new Course(100);
        course.completeHours(20);
        assertEquals(80, course.getHours());
    }
    @Test
    void anotherCourseShouldStartFromItsOwnStateTask12() {
        Course course = new Course(50);
        assertEquals(50, course.getHours());
    }
    @Test
    void courseShouldNotDependOnCurrentDirectoryTask12() {
        Course course = new Course(30);
        assertEquals(30, course.getHours());
    }
    @Test
    void courseCodeShouldBeCheckedByValueTask12() {
        Course course = new Course(" java-101 ");
        assertEquals("JAVA-101", course.getCode());
    }



//lb5.Task13
    static String enroll(
            String status,
            int places,
            boolean prerequisite,
            boolean repeated) {

        if (!"OPEN".equals(status)) {
            return "COURSE_CLOSED";
        }

        if (places <= 0) {
            return "NO_PLACES";
        }

        if (!prerequisite) {
            return "PREREQUISITE_REQUIRED";
        }

        if (repeated) {
            return "ALREADY_ENROLLED";
        }

        return "ENROLLED";
    }
    @Test
    void enrollmentShouldSucceedWhenAllConditionsAreCorrect() {

        String result = enroll("OPEN",10,true,false);
        assertEquals("ENROLLED", result);
    }
    @Test
    void enrollmentShouldRejectClosedCourse() {

        String result = enroll("CLOSED",10,true,false);
        assertEquals("COURSE_CLOSED", result);
    }
    @Test
    void enrollmentShouldRejectWhenNoPlaces() {
        String result = enroll("OPEN",0,true,false);
        assertEquals("NO_PLACES", result);
    }
    @Test
    void enrollmentShouldRejectWithoutPrerequisite() {

        String result = enroll("OPEN",10,false,false);
        assertEquals("PREREQUISITE_REQUIRED",result);
    }
    @Test
    void enrollmentShouldRejectRepeatedApplication() {
        String result = enroll("OPEN",10, true,true);
        assertEquals("ALREADY_ENROLLED",result
        );
    }
}