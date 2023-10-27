package cc.ddrpa.towel.exception;

public class MisconfigurationException extends RuntimeException {
    public MisconfigurationException(String message) {
        super(message);
    }
}