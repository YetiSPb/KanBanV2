package main.managers.tasks;

import main.managers.TaskManager;
import main.tasks.Epic;
import main.tasks.Subtask;
import main.tasks.Task;
import main.tasks.status.Status;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

abstract class TaskManagerTest<T extends TaskManager> {

    protected T manager;

    @Test
    void addTask() {
        Task task = new Task("Задача 1", "Описание задачи 1", Instant.EPOCH, 0);
        manager.addTask(task);

        assertEquals(task, manager.getTaskById(task.getId()));
    }

    @Test
    void addEpic() {
        Epic epic = new Epic("Эпик 1", "Описание эпика 1");
        manager.addEpic(epic);

        assertEquals(epic, manager.getEpicById(epic.getId()));
    }

    @Test
    void addSubtask() {
        Epic epic = new Epic("Эпик 1", "Описание эпика 1");
        manager.addEpic(epic);

        Subtask subtask = new
                Subtask("Подзадача 1", "Подзадача эпика 1", Instant.EPOCH, 0, epic.getId());
        manager.addSubtask(subtask);

        assertEquals(subtask, manager.getSubtaskById(subtask.getId()));
    }

    @Test
    void deleteTask() {
        Task task = new Task("Задача 1", "Описание задачи 1", Instant.EPOCH, 0);
        manager.addTask(task);

        assertTrue(manager.deleteTaskById(task.getId()));
        assertNull(manager.getTaskById(task.getId()));
    }

    @Test
    void deleteEpic() {
        Epic epic = new Epic("Эпик 1", "Описание эпика 1");
        manager.addEpic(epic);

        assertTrue(manager.deleteEpicById(epic.getId()));
        assertNull(manager.getEpicById(epic.getId()));
    }

    @Test
    void deleteSubtask() {
        Epic epic = new Epic("Эпик 1", "Описание эпика 1");
        manager.addEpic(epic);

        Subtask subtask = new
                Subtask("Подзадача 1", "Подзадача эпика 1", Instant.EPOCH, 0, epic.getId());
        manager.addSubtask(subtask);

        assertTrue(manager.deleteSubtaskById(subtask.getId()));
        assertNull(manager.getSubtaskById(subtask.getId()));
    }

    @Test
    void deleteAllTasks() {
        Task task1 = new Task("Задача 1", "Описание задачи 1",
                Instant.EPOCH.plusSeconds(600 * 6 + 1), 600);
        manager.addTask(task1);

        Epic epic1 = new Epic("Эпик 1", "Описание эпика 1");
        Epic epic2 = new Epic("Эпик 2", "Описание эпика 2");

        manager.addEpic(epic1);
        manager.addEpic(epic2);

        Subtask subtask11 = new Subtask("Подзадача 11", "Описание подзадачи 11",
                Instant.EPOCH.plusSeconds(600 * 4 + 1), 600, epic1.getId());
        manager.addSubtask(subtask11);

        Subtask subtask12 = new Subtask("Подзадача 12", "Описание подзадачи 12",
                Instant.EPOCH.plusSeconds(600 * 2 + 1), 600, epic1.getId());
        manager.addSubtask(subtask12);

        Subtask subtask21 = new Subtask("Подзадача 21", "Описание подзадачи 21",
                Instant.EPOCH.plusSeconds(600), 600, epic2.getId());
        manager.addSubtask(subtask21);

        Subtask subtask22 = new Subtask("Подзадача 22", "Описание подзадачи 22",
                Instant.EPOCH, 600, epic2.getId());
        manager.addSubtask(subtask22);

        manager.deleteAllTasks();

        assertEquals(0, manager.getAllTasks().size());

        assertEquals(2,manager.getAllEpics().size());
        assertEquals(4, manager.getAllSubtasks().size());
    }

    @Test
    void deleteAllEpics() {
        Task task1 = new Task("Задача 1", "Описание задачи 1",
                Instant.EPOCH.plusSeconds(600 * 6 + 1), 600);
        manager.addTask(task1);

        Epic epic1 = new Epic("Эпик 1", "Описание эпика 1");
        Epic epic2 = new Epic("Эпик 2", "Описание эпика 2");

        manager.addEpic(epic1);
        manager.addEpic(epic2);

        Subtask subtask11 = new Subtask("Подзадача 11", "Описание подзадачи 11",
                Instant.EPOCH.plusSeconds(600 * 4 + 1), 600, epic1.getId());
        manager.addSubtask(subtask11);

        Subtask subtask12 = new Subtask("Подзадача 12", "Описание подзадачи 12",
                Instant.EPOCH.plusSeconds(600 * 2 + 1), 600, epic1.getId());
        manager.addSubtask(subtask12);

        Subtask subtask21 = new Subtask("Подзадача 21", "Описание подзадачи 21",
                Instant.EPOCH.plusSeconds(600), 600, epic2.getId());
        manager.addSubtask(subtask21);

        Subtask subtask22 = new Subtask("Подзадача 22", "Описание подзадачи 22",
                Instant.EPOCH, 600, epic2.getId());
        manager.addSubtask(subtask22);

        manager.deleteAllEpics();

        assertEquals(0,manager.getAllEpics().size());

        assertEquals(1, manager.getAllTasks().size());
        assertEquals(0, manager.getAllSubtasks().size());

    }

