package lb1n;

public class Task12 {

    sealed interface EnrollmentResult
            permits Accepted, Rejected, WaitListed, PaymentPending {
    }

    record Accepted(String courseId)
            implements EnrollmentResult {
    }

    record Rejected(String reason)
            implements EnrollmentResult {
    }

    record WaitListed(String courseId, int position)
            implements EnrollmentResult {
    }

    record PaymentPending(String courseId, int amount)
            implements EnrollmentResult {
    }

    static String describe(EnrollmentResult result) {
        return switch (result) {
            case Accepted accepted -> "Запись подтверждена на курс " + accepted.courseId();
            case Rejected rejected -> "В записи отказано: " + rejected.reason();
            case WaitListed waitListed -> "Курс " + waitListed.courseId() + ": место в листе ожидания № " + waitListed.position();
            case PaymentPending pending -> "Ожидается оплата курса " + pending.courseId() + ": " + pending.amount() + " руб.";
        };
    }
    public static void main(String[] args) {
        EnrollmentResult[] results = {
                new Accepted("JAVA-101"),
                new Rejected(
                        "Не выполнены предварительные требования"),
                new WaitListed("JAVA-201", 3),
                new PaymentPending("JAVA-301", 2500)
        };

        for (EnrollmentResult result : results) {
            System.out.println(describe(result));
        }
    }
}