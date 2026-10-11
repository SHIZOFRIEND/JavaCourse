package lb1;

public class ConsoleNotificationSender extends AbstractNotificationSender {

    @Override
    protected void doSend(String recipient, String message) {
        System.out.println("Уведомления");
        System.out.println("ОТ: " + recipient);
        System.out.println("сообщкние: " + message);
    }
}