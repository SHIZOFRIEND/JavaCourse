package lb1;

public abstract class AbstractNotificationSender {

    public final void send(String recipient, String message) {
        if (recipient == null || recipient.isBlank()) {
            System.out.println("ERR: invalid recipient");
            return;
        }

        if (message == null || message.isBlank()) {
            System.out.println("ERR: invalid message");
            return;
        }

        doSend(recipient, message);
    }

    protected abstract void doSend(String recipient, String message);
}