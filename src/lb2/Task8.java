package lb2;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

public class Task8 {

    static class Course {

        private int hours;

        Course(int hours) {
            this.hours = hours;
        }

        void completeHours(int completed) {
            hours -= completed;
        }

        int getHours() {
            return hours;
        }
    }

    @Test
    void completeHoursShouldReduceRemainingHours() {

         
        Course course = new Course(100);
 
        course.completeHours(20);

  
        assertEquals(80, course.getHours());
    }
}