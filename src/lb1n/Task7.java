package lb1n;

public class Task7 {

    interface NotificationSender {
        void send(String recipient, String message);
    }

    static class EmailSender implements NotificationSender {
        private int failuresRemaining;
        private int attempts;

        public EmailSender(int failuresBeforeSuccess) {
            this.failuresRemaining = failuresBeforeSuccess;
        }

        @Override
        public void send(String recipient, String message) {
            attempts++;

            System.out.println("[EMAIL] Попытка " + attempts + " для " + recipient);

            if (failuresRemaining > 0) {
                failuresRemaining--;
                throw new RuntimeException("Временная ошибка SMTP");
            }
            System.out.println("[EMAIL] Сообщение отправлено: " + message);
        }
    }

    static abstract class SenderDecorator implements NotificationSender {
        protected final NotificationSender delegate;
        public SenderDecorator(NotificationSender delegate) {
            if (delegate == null) {
                throw new IllegalArgumentException("Отправитель не должен быть null");
            }
            this.delegate = delegate;
        }
    }

    static class LoggingDecorator extends SenderDecorator {
        public LoggingDecorator(NotificationSender delegate) {
            super(delegate);
        }
        @Override
        public void send(String recipient, String message) {
            System.out.println("[LOG] Начало отправки для " + recipient);
            try {
                delegate.send(recipient, message);
                System.out.println("[LOG] Отправка завершена успешно");
            } catch (RuntimeException exception) {
                System.out.println(
                        "[LOG] Ошибка: " + exception.getMessage());
                throw exception;
            }
        }
    }

    static class MetricsDecorator extends SenderDecorator {

        private int calls;
        private int successes;
        private int failures;
        private long totalNanos;

        public MetricsDecorator(NotificationSender delegate) {
            super(delegate);
        }
        @Override
        public void send(String recipient, String message) {
            calls++;
            long start = System.nanoTime();
            try {
                delegate.send(recipient, message);
                successes++;
            } catch (RuntimeException exception) {
                failures++;
                throw exception;
            } finally {
                totalNanos += System.nanoTime() - start;
            }
        }

        public void printMetrics() {
            System.out.println("Вызовы отправителя: " + calls);
            System.out.println("Успешные вызовы: " + successes);
            System.out.println("Неудачные вызовы: " + failures);
            System.out.println("Суммарное время: " + totalNanos + " нс");
        }
    }

    static class RetryDecorator extends SenderDecorator {

        private final int maxAttempts;

        public RetryDecorator(NotificationSender delegate, int maxAttempts) {
            super(delegate);
            if (maxAttempts < 1) {
                throw new IllegalArgumentException("Число попыток должно быть не меньше 1");
            }
            this.maxAttempts = maxAttempts;
        }

        @Override
        public void send(String recipient, String message) {
            for (int attempt = 1; attempt <= maxAttempts; attempt++) {
                try {
                    delegate.send(recipient, message);
                    return;
                } catch (RuntimeException exception) {
                    System.out.println("[RETRY] Попытка " + attempt + " из " + maxAttempts + " не удалась");
                    if (attempt == maxAttempts) {
                        throw exception;
                    }
                }
            }
        }
    }

    public static void main(String[] args) {

        System.out.println("Вариант 1: Logging(Metrics(Retry(Email)))");
        EmailSender email1 = new EmailSender(2);
        RetryDecorator retry1 = new RetryDecorator(email1, 3);
        MetricsDecorator metrics1 = new MetricsDecorator(retry1);
        NotificationSender sender1 = new LoggingDecorator(metrics1);
        try {
            sender1.send("student@example.com", "Вы записаны на курс Java");
        } catch (RuntimeException exception) {
            System.out.println("Отправка окончательно не удалась.");
        }
        System.out.println("\nМетрики первого варианта:");
        metrics1.printMetrics();
        System.out.println("Вариант 2: Retry(Metrics(Logging(Email)))");
        EmailSender email2 = new EmailSender(2);
        LoggingDecorator logging2 = new LoggingDecorator(email2);
        MetricsDecorator metrics2 = new MetricsDecorator(logging2);
        NotificationSender sender2 = new RetryDecorator(metrics2, 3);
        try {
            sender2.send("student@example.com", "Вы записаны на курс Java");
        } catch (RuntimeException exception) {
            System.out.println("Отправка окончательно не удалась.");
        }
        System.out.println("\nМетрики второго варианта:");
        metrics2.printMetrics();
    }
}