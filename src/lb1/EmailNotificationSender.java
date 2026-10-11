package lb1;

public class EmailNotificationSender extends AbstractNotificationSender {

    @Override
    protected void doSend(String recipient, String message) {
        System.out.println("письмо отправлено: " + recipient);
        System.out.println("сообщение: " + message);
    }
}