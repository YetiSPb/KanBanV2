package main.managers.tasks;

import main.managers.history.InMemoryHistoryManager;
import main.managers.Managers;
import main.tasks.status.*;
import main.tasks.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class InMemoryHistoryManagerTest {

    private final List<Task> emptyList = new ArrayList<>();
    private InMemoryHistoryManager manager;

    @BeforeEach
    public void loadInitialConditions() {

        manager = (InMemoryHistoryManager) Managers.getDefaultHistory();

    }

    @Test
    public void addTasksToHistoryTest() {

        Task task1 = new Task(1, "Task1", "Task1",
                Instant.EPOCH, 600, Status.NEW);
        Task task2 = new Task(2, "Task2", "Task2",
                Instant.EPOCH.plusSeconds(600+1), 600, Status.NEW);
        Task task3 = new Task(3, "Task3", "Task3",
                Instant.EPOCH.plusSeconds(600*2+1), 600, Status.NEW);

        manager.add(task1);
        manager.add(task2);
        manager.add(task3);

        assertEquals(List.of(task1, task2, task3), manager.getHistory());

    }

    @Test
    public void clearHistoryTest() {

        Task task1 = new Task(1, "Task1", "Task1",
                Instant.EPOCH, 600, Status.NEW);
        Task task2 = new Task(2, "Task2", "Task2",
                Instant.EPOCH.plusSeconds(600+1), 600, Status.NEW);
        Task task3 = new Task(3, "Task3", "Task3",
                Instant.EPOCH.plusSeconds(600*2+1), 600, Status.NEW);

        manager.add(task1);
        manager.add(task2);
        manager.add(task3);

        for (Task task:manager.getHistory()) {
            manager.remove(task.getId());
        }

        assertEquals(emptyList, manager.getHistory());

    }

    @Test
    public void removeTask() {

        Task task1 = new Task(1, "Task1", "Task1",
                Instant.EPOCH, 600, Status.NEW);
        Task task2 = new Task(2, "Task2", "Task2",
                Instant.EPOCH.plusSeconds(600+1), 600, Status.NEW);
        Task task3 = new Task(3, "Task3", "Task3",
                Instant.EPOCH.plusSeconds(600*2+1), 600, Status.NEW);

        manager.add(task1);
        manager.add(task2);
        manager.add(task3);

        manager.remove(task2.getId());

        assertEquals(List.of(task1, task3), manager.getHistory());

    }

    @Test
    public void noDuplicatesTest() {

        Task task1 = new Task(1, "Task1", "Task1",
                Instant.EPOCH, 600, Status.NEW);
        Task task2 = new Task(2, "Task2", "Task2",
                Instant.EPOCH.plusSeconds(600+1), 600, Status.NEW);
        Task task3 = new Task(3, "Task3", "Task3",
                Instant.EPOCH.plusSeconds(600*2+1), 600, Status.NEW);

        manager.add(task1);
        manager.add(task2);
        manager.add(task3);
        manager.add(task1);
        manager.add(task2);
        manager.add(task3);
        manager.add(task2);
        manager.add(task3);

        assertEquals(List.of(task1, task2, task3), manager.getHistory());

    }

    @Test
    public void noTaskRemoveIfIncorrectIDTest() {

        Task task1 = new Task(1, "Task1", "Task1",
                Instant.EPOCH, 600, Status.NEW);
        Task task2 = new Task(2, "Task2", "Task2",
                Instant.EPOCH.plusSeconds(600+1), 600, Status.NEW);
        Task task3 = new Task(3, "Task3", "Task3",
                Instant.EPOCH.plusSeconds(600*2+1), 600, Status.NEW);

        manager.add(task1);
        manager.add(task2);
        manager.add(task3);

        manager.remove(42);
        manager.remove(17);
        manager.remove(9);

        assertEquals(List.of(task1, task2, task3), manager.getHistory());

    }

}