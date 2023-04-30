package main.managers.manager_tasks;

import main.tasks.Epic;
import main.tasks.Subtask;
import main.tasks.Task;

import java.util.List;

public interface TaskManager {

    Task addTask(Task task);

    Epic addEpic(Epic epic);

    Subtask addSubtask(Subtask subtask);

    boolean deleteTask(int id);

    boolean deleteEpic(int id);

    boolean deleteSubtask(int id);

    void deleteAllTasks();

    void deleteAllEpics();

    void deleteAllSubtasks();

    void deleteAllSubtasksByEpic(Epic epic);

    Task getTask(int id);

    Epic getEpic(int id);

    Subtask getSubtask(int id);

    List<Task> getAllTasks();

    List<Epic> getAllEpics();

    List<Subtask> getAllSubtasks();

    List<Subtask> getAllSubtasksByEpic(int id);

    List<Task> getHistory();

    Task updateTask(Task task);

    Epic updateEpic(Epic epic);

    Subtask updateSubtask(Subtask subtask);

    List<Task> getPrioritizedTasks();

}