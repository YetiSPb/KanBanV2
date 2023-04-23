package main.managers.manager_history;

import main.tasks.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager{

    protected final ArrayList<Task> historyView = new ArrayList<>(10);

    @Override
    public void add(Task task) {
        if (historyView.size() == 10) {
            historyView.remove(0);
        }
        historyView.add(task);
    }

    @Override
    public List<Task> getHistory() {
        return historyView;
    }
}
