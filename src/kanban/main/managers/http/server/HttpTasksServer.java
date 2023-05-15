package main.managers.http.server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import main.managers.adapter.InstantAdapter;
import main.managers.TaskManager;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import main.tasks.Epic;
import main.tasks.Subtask;
import main.tasks.Task;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class HttpTasksServer {
    private static final int PORT = 8056;
    private final HttpServer httpServer = HttpServer.create(new InetSocketAddress(PORT), 0);
    private HttpExchange httpExchange;
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    private int responseCode = 404;
    private byte[] response = new byte[0];

    Gson gson;

    private final TaskManager taskManager;
    private String query;

    public HttpTasksServer(TaskManager manager) throws IOException {
        this.taskManager = manager;
        httpServer.createContext("/tasks", new TasksHandler());
        gson = new GsonBuilder()
                .serializeNulls()
                .setPrettyPrinting()
                .registerTypeAdapter(Instant.class, new InstantAdapter())
                .create();
    }

    public void start() {
        httpServer.start();
        System.out.println("HTTP-сервер запущен на " + PORT + " порту!");
    }

    public void stop() {
        httpServer.stop(0);
        System.out.println("HTTP-сервер остановлен на " + PORT + " порту!");
    }

    class TasksHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange exchange) {
            System.out.println("Началась обработка /tasks запроса от клиента.");
            httpExchange = exchange;
            ArrayList<String> regexRoots = new ArrayList<>();

            regexRoots.add("^/tasks$");
            regexRoots.add("^/tasks/task$");
            regexRoots.add("^/tasks/epic$");
            regexRoots.add("^/tasks/subtask$");
            regexRoots.add("^/tasks/task/$");
            regexRoots.add("^/tasks/subtask/$");
            regexRoots.add("^/tasks/epic/$");
            regexRoots.add("^/tasks/history$");
            regexRoots.add("^/tasks/task/$");

            String path = exchange.getRequestURI().getPath();
            query = exchange.getRequestURI().getQuery();
            String method = exchange.getRequestMethod();

            try {
                switch (method) {
                    case "GET":
                        if (Pattern.matches(regexRoots.get(0), path))
                            getPrioritizedTasks();
                        if (Pattern.matches(regexRoots.get(1), path))
                            getTasks();
                        if (Pattern.matches(regexRoots.get(2), path))
                            getEpics();
                        if (Pattern.matches(regexRoots.get(3), path))
                            getSubtasks();
                        if (Pattern.matches(regexRoots.get(4), path))
                            getTaskByID();
                        if (Pattern.matches(regexRoots.get(5), path))
                            getSubtaskByID();
                        if (Pattern.matches(regexRoots.get(6), path))
                            getEpicByID();
                        if (Pattern.matches(regexRoots.get(7), path))
                            getHistory();
                        break;

                    case "POST":
                        if (Pattern.matches(regexRoots.get(1), path))
                            addTask();
                        if (Pattern.matches(regexRoots.get(2), path))
                            addEpic();
                        if (Pattern.matches(regexRoots.get(3), path))
                            addSubtask();
                        break;

                    case "DELETE":
                        if (Pattern.matches(regexRoots.get(1), path))
                            deleteAllTasksEpicsSubtasks();
                        if (Pattern.matches(regexRoots.get(8), path))
                            deleteByID();
                        break;

                    default:
                        System.out.println("Метод " + method + " не поддерживается.");
                }

                exchange.sendResponseHeaders(responseCode, 0);
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(response);
                }
            } catch (IOException e) {
                System.out.println("Ошибка выполнения запроса: " + e.getMessage());
            } finally {
                exchange.close();
            }
        }

        private void getPrioritizedTasks() {
            System.out.println("GET: началась обработка /tasks запроса от клиента.\n");

            String tasksToJson = gson.toJson(taskManager.getPrioritizedTasks());

            if (!tasksToJson.isEmpty()) {
                response = tasksToJson.getBytes(DEFAULT_CHARSET);
                httpExchange.getResponseHeaders().add("Content-Type", "application/json");
                responseCode = 200;
            } else
                responseCode = 400;
        }

        private void getTasks() {
            System.out.println("GET: началась обработка /tasks/task запроса от клиента.\n");

            String tasksToJson = gson.toJson(taskManager.getAllTasks());

            if (!tasksToJson.isEmpty()) {
                response = tasksToJson.getBytes(DEFAULT_CHARSET);
                httpExchange.getResponseHeaders().add("Content-Type", "application/json");
                responseCode = 200;
            } else
                responseCode = 400;
        }

        private void getEpics() {
            System.out.println("GET: началась обработка /tasks/epic запроса от клиента.\n");

            String tasksToJson = gson.toJson(taskManager.getAllEpics());

            if (!tasksToJson.isEmpty()) {
                response = tasksToJson.getBytes(DEFAULT_CHARSET);
                httpExchange.getResponseHeaders().add("Content-Type", "application/json");
                responseCode = 200;
            } else
                responseCode = 400;
        }

        private void getSubtasks() {
            System.out.println("GET: началась обработка /tasks/subtask запроса от клиента.\n");

            String tasksToJson = gson.toJson(taskManager.getAllSubtasks());

            if (!tasksToJson.isEmpty()) {
                response = tasksToJson.getBytes(DEFAULT_CHARSET);
                httpExchange.getResponseHeaders().add("Content-Type", "application/json");
                responseCode = 200;
            } else
                responseCode = 400;
        }

        private void getTaskByID() {
            System.out.println("GET: началась обработка /tasks/task/d+ запроса от клиента.\n");
            int id = Integer.parseInt(query.replaceFirst("id=", ""));
            String tasksToJson = gson.toJson(taskManager.getTaskById(id));

            if (!tasksToJson.isEmpty()) {
                response = tasksToJson.getBytes(DEFAULT_CHARSET);
                httpExchange.getResponseHeaders().add("Content-Type", "application/json");
                responseCode = 200;
            } else
                responseCode = 400;
        }

        private void getEpicByID() {
            System.out.println("GET: началась обработка /tasks/epic/d+ запроса от клиента.\n");
            int id = Integer.parseInt(query.replaceFirst("id=", ""));
            String tasksToJson = gson.toJson(taskManager.getEpicById(id));

            if (!tasksToJson.isEmpty()) {
                response = tasksToJson.getBytes(DEFAULT_CHARSET);
                httpExchange.getResponseHeaders().add("Content-Type", "application/json");
                responseCode = 200;
            } else
                responseCode = 400;
        }

        private void getSubtaskByID() {
            System.out.println("GET: началась обработка /tasks/subtask/d+ запроса от клиента.\n");
            int id = Integer.parseInt(query.replaceFirst("id=", ""));
            String tasksToJson = gson.toJson(taskManager.getSubtaskById(id));

            if (!tasksToJson.isEmpty()) {
                response = tasksToJson.getBytes(DEFAULT_CHARSET);
                httpExchange.getResponseHeaders().add("Content-Type", "application/json");
                responseCode = 200;
            } else
                responseCode = 400;
        }

        private void getHistory() {
            System.out.println("GET: началась обработка /tasks/history запроса от клиента.\n");

            String tasksToJson = gson.toJson(taskManager.getHistory());

            if (!tasksToJson.isEmpty()) {
                response = tasksToJson.getBytes(DEFAULT_CHARSET);
                httpExchange.getResponseHeaders().add("Content-Type", "application/json");
                responseCode = 200;
            } else
                responseCode = 400;
        }

        private void addTask() throws IOException {
            System.out.println("POST: началась обработка /tasks/task запроса от клиента.\n");
            InputStream inputStream = httpExchange.getRequestBody();
            String body = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);
            Task task = gson.fromJson(body, Task.class);
            responseCode = 200;

            if (!taskManager.getAllTasks().contains(task)) {
                taskManager.addTask(task);
                System.out.println("Задача #" + task.getId() + " создана.\n" + body);
            } else {
                taskManager.updateTask(task);
                System.out.println("Задача #" + task.getId() + " обновлена.\n" + body);
            }
        }

        private void addEpic() throws IOException {
            System.out.println("POST: началась обработка /tasks/epic запроса от клиента.\n");
            InputStream inputStream = httpExchange.getRequestBody();
            String body = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);
            Epic epic = gson.fromJson(body, Epic.class);
            responseCode = 200;

            if (!taskManager.getAllEpics().contains(epic)) {
                taskManager.addEpic(epic);
                System.out.println("Задача #" + epic.getId() + " создана.\n" + body);
            } else {
                taskManager.updateTask(epic);
                System.out.println("Задача #" + epic.getId() + " обновлена.\n" + body);
            }
        }

        private void addSubtask() throws IOException {
            System.out.println("POST: началась обработка /tasks/subtask запроса от клиента.\n");
            InputStream inputStream = httpExchange.getRequestBody();
            String body = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);
            Subtask subtask = gson.fromJson(body, Subtask.class);
            responseCode = 200;

            if (!taskManager.getAllSubtasks().contains(subtask)) {
                taskManager.addSubtask(subtask);
                System.out.println("Задача #" + subtask.getId() + " создана.\n" + body);
            } else {
                taskManager.updateTask(subtask);
                System.out.println("Задача #" + subtask.getId() + " обновлена.\n" + body);
            }
        }

        private void deleteAllTasksEpicsSubtasks() {
            System.out.println("DELETE: началась обработка /tasks/task запроса от клиента.\n");
            taskManager.deleteAllTasks();
            taskManager.deleteAllSubtasks();
            taskManager.deleteAllEpics();
            System.out.println("Все таски, эпики и сабтаски удалены.");
            responseCode = 200;
        }

        private void deleteByID() {
            System.out.println("DELETE: началась обработка /tasks/task/d+ запроса от клиента.\n");
            int id = Integer.parseInt(query.replaceFirst("id=", ""));
            if (taskManager.getTaskById(id) != null) {
                taskManager.deleteTaskById(id);
                System.out.println("Задача #" + id + " удалена.\n");
            }
            if (taskManager.getSubtaskById(id) != null) {
                taskManager.deleteSubtaskById(id);
                System.out.println("Подзадача #" + id + " удалена.\n");
            }
            if (taskManager.getEpicById(id) != null) {
                taskManager.deleteEpicById(id);
                System.out.println("Эпик #" + id + " удалена.\n");
            }
            responseCode = 200;
        }

    }
}
