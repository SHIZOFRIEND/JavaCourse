
package lb1;

public class Email implements Notification {

    @Override
    public void send(String recipient, String message) {
        System.out.println("отправлено от: " + recipient);
    }
}

