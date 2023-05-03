package main.managers.tasks;

import main.tasks.Epic;
import main.tasks.Subtask;
import main.tasks.Task;
import main.tasks.status.Status;
import main.tasks.status.TaskType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BooleanSupplier;

import static org.junit.jupiter.api.Assertions.*;

abstract class TaskManagerTest<T extends TaskManager> {

    protected T manager;

    protected Task newTask() {
        return new Task("Task1", "Task1", Instant.EPOCH, 0);
    }

    protected Epic newEpic() {
        return new Epic("Epic1", "Epic1");
    }

    protected Subtask newSubtask(Epic epic) {
        return new Subtask("Subtask1", "Subtask1", Instant.EPOCH, 0, epic.getId());
    }


    @Test
    void addTask() {
        Task task = new Task("Задача 1", "Описание задачи 1", Instant.EPOCH, 0);
        manager.addTask(task);

        assertEquals(task, manager.getTask(task.getId()));
    }

    @Test
    void addEpic() {
        Epic epic = new Epic("Эпик 1", "Описание эпика 1");
        manager.addEpic(epic);

        assertEquals(epic, manager.getEpic(epic.getId()));
    }

    @Test
    void addSubtask() {
        Epic epic = new Epic("Эпик 1", "Описание эпика 1");
        manager.addEpic(epic);

        Subtask subtask = new
                Subtask("Подзадача 1", "Подзадача эпика 1", Instant.EPOCH, 0, epic.getId());
        manager.addSubtask(subtask);

        assertEquals(subtask, manager.getSubtask(subtask.getId()));
    }

    @Test
    void deleteTask() {
        Task task = new Task("Задача 1", "Описание задачи 1", Instant.EPOCH, 0);
        manager.addTask(task);

        assertTrue(manager.deleteTask(task.getId()));
        assertNull(manager.getTask(task.getId()));
    }

    @Test
    void deleteEpic() {
        Epic epic = new Epic("Эпик 1", "Описание эпика 1");
        manager.addEpic(epic);

        assertTrue(manager.deleteEpic(epic.getId()));
        assertNull(manager.getEpic(epic.getId()));
    }

    @Test
    void deleteSubtask() {
        Epic epic = new Epic("Эпик 1", "Описание эпика 1");
        manager.addEpic(epic);

        Subtask subtask = new
                Subtask("Подзадача 1", "Подзадача эпика 1", Instant.EPOCH, 0, epic.getId());
        manager.addSubtask(subtask);

        assertTrue(manager.deleteSubtask(subtask.getId()));
        assertNull(manager.getSubtask(subtask.getId()));

    }

    @Test
    void deleteAllTasks() {
        assertTrue(false);
    }

    @Test
    void deleteAllEpics() {
        assertTrue(false);
    }

    @Test
    void deleteAllSubtasks() {
        assertTrue(false);
    }

    @Test
    void deleteAllSubtasksByEpic() {
        assertTrue(false);
    }

    @Test
    void getTask() {
        Task task = new Task("Задача 1", "Описание задачи 1", Instant.EPOCH, 0);
        manager.addTask(task);

        assertEquals(manager.getTask(task.getId()), task);
    }

    @Test
    void getEpic() {
        Epic epic = new Epic("Эпик 1", "Описание эпика 1");
        manager.addEpic(epic);

        assertEquals(manager.getEpic(epic.getId()), epic);
    }

    @Test
    void getSubtask() {
        Epic epic = new Epic("Эпик 1", "Описание эпика 1");
        manager.addEpic(epic);

        Subtask subtask = new
                Subtask("Подзадача 1", "Подзадача эпика 1", Instant.EPOCH, 0, epic.getId());
        manager.addSubtask(subtask);

        assertEquals(manager.getSubtask(subtask.getId()), subtask);
    }

    @Test
    void getAllTasks() {
        assertTrue(false);
    }

    @Test
    void getAllEpics() {
        assertTrue(false);
    }

    @Test
    void getAllSubtasks() {
        assertTrue(false);
    }

    @Test
    void getAllSubtasksByEpic() {
        assertTrue(false);
    }

    @Test
    void getHistory() {
        Task task = new Task("Задача 1", "Описание задачи 1", Instant.EPOCH, 0);
        manager.addTask(task);

        Epic epic = new Epic("Эпик 1", "Описание эпика 1");
        manager.addEpic(epic);

        Subtask subtask = new Subtask("Подзадача 1", "Подзадача эпика 1",
                Instant.EPOCH.plusSeconds(600), 0, epic.getId());
        manager.addSubtask(subtask);

        manager.getTask(task.getId());
        manager.getSubtask(subtask.getId());
        manager.getEpic(epic.getId());

        List<Task> tasksExpected = new ArrayList<>();

        tasksExpected.add(task);
        tasksExpected.add(subtask);
        tasksExpected.add(epic);

        assertEquals(manager.getHistory(), tasksExpected);

    }

    @Test
    void updateTask() {
        Task task = new Task("Задача 1", "Описание задачи 1", Instant.EPOCH, 0);
        manager.addTask(task);

        Task task2 = new Task(task.getId(), "Задача 10", "Описание задачи 10",
                Instant.EPOCH, 0, Status.DONE);

        manager.updateTask(task2);

        assertEquals(manager.getTask(task.getId()), task2);
    }

    @Test
    void updateEpic() {
        Epic epic = new Epic("Эпик 1", "Описание эпика 1");
        manager.addTask(epic);

        Epic epic2 = new Epic(epic.getId(), "Эпик 10", "Описание эпика 10",
                Status.DONE);

        manager.updateTask(epic2);

        assertEquals(manager.getTask(epic.getId()), epic2);
    }

    @Test
    void updateSubtask() {
        Epic epic = new Epic("Эпик 1", "Описание эпика 1");
        manager.addEpic(epic);

        Subtask subtask = new Subtask("Подзадача 1", "Подзадача эпика 1",
                Instant.EPOCH.plusSeconds(600), 0, epic.getId());
        manager.addSubtask(subtask);

        Subtask subtask2 = new Subtask(subtask.getId(), "Подзадача 10", "Подзадача эпика 10",
                Instant.EPOCH.plusSeconds(600), 0, Status.DONE, subtask.getEpicID());

        manager.updateSubtask(subtask2);

        assertEquals(manager.getSubtask(subtask2.getId()), subtask2);
    }

    @Test
    void getPrioritizedTasks() {
        Task task = new Task("Задача 1", "Описание задачи 1", Instant.EPOCH.plusSeconds(600 * 3 + 2), 600);
        manager.addTask(task);

        Epic epic = new Epic("Эпик 1", "Описание эпика 1");
        manager.addEpic(epic);

        Subtask subtask = new Subtask("Подзадача 1", "Подзадача эпика 1",
                Instant.EPOCH, 600, epic.getId());
        manager.addSubtask(subtask);

        Subtask subtask2 = new Subtask("Подзадача 1", "Подзадача эпика 1",
                Instant.EPOCH.plusSeconds(600 * 2 + 1), 600, epic.getId());
        manager.addSubtask(subtask2);

        List<Task> tasksActual = new ArrayList<>();

        tasksActual.add(subtask);
        tasksActual.add(subtask2);
        tasksActual.add(task);

        assertEquals(manager.getPrioritizedTasks(), tasksActual);

    }
}