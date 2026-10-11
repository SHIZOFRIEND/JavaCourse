package lb1;

public class User implements Logger, Auditor {

    @Override
    public void log() {
        Logger.super.log();
    }
}