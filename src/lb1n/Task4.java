package lb1n;

public class Task4 {

    static abstract class AbstractNotificationSender {

        public final void send(String recipient, String message) {
            if (recipient == null || recipient.trim().isEmpty()) {
                throw new IllegalArgumentException(
                        "Адресат не должен быть пустым");
            }

            if (message == null || message.trim().isEmpty()) {
                throw new IllegalArgumentException(
                        "Сообщение не должно быть пустым");
            }

            doSend(recipient, message);
        }

        protected abstract void doSend(
                String recipient,
                String message);
    }

    static class EmailSender extends AbstractNotificationSender {

        @Override
        protected void doSend(String recipient, String message) {
            System.out.println("Отправка email на адрес: " + recipient);
            System.out.println("Текст письма: " + message);
        }
    }

    static class ConsoleSender extends AbstractNotificationSender {

        @Override
        protected void doSend(String recipient, String message) {
            System.out.println(
                    "Уведомление для " + recipient + ": " + message);
        }
    }

    public static void main(String[] args) {
        AbstractNotificationSender emailSender = new EmailSender();
        AbstractNotificationSender consoleSender = new ConsoleSender();

        emailSender.send(
                "student@example.com",
                "Вы записаны на курс Java");

        System.out.println();

        consoleSender.send(
                "student",
                "Начинается занятие по Java");

        System.out.println();

        try {
            emailSender.send("", "Проверка адресата");
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }
}