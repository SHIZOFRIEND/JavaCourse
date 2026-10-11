
package lb1;

public class PrerequisitesEnrollmentPolicy implements EnrollmentPolicy {

    private final int requiredHours;

    public PrerequisitesEnrollmentPolicy(int requiredHours) {
        this.requiredHours = requiredHours;
    }

    @Override
    public boolean canEnroll(Student student, Course course) {
        return course.completedHours() >= requiredHours;
    }
}

