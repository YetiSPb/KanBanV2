package main.managers.manager_history;

import main.tasks.Task;

import java.util.List;

public interface HistoryManager {

    void add(Task task);
    void remove(int id);
    List<Task> getHistory();

}
