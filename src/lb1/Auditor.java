package lb1;

public interface Auditor {

    default void log() {
        System.out.println("Auditor");
    }
}