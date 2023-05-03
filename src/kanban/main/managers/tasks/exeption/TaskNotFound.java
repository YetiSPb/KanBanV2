package main.managers.tasks.exeption;

public class TaskNotFound extends IndexOutOfBoundsException{
    public TaskNotFound(String message){
        super(message);
    }
}
