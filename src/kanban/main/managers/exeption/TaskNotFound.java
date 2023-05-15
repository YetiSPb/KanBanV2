package main.managers.exeption;

public class TaskNotFound extends IndexOutOfBoundsException{
    public TaskNotFound(String message){
        super(message);
    }
}