    @Test
    void deleteAllSubtasks() {

        Task task = new Task("Задача 1", "Описание задачи 1", Instant.EPOCH, 0);
        manager.addTask(task);

        Epic epic = new Epic("Эпик 1", "Описание эпика 1");
        manager.addEpic(epic);
        Epic epic2 = new Epic("Эпик 2", "Описание эпика 2");
        manager.addEpic(epic2);

        Subtask subtask = new Subtask("Подзадача 1", "Подзадача эпика 1",
                Instant.EPOCH.plusSeconds(600), 0, epic.getId());
        manager.addSubtask(subtask);

        manager.deleteAllEpics();
        assertEquals(0, manager.getAllEpics().size());
        assertEquals(1, manager.getAllTasks().size());
        assertEquals(0, manager.getAllSubtasks().size());

        manager.deleteAllSubtasks();
        assertEquals(0, manager.getAllSubtasks().size());
    }

    @Test
    void deleteAllSubtasksByEpic() {
        Task task1 = new Task("Задача 1", "Описание задачи 1",
                Instant.EPOCH.plusSeconds(600 * 6 + 1), 600);
        manager.addTask(task1);

        Epic epic1 = new Epic("Эпик 1", "Описание эпика 1");
        Epic epic2 = new Epic("Эпик 2", "Описание эпика 2");

        manager.addEpic(epic1);
        manager.addEpic(epic2);

        Subtask subtask11 = new Subtask("Подзадача 11", "Описание подзадачи 11",
                Instant.EPOCH.plusSeconds(600 * 4 + 1), 600, epic1.getId());

        Subtask subtask12 = new Subtask("Подзадача 12", "Описание подзадачи 12",
                Instant.EPOCH.plusSeconds(600 * 2 + 1), 600, epic1.getId());

        Subtask subtask21 = new Subtask("Подзадача 21", "Описание подзадачи 21",
                Instant.EPOCH.plusSeconds(600), 600, epic2.getId());

        Subtask subtask22 = new Subtask("Подзадача 22", "Описание подзадачи 22",
                Instant.EPOCH, 600, epic2.getId());

        manager.addSubtask(subtask11);
        manager.addSubtask(subtask12);
        manager.addSubtask(subtask21);
        manager.addSubtask(subtask22);

        manager.deleteAllSubtasksByEpic(epic2);

        assertNull(manager.getSubtaskById(subtask21.getId()));
        assertNull(manager.getSubtaskById(subtask22.getId()));

        assertEquals(1, manager.getAllTasks().size());
        assertEquals(2, manager.getAllSubtasks().size());
        assertEquals(2, manager.getAllEpics().size());
    }

    @Test
    void getTaskById() {
        Task task = new Task("Задача 1", "Описание задачи 1", Instant.EPOCH, 0);
        manager.addTask(task);

        assertEquals(manager.getTaskById(task.getId()), task);
    }

    @Test
    void getEpicById() {
        Epic epic = new Epic("Эпик 1", "Описание эпика 1");
        manager.addEpic(epic);

        assertEquals(manager.getEpicById(epic.getId()), epic);
    }

    @Test
    void getSubtaskById() {
        Epic epic = new Epic("Эпик 1", "Описание эпика 1");
        manager.addEpic(epic);

        Subtask subtask = new
                Subtask("Подзадача 1", "Подзадача эпика 1", Instant.EPOCH, 0, epic.getId());
        manager.addSubtask(subtask);

        assertEquals(manager.getSubtaskById(subtask.getId()), subtask);
    }

    @Test
    void getAllTasks() {

        Task task1 = new Task("Задача 1", "Описание задачи 1",
                Instant.EPOCH.plusSeconds(600 * 6 + 1), 600);
        manager.addTask(task1);

        Task task2 = new Task("Задача 2", "Описание задачи 2",
                Instant.EPOCH.plusSeconds(600 * 5 + 1), 600);
        manager.addTask(task2);

        Epic epic1 = new Epic("Эпик 1", "Описание эпика 1");
        manager.addEpic(epic1);

        Epic epic2 = new Epic("Эпик 2", "Описание эпика 2");
        manager.addEpic(epic2);

        Subtask subtask11 = new Subtask("Подзадача 11", "Описание подзадачи 11",
                Instant.EPOCH.plusSeconds(600 * 4 + 1), 600, epic1.getId());
        manager.addSubtask(subtask11);

        Subtask subtask12 = new Subtask("Подзадача 12", "Описание подзадачи 12",
                Instant.EPOCH.plusSeconds(600 * 2 + 1), 600, epic1.getId());
        manager.addSubtask(subtask12);

        Subtask subtask21 = new Subtask("Подзадача 21", "Описание подзадачи 21",
                Instant.EPOCH.plusSeconds(600), 600, epic2.getId());
        manager.addSubtask(subtask21);

        Subtask subtask22 = new Subtask("Подзадача 22", "Описание подзадачи 22",
                Instant.EPOCH, 600, epic2.getId());
        manager.addSubtask(subtask22);

        assertEquals(2, manager.getAllTasks().size());
        assertEquals(2, manager.getAllEpics().size());
        assertEquals(4, manager.getAllSubtasks().size());
    }

