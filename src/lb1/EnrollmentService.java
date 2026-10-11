
package lb1;

public class EnrollmentService {

    private final EnrollmentPolicy policy;

    public EnrollmentService(EnrollmentPolicy policy) {
        this.policy = policy;
    }

    public void enroll(Student student, Course course) {

        if (policy.canEnroll(student, course)) {
            System.out.println(
                    student.name() + " записан на " + course.title()
            );
        } else {
            System.out.println(
                    student.name() + " не может быть записан на " + course.title()
            );
        }
    }
}
