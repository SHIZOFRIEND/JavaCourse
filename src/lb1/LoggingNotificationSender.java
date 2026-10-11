
package lb1;

public class LoggingNotificationSender implements Notification {

    private final Notification sender;

    public LoggingNotificationSender(Notification sender) {
        this.sender = sender;
    }

    @Override
    public void send(String recipient, String message) {
        System.out.println("LOG: отправка началась");

        sender.send(recipient, message);

        System.out.println("LOG: отправка завершена");
    }
}