    @Test
    void getAllEpics() {
        Task task = new Task("Задача 1", "Описание задачи 1", Instant.EPOCH, 0);
        manager.addTask(task);

        Epic epic = new Epic("Эпик 1", "Описание эпика 1");
        manager.addEpic(epic);
        Epic epic2 = new Epic("Эпик 2", "Описание эпика 2");
        manager.addEpic(epic2);

        Subtask subtask = new Subtask("Подзадача 1", "Подзадача эпика 1",
                Instant.EPOCH.plusSeconds(600), 0, epic.getId());
        manager.addSubtask(subtask);

        manager.deleteAllEpics();

        assertEquals(0, manager.getAllEpics().size());
        assertEquals(1, manager.getAllTasks().size());
        assertEquals(0, manager.getAllSubtasks().size());
    }

    @Test
    void getAllSubtasks() {
        Task task1 = new Task("Задача 1", "Описание задачи 1",
                Instant.EPOCH.plusSeconds(600 * 6 + 1), 600);
        manager.addTask(task1);

        Epic epic1 = new Epic("Эпик 1", "Описание эпика 1");
        Epic epic2 = new Epic("Эпик 2", "Описание эпика 2");

        manager.addEpic(epic1);
        manager.addEpic(epic2);

        Subtask subtask11 = new Subtask("Подзадача 11", "Описание подзадачи 11",
                Instant.EPOCH.plusSeconds(600 * 4 + 1), 600, epic1.getId());
        manager.addSubtask(subtask11);

        Subtask subtask12 = new Subtask("Подзадача 12", "Описание подзадачи 12",
                Instant.EPOCH.plusSeconds(600 * 2 + 1), 600, epic1.getId());
        manager.addSubtask(subtask12);

        Subtask subtask21 = new Subtask("Подзадача 21", "Описание подзадачи 21",
                Instant.EPOCH.plusSeconds(600), 600, epic2.getId());
        manager.addSubtask(subtask21);

        Subtask subtask22 = new Subtask("Подзадача 22", "Описание подзадачи 22",
                Instant.EPOCH, 600, epic2.getId());
        manager.addSubtask(subtask22);

        assertEquals(2,manager.getAllEpics().size());

        assertEquals(1, manager.getAllTasks().size());
        assertEquals(4, manager.getAllSubtasks().size());
    }

    @Test
    void getAllSubtasksByEpic() {
        Task task1 = new Task("Задача 1", "Описание задачи 1",
                Instant.EPOCH.plusSeconds(600 * 6 + 1), 600);
        manager.addTask(task1);

        Epic epic1 = new Epic("Эпик 1", "Описание эпика 1");
        Epic epic2 = new Epic("Эпик 2", "Описание эпика 2");

        manager.addEpic(epic1);
        manager.addEpic(epic2);

        Subtask subtask11 = new Subtask("Подзадача 11", "Описание подзадачи 11",
                Instant.EPOCH.plusSeconds(600 * 4 + 1), 600, epic1.getId());
        manager.addSubtask(subtask11);

        Subtask subtask12 = new Subtask("Подзадача 12", "Описание подзадачи 12",
                Instant.EPOCH.plusSeconds(600 * 2 + 1), 600, epic1.getId());
        manager.addSubtask(subtask12);

        Subtask subtask21 = new Subtask("Подзадача 21", "Описание подзадачи 21",
                Instant.EPOCH.plusSeconds(600), 600, epic2.getId());
        manager.addSubtask(subtask21);

        Subtask subtask22 = new Subtask("Подзадача 22", "Описание подзадачи 22",
                Instant.EPOCH, 600, epic2.getId());
        manager.addSubtask(subtask22);

        assertEquals(2,manager.getAllSubtasksByEpic(epic2.getId()).size());

        assertEquals(1, manager.getAllTasks().size());
        assertEquals(2, manager.getAllEpics().size());
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

        manager.getTaskById(task.getId());
        manager.getSubtaskById(subtask.getId());
        manager.getEpicById(epic.getId());

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

        assertEquals(manager.getTaskById(task.getId()), task2);
    }

    @Test
    void updateEpic() {
        Epic epic = new Epic("Эпик 1", "Описание эпика 1");
        manager.addTask(epic);

        Epic epic2 = new Epic(epic.getId(), "Эпик 10", "Описание эпика 10",
                Status.DONE);

        manager.updateTask(epic2);

        assertEquals(manager.getTaskById(epic.getId()), epic2);
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

        assertEquals(manager.getSubtaskById(subtask2.getId()), subtask2);
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