package main.managers.tasks;

import main.managers.FileBackedTasksManager;
import main.managers.TaskManager;
import main.tasks.Epic;
import main.tasks.Subtask;
import main.tasks.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

import main.managers.Managers;

class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager> {

    private static final String FILE_EXPECTED_DATA = "src/test/resources/file4save.csv";
    private static final String FILE_FOR_DATA = "src/resources/file4save.csv";

    @BeforeEach
    void setUp() {
        manager = new FileBackedTasksManager();
    }

    @Test
    public void saveToFileTest() throws IOException {

        Task task1 = manager.addTask(new Task("Задача 1", "Описание задачи 1",
                Instant.EPOCH.plusSeconds(600 * 6 + 1), 600));
        Epic epic1 = manager.addEpic(new Epic("Эпик 1", "Описание эпика 1"));
        Subtask subtask1 = manager.addSubtask(new Subtask("Подзадача 11", "Описание подзадачи 11",
                Instant.EPOCH.plusSeconds(600 * 4 + 1), 600, epic1.getId()));

        manager.getTaskById(task1.getId());
        manager.getEpicById(epic1.getId());
        manager.getSubtaskById(subtask1.getId());

        String fileContents = Files.readString(Path.of(FILE_FOR_DATA));
        String fileExpected = Files.readString(Path.of(FILE_EXPECTED_DATA));

        assertEquals(fileExpected, fileContents);

    }

    @Test
    public void loadFromFileTest() {
        Path filePath = Path.of(FILE_EXPECTED_DATA);
        manager = FileBackedTasksManager.load(filePath.toFile());

        TaskManager managerActual = Managers.getDefaultFileBackedTasksManager();

        Task task1 = new Task("Задача 1", "Описание задачи 1",
                Instant.EPOCH.plusSeconds(600 * 6 + 1), 600);
        managerActual.addTask(task1);

        Epic epic1 = new Epic("Эпик 1", "Описание эпика 1");
        managerActual.addEpic(epic1);

        Subtask subtask1 = new Subtask("Подзадача 11", "Описание подзадачи 11",
                Instant.EPOCH.plusSeconds(600 * 4 + 1), 600, epic1.getId());
        managerActual.addSubtask(subtask1);

        managerActual.getTaskById(task1.getId());
        managerActual.getEpicById(epic1.getId());
        managerActual.getSubtaskById(subtask1.getId());

        assertEquals(managerActual.getHistory(), manager.getHistory());

        assertEquals(managerActual.getTaskById(task1.getId()), manager.getTaskById(task1.getId()));
        assertEquals(managerActual.getEpicById(epic1.getId()), manager.getEpicById(epic1.getId()));
        assertEquals(managerActual.getSubtaskById(subtask1.getId()), manager.getSubtaskById(subtask1.getId()));
    }
}