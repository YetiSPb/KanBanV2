package main.managers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import main.managers.adapter.InstantAdapter;
import main.managers.http.client.KVTasksClient;
import main.tasks.Epic;
import main.tasks.Subtask;
import main.tasks.Task;

import java.lang.reflect.Type;
import java.time.Instant;
import java.util.List;

public class HttpTasksManager extends FileBackedTasksManager {

    protected KVTasksClient client = new KVTasksClient();
    private final Gson gson = new GsonBuilder().registerTypeAdapter(Instant.class, new InstantAdapter()).create();

    public KVTasksClient getClient() {
        return client;
    }

    @Override
    public void save() {

        String prioritized = gson.toJson(super.getPrioritizedTasks());
        client.put("tasks", prioritized);

        String history = gson.toJson(super.getHistory());
        client.put("tasks/history", history);

        String tasks = gson.toJson(super.getAllTasks());
        client.put("tasks/task", tasks);

        String epics = gson.toJson(super.getAllEpics());
        client.put("tasks/epic", epics);

        String subtasks = gson.toJson(super.getAllSubtasks());
        client.put("tasks/subtask", subtasks);

    }

    public void load() {

        String jsonTasks = client.load("tasks/task");
        Type taskType = new TypeToken<List<Task>>() {
        }.getType();

        List<Task> tasks = gson.fromJson(jsonTasks, taskType);

        tasks.forEach(value -> super.tasks.put(value.getId(), value));


        String jsonEpics = client.load("tasks/epic");
        Type epicType = new TypeToken<List<Epic>>() {
        }.getType();

        List<Epic> epics = gson.fromJson(jsonEpics, epicType);

        epics.forEach(value -> super.epics.put(value.getId(), value));

        String jsonSubtasks = client.load("tasks/subtask");
        Type subtaskType = new TypeToken<List<Subtask>>() {
        }.getType();

        List<Subtask> subtasks = gson.fromJson(jsonSubtasks, subtaskType);

        subtasks.forEach(value -> super.subtasks.put(value.getId(), value));

        String jsonPrioritizedTasks = client.load("tasks");
        Type prioritizedTaskType = new TypeToken<List<Task>>() {
        }.getType();


        List<Task> priorityTasks = gson.fromJson(jsonPrioritizedTasks, prioritizedTaskType);

        if (priorityTasks != null) {
            prioritizedTasks.addAll(priorityTasks);
        }

        String gsonHistory = client.load("tasks/history");
        Type historyType = new TypeToken<List<Task>>() {
        }.getType();


        List<Task> history = gson.fromJson(gsonHistory, historyType);


        history.forEach(super.historyManager::add);
//        for (Task task : history) {
//            historyManager.add(task);
//        }

    }
}
