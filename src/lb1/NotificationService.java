package lb1;

public class NotificationService {

    private final Notification sender;

    public NotificationService(Notification sender) {
        this.sender = sender;
    }

    public void notify(String recipient, String message) {
        sender.send(recipient, message);
    }
}