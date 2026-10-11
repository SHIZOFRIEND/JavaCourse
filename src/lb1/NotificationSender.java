package lb1;
public class NotificationSender{

    public void send(String message){
        System.out.println("Not string" + message);
    }

    public void send(Object message){
        System.out.println("Not obj" + message);

    }

}