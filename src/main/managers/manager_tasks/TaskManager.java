package main.managers.manager_tasks;

import main.tasks.Epic;
import main.tasks.Subtask;
import main.tasks.Task;

import java.io.IOException;
import java.util.List;

public interface TaskManager {

    Task addTask(Task task) throws IOException;

    Epic addEpic(Epic epic) throws IOException;

    Subtask addSubtask(Subtask subtask) throws IOException;

    void deleteTask(int id) throws IOException;

    void deleteEpic(int id) throws IOException;

    void deleteSubtask(int id) throws IOException;

    void deleteAllTasks() throws IOException;

    void deleteAllEpics() throws IOException;

    void deleteAllSubtasks() throws IOException;

    void deleteAllSubtasksByEpic(Epic epic) throws IOException;

    Task getTask(int id) throws IOException;

    Epic getEpic(int id) throws IOException;

    Subtask getSubtask(int id) throws IOException;

    List<Task> getAllTasks();

    List<Epic> getAllEpics();

    List<Subtask> getAllSubtasks();

    List<Subtask> getAllSubtasksByEpic(int id);

    List<Task> getHistory();

    //void updateTask(Task task);

    //void updateEpic(Epic epic);

    //void updateSubtask(Subtask subtask);

    //public List<Task> getPrioritizedTasks();

}