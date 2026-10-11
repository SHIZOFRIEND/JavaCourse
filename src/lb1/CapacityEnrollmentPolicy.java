
package lb1;

public class CapacityEnrollmentPolicy implements EnrollmentPolicy {

    private final int currentStudents;
    private final int capacity;

    public CapacityEnrollmentPolicy(int currentStudents, int capacity) {
        this.currentStudents = currentStudents;
        this.capacity = capacity;
    }

    @Override
    public boolean canEnroll(Student student, Course course) {
        return currentStudents < capacity;
    }
}

