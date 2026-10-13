package lb1n;

import java.util.Arrays;
import java.util.List;

public class Task14 {

    interface CourseFormat {
        String getDescription();
    }

    interface PaymentPolicy {
        String getDescription();
    }

    static class OnlineFormat implements CourseFormat {
        public String getDescription() {
            return "Онлайн";
        }
    }

    static class ClassroomFormat implements CourseFormat {
        public String getDescription() {
            return "В аудитории";
        }
    }

    static class HybridFormat implements CourseFormat {
        public String getDescription() {
            return "Гибридный";
        }
    }

    static class FreePayment implements PaymentPolicy {
        public String getDescription() {
            return "Бесплатно";
        }
    }

    static class FixedPayment implements PaymentPolicy {
        private int price;

        public FixedPayment(int price) {
            this.price = price;
        }

        public String getDescription() {
            return "Фиксированная оплата: " + price + " руб.";
        }
    }

    static class SubscriptionPayment implements PaymentPolicy {
        private int monthlyPrice;

        public SubscriptionPayment(int monthlyPrice) {
            this.monthlyPrice = monthlyPrice;
        }

        public String getDescription() {
            return "Подписка: " + monthlyPrice + " руб. в месяц";
        }
    }

    static class Course {
        private String name;
        private CourseFormat format;
        private PaymentPolicy payment;

        public Course(
                String name,
                CourseFormat format,
                PaymentPolicy payment) {
            this.name = name;
            this.format = format;
            this.payment = payment;
        }

        public void printInfo() {
            System.out.println("Курс: " + name);
            System.out.println("Формат: " + format.getDescription());
            System.out.println("Оплата: " + payment.getDescription());
            System.out.println();
        }
    }

    static class RecordedFormat implements CourseFormat {
        public String getDescription() {
            return "Записанные уроки";
        }
    }

    static class InstallmentPayment implements PaymentPolicy {
        private int numberOfPayments;
        private int paymentAmount;

        public InstallmentPayment(int numberOfPayments, int paymentAmount) {
            this.numberOfPayments = numberOfPayments;
            this.paymentAmount = paymentAmount;
        }

        public String getDescription() {
            return "Рассрочка: " + numberOfPayments
                    + " платежа по " + paymentAmount + " руб.";
        }
    }

    public static void main(String[] args) {
        List<CourseFormat> formats = Arrays.asList(
                new OnlineFormat(),
                new ClassroomFormat(),
                new HybridFormat()
        );

        List<PaymentPolicy> payments = Arrays.asList(
                new FreePayment(),
                new FixedPayment(5000),
                new SubscriptionPayment(1200)
        );

        System.out.println("Все комбинации формата и оплаты:");

        for (CourseFormat format : formats) {
            for (PaymentPolicy payment : payments) {
                Course course = new Course("Java", format, payment);
                course.printInfo();
            }
        }

        System.out.println("Добавляем новый формат и новую оплату:");

        Course newCourse = new Course(
                "Продвинутый Java",
                new RecordedFormat(),
                new InstallmentPayment(3, 2000)
        );

        newCourse.printInfo();
    }
}