package Exceptions;

public class StackEmptyException extends RuntimeException {
    public StackEmptyException() {
        super("The stack is empty!");
    }

    public StackEmptyException(String message) {
        super(message);
    }
}
