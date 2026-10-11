package lb1;

public class Console implements Notification {

    @Override
    public void send(String recipient, String message) {
        System.out.println(
                "Консоль " + recipient + ": " + message
        );
    }
}