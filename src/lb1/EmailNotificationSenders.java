package lb1;

public class EmailNotificationSenders implements Notification {

    @Override
    public void send(String recipient, String message) {
        System.out.println(
                "письмо от " + recipient + ": " + message
        );
    }
}