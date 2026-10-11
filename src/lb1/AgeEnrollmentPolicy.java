
package lb1;

public class AgeEnrollmentPolicy implements EnrollmentPolicy {

    private final int minimumAge;

    public AgeEnrollmentPolicy(int minimumAge) {
        this.minimumAge = minimumAge;
    }

    @Override
    public boolean canEnroll(Student student, Course course) {
        return student.age() >= minimumAge;
    }
}

