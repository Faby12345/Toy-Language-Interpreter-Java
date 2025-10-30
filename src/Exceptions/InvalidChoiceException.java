package Exceptions;

public class InvalidChoiceException extends RuntimeException {
    public InvalidChoiceException(String message) {
        super(message);
    }
    public InvalidChoiceException() {
        super("Menu choice is not valid!");
    }
}
