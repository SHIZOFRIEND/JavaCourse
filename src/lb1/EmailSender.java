package lb1;

public class EmailSender extends NotificationSender{
@Override
public void send(String message){
    System.out.println("электронная почта:" + message);
}
}