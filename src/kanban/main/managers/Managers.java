package main.managers;

import main.managers.history.HistoryManager;
import main.managers.history.InMemoryHistoryManager;
import main.managers.tasks.FileBackedTasksManager;
import main.managers.tasks.InMemoryTasksManager;
import main.managers.tasks.TaskManager;

public class Managers {
    public static TaskManager getDefault() {
        return new InMemoryTasksManager();
    }

    public static TaskManager getDefaultFileBackedTasksManager() {
        return new FileBackedTasksManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }



}
