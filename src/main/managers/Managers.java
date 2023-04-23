package main.managers;

import main.managers.manager_history.HistoryManager;
import main.managers.manager_history.InMemoryHistoryManager;
import main.managers.manager_tasks.InMemoryTasksManager;
import main.managers.manager_tasks.TaskManager;

public class Managers {
    public static TaskManager getDefault() {
        return new InMemoryTasksManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

}
