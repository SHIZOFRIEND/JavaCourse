package lb1n;

public class Task6 {

    static class NotificationSender {

        public void send(String message) {
            System.out.println("NotificationSender: " + message);
        }

        protected void writeLog(String message) {
            System.out.println("Лог базового класса: " + message);
        }

        public Number getStatusCode() {
            return 100;
        }
    }

    static class EmailSender extends NotificationSender {

        @Override
        public void send(String message) {
            System.out.println("EmailSender: отправлено письмо: " + message);
        }

        @Override
        public void writeLog(String message) {
            System.out.println("EmailSender: запись в лог: " + message);
        }

        @Override
        public Integer getStatusCode() {
            return 200;
        }
    }

    static class ConsoleSender extends NotificationSender {

        @Override
        public void send(String message) {
            System.out.println("ConsoleSender: " + message);
        }

        @Override
        protected void writeLog(String message) {
            System.out.println("ConsoleSender: " + message);
        }

        @Override
        public Number getStatusCode() {
            return 300;
        }
    }

    static class AccidentalEmailSender extends NotificationSender {

        public void send(Object message) {
            System.out.println("AccidentalEmailSender: " + message);
        }
    }

    public static void main(String[] args) {

        System.out.println("Правильное переопределение");

        NotificationSender sender = new EmailSender();
        sender.send("Вы записаны на курс Java");

        System.out.println("Код ответа: " + sender.getStatusCode());

        System.out.println();

        NotificationSender console = new ConsoleSender();
        console.send("Начинается занятие");

        System.out.println();

        System.out.println("Уровень доступа");

        EmailSender email = new EmailSender();
        email.writeLog("Отправка завершена");

        System.out.println();

        System.out.println("Случайная перегрузка");

        NotificationSender accidental =
                new AccidentalEmailSender();

        accidental.send("Привет");

        System.out.println();

        AccidentalEmailSender concrete = new AccidentalEmailSender();

        concrete.send("Привет");
        concrete.send((Object) "Привет");
    }
}