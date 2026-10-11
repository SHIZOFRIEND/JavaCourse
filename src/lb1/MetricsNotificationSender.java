
package lb1;

public class MetricsNotificationSender implements Notification {

    private final Notification sender;

    public MetricsNotificationSender(Notification sender) {
        this.sender = sender;
    }

    @Override
    public void send(String recipient, String message) {

        long start = System.currentTimeMillis();

        sender.send(recipient, message);

        long end = System.currentTimeMillis();

        System.out.println("METRICS: " + (end - start) + " ms");
    }
}

