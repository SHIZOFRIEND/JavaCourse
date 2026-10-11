
package lb1;

public class Child extends Parent {

    @Override
    public void send(String message) {
        System.out.println("Child: " + message);
    }

    @Override
    public Integer getNumber() {
        return 20;
    }

    public void send(Object message) {
        System.out.println("Child Object: " + message);
    }

    public static void main(String[] args) {

        Parent parent = new Child();

        parent.send("Hello");

        System.out.println(parent.getNumber());
    }
}
