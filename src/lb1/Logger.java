package lb1;

public interface Logger {

    default void log() {
        System.out.println("Logger");
    }
}