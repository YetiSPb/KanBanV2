package main.managers.Exeption;

public class TaskAlreadyExistsException extends RuntimeException {

    public TaskAlreadyExistsException(String message) {
        super(message);
    }
}
