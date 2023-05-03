package main.managers.tasks.exeption;

public class TaskIsNull extends NullPointerException{
    public TaskIsNull(String message){
        super(message);
    }
}
