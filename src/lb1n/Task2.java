package lb1n;

public class Task2 {

    static class NotificationSender {

        public void send(String message) {
            System.out.println(
                    "NotificationSender.send(String): " + message);
        }

        public void send(Object message) {
            System.out.println(
                    "NotificationSender.send(Object): " + message);
        }
    }

    static class EmailSender extends NotificationSender {

        @Override
        public void send(String message) {
            System.out.println(
                    "EmailSender.send(String): " + message);
        }

        @Override
        public void send(Object message) {
            System.out.println(
                    "EmailSender.send(Object): " + message);
        }

        public void send(Integer code) {
            System.out.println(
                    "EmailSender.send(Integer): " + code);
        }
    }

    static class ConsoleSender extends NotificationSender {

        @Override
        public void send(String message) {
            System.out.println(
                    "ConsoleSender.send(String): " + message);
        }

        @Override
        public void send(Object message) {
            System.out.println(
                    "ConsoleSender.send(Object): " + message);
        }
    }

    public static void main(String[] args) {

        NotificationSender sender = new EmailSender();

        sender.send("Привет");
        sender.send((Object) "Привет");
        sender.send(Integer.valueOf(42));

        System.out.println();

        NotificationSender console = new ConsoleSender();
        console.send("Новое уведомление");

        System.out.println();

        EmailSender email = new EmailSender();
        NotificationSender baseReference = email;

        email.send(Integer.valueOf(7));
        baseReference.send(Integer.valueOf(7));
    }
}