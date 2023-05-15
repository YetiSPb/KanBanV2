package main.managers;

import main.managers.history.HistoryManager;
import main.managers.history.InMemoryHistoryManager;

public class Managers {
    public static TaskManager getDefault() {
        return new HttpTasksManager();
        //return new InMemoryTasksManager();
    }

    public static TaskManager getDefaultFileBackedTasksManager() {
        return new FileBackedTasksManager();
    }


    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }


}
