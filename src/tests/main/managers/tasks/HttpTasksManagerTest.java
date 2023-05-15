package main.managers.tasks;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import main.managers.FileBackedTasksManager;
import main.managers.HttpTasksManager;
import main.managers.TaskManager;
import main.managers.adapter.InstantAdapter;
import main.managers.http.client.KVTasksClient;
import main.managers.http.server.KVServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class HttpTasksManagerTest extends TaskManagerTest<HttpTasksManager> {

    protected KVServer server;
    private static final String FILE_EXPECTED_DATA = "src/test/resources/file4save.csv";

    @BeforeEach
    public void loadInitialConditions() throws IOException {

        server = new KVServer();
        server.start();

        manager = new HttpTasksManager();

    }

    @AfterEach
    void serverStop() {

        server.stop();

    }

    @Test
    void loadFromServerTest() {

        Path filePath = Path.of(FILE_EXPECTED_DATA);
        TaskManager taskManager = FileBackedTasksManager.load(filePath.toFile());

        KVTasksClient client = manager.getClient();
        Gson gson = new GsonBuilder().registerTypeAdapter(Instant.class, new InstantAdapter()).create();

        String prioritized = gson.toJson(taskManager.getPrioritizedTasks());
        client.put("tasks", prioritized);

        String tasks = gson.toJson(taskManager.getAllTasks());
        client.put("tasks/task", tasks);

        String epics = gson.toJson(taskManager.getAllEpics());
        client.put("tasks/epic", epics);

        String subtasks = gson.toJson(taskManager.getAllSubtasks());
        client.put("tasks/subtask", subtasks);

        String history = gson.toJson(taskManager.getHistory());
        client.put("tasks/history", history);

        manager.load();

        assertNotNull(tasks);
        assertEquals(manager.getAllTasks(), taskManager.getAllTasks());
        assertEquals(manager.getAllEpics(), taskManager.getAllEpics());
        assertEquals(manager.getAllSubtasks(), taskManager.getAllSubtasks());
        assertEquals(manager.getPrioritizedTasks(), taskManager.getPrioritizedTasks());
        assertEquals(manager.getHistory(), taskManager.getHistory());

    }

}