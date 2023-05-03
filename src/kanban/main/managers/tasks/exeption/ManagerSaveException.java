package main.managers.tasks.exeption;

public class ManagerSaveException extends RuntimeException {

    public ManagerSaveException(String message, Throwable cause) {
        super(message, cause);
    }

}
