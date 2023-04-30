package main.managers.Exeption;

public class TaskNotFound extends IndexOutOfBoundsException{
    public TaskNotFound(String message){
        super(message);
    }
}
