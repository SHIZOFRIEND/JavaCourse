package lb1n;

import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.function.Supplier;

public class Task13 {

    interface NotificationSender {
        void send(String recipient, String message);
    }

    static class ConsoleSender implements NotificationSender {
        @Override
        public void send(String recipient, String message) {
            System.out.println("[CONSOLE] Для " + recipient + ": " + message);
        }
    }

    static class EmailSender implements NotificationSender {
        private final String smtpServer;
        public EmailSender(String smtpServer) {
            if (smtpServer == null || smtpServer.trim().isEmpty()) {
                throw new IllegalArgumentException("SMTP-сервер не задан");
            }
            this.smtpServer = smtpServer;
        }
        @Override
        public void send(String recipient, String message) {
            System.out.println(
                    "[EMAIL через " + smtpServer + "] На " + recipient + ": " + message);
        }
    }

    static class SmsSender implements NotificationSender {
        private final String gateway;

        public SmsSender(String gateway) {
            if (gateway == null || gateway.trim().isEmpty()) {
                throw new IllegalArgumentException("SMS-шлюз не задан");
            }
            this.gateway = gateway;
        }

        @Override
        public void send(String recipient, String message) {
            System.out.println("[SMS через " + gateway + "] На номер " + recipient + ": " + message);
        }
    }

    static class NotificationSenderFactory {
        private final Map<String, Supplier<NotificationSender>> creators =
                new LinkedHashMap<>();
        public void register(
                String type,
                Supplier<NotificationSender> creator) {
            if (type == null || type.trim().isEmpty()) {
                throw new IllegalArgumentException(
                        "Тип уведомления не задан");
            }
            if (creator == null) {
                throw new IllegalArgumentException(
                        "Создатель не задан");
            }
            String key = normalize(type);
            if (creators.containsKey(key)) {
                throw new IllegalArgumentException(
                        "Тип уже зарегистрирован: " + type);
            }
            creators.put(key, creator);
        }

        public NotificationSender create(String type) {
            if (type == null) {
                throw new IllegalArgumentException("Тип уведомления не задан");
            }
            Supplier<NotificationSender> creator = creators.get(normalize(type));
            if (creator == null) {
                throw new IllegalArgumentException(
                        "Неизвестный тип уведомления: " + type);
            }
            return creator.get();
        }

        private static String normalize(String type) {
            return type.trim().toLowerCase(Locale.ROOT);
        }
    }

    static class NotificationService {
        private final NotificationSender sender;
        public NotificationService(NotificationSender sender) {
            if (sender == null) {
                throw new IllegalArgumentException(
                        "Отправитель не задан");
            }
            this.sender = sender;
        }
        public void notifyUser(String recipient, String message) {
            sender.send(recipient, message);
        }
    }
    public static void main(String[] args) {
        NotificationSenderFactory factory = new NotificationSenderFactory();
        factory.register("console", ConsoleSender::new);

        factory.register("email", () -> new EmailSender("smtp.example.com"));
        factory.register("sms", () -> new SmsSender("gateway.example.com"));
        factory.register("email-backup", () -> new EmailSender("smtp-backup.example.com"));
        send(factory, "console", "student-101", "Вы записаны на курс Java");
        send(factory, "email", "student@example.com", "Начало курса в понедельник");
        send(factory, "sms", "+46000000000", "Напоминание о занятии");
        send(factory, "email-backup", "student@example.com", "Повторное уведомление");
    }

    private static void send(NotificationSenderFactory factory, String type, String recipient, String message) {
        NotificationSender sender = factory.create(type);
        NotificationService service = new NotificationService(sender);
        service.notifyUser(recipient, message);
    }
}