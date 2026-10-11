package lb1;
public class ConsoleSender extends NotificationSender{
  @Override
    public void send(String message) {
        System.out.println("ConsoleSender: " + message);
    }
}