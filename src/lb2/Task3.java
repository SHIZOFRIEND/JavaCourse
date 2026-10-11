package lb2;
public class Task3 {

    static class EnrollmentRejectedException extends Exception {

        int studentId;
        int courseId;
        String reasonCode;

        EnrollmentRejectedException(int studentId,int courseId,String reasonCode,Throwable cause) {
            super("Enrollment rejected: " + reasonCode, cause);

            this.studentId = studentId;
            this.courseId = courseId;
            this.reasonCode = reasonCode;
        }

        @Override
        public String toString() {
            return "EnrollmentRejectedException: "+ reasonCode;
        }
    }

    static void enroll() throws EnrollmentRejectedException {

        try {
            throw new Exception("Курс полон");
        } catch (Exception e) {

            throw new EnrollmentRejectedException(1,10,"КУРС ПОЛОН",e
            );
        }
    }

    public static void main(String[] args) {

        try {
            enroll();
        } catch (EnrollmentRejectedException e) {

            System.out.println(e.getMessage());
            System.out.println(e);
            System.out.println("Исходная причина: "
                    + e.getCause().getMessage());
        }
    }
}