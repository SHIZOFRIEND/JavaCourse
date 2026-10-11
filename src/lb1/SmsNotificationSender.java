package lb1;

public class SmsNotificationSender implements Notification {

    @Override
    public void send(String recipient, String message) {
        System.out.println(
                "СМС " + recipient + ": " + message
        );
    }
}