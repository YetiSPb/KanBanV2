package main.managers.manager_tasks;

import main.managers.Exeption.ManagerSaveException;
import main.tasks.Epic;
import main.tasks.Subtask;
import main.tasks.Task;
import main.tasks.status.Status;
import main.tasks.status.TaskType;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTasksManager extends InMemoryTasksManager {

    private static final String TABLE_HEADER = "id,type,name,status,description,startTime,duration,endTime,epic";
    private static final String FILE_FOR_DATA = "src/main/resources/file4save.csv";
    private static final String TAG_HISTORY = "<HISTORY>";

    private final Path pathForFileSave = Paths.get(FILE_FOR_DATA);

    public static FileBackedTasksManager loadFromFile(File file) {

        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager();

        List<String> stringsTasks = new ArrayList<>();

        try (Reader fileReader = new FileReader(file)) {
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            while (bufferedReader.ready()) {
                stringsTasks.add(bufferedReader.readLine());
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка чтения из файла:" + FILE_FOR_DATA, e);
        }

        int startBlockHistory = stringsTasks.indexOf(TAG_HISTORY);

        for (int i = 1; i < startBlockHistory; i++) {

            List<String> taskStr = new ArrayList<>(List.of(stringsTasks.get(i).split(",")));

            TaskType taskType = TaskType.valueOf(taskStr.get(1));

            if (taskType == TaskType.TASK) {
                fileBackedTasksManager.loadTask(arrayListToTask(taskStr));
            } else if (taskType == TaskType.EPIC) {
                fileBackedTasksManager.loadEpic(arrayListToEpic(taskStr));
            } else if (taskType == TaskType.SUBTASK) {
                fileBackedTasksManager.loadSubtask(arrayListToSubtask(taskStr));
            }

        }

        if (stringsTasks.size() > startBlockHistory + 1) {
            List<String> idsHistory = new ArrayList<>(List.of(stringsTasks.get(startBlockHistory + 1).split(",")));
            for (String id:idsHistory) {
                fileBackedTasksManager.getTask(Integer.parseInt(id));
            }
        }


        return fileBackedTasksManager;
    }

    private static Task arrayListToTask(List<String> taskString) {
        return new Task(Integer.parseInt(taskString.get(0)), taskString.get(2), taskString.get(4),
                Instant.parse(taskString.get(5)), Long.parseLong(taskString.get(6)),
                Status.whatStatus(taskString.get(3)));
    }

    private static Epic arrayListToEpic(List<String> epicString) {
        return new Epic(Integer.parseInt(epicString.get(0)), epicString.get(2), epicString.get(4),
                Instant.parse(epicString.get(5)), Long.parseLong(epicString.get(6)),
                Status.whatStatus(epicString.get(3)));
    }

    private static Subtask arrayListToSubtask(List<String> subtaskString) {
        return new Subtask(Integer.parseInt(subtaskString.get(0)), subtaskString.get(2), subtaskString.get(4),
                Instant.parse(subtaskString.get(5)), Long.parseLong(subtaskString.get(6)),
                Status.whatStatus(subtaskString.get(3)), Integer.parseInt(subtaskString.get(8)));
    }

    private void save() {

        try {
            if (Files.exists(pathForFileSave)) {
                Files.delete(pathForFileSave);
                Files.createFile(pathForFileSave);
            } else {
                Files.createFile(pathForFileSave);
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка создания файла:" + FILE_FOR_DATA, e);
        }

        try (Writer fileWrite = new FileWriter(FILE_FOR_DATA)) {

            List<Task> listTasks = new ArrayList<>();

            listTasks.addAll(getAllTasks());
            listTasks.addAll(getAllEpics());
            listTasks.addAll(getAllSubtasks());

            StringBuilder stringTasksForSave = new StringBuilder();

            stringTasksForSave.append(TABLE_HEADER + "\n");
            listTasks.forEach(task -> stringTasksForSave.append(task).append("\n"));
            stringTasksForSave.append(TAG_HISTORY + "\n");
            getHistory().forEach(task -> stringTasksForSave.append(task.getId()).append(","));
            stringTasksForSave.delete(stringTasksForSave.length() - 1, stringTasksForSave.length());

            fileWrite.write(stringTasksForSave.toString());
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка записи в файл:" + FILE_FOR_DATA, e);
        }
    }

    private void loadTask(Task task) {
        super.addTask(task);
    }

    private void loadEpic(Task task) {
        super.addTask(task);
    }

    private void loadSubtask(Task task) {
        super.addTask(task);
    }

    @Override
    public Task addTask(Task task) {
        Task newTask = super.addTask(task);
        save();
        return newTask;
    }


    @Override
    public Epic addEpic(Epic epic) {
        Epic newEpic = super.addEpic(epic);
        save();
        return newEpic;
    }

    @Override
    public Subtask addSubtask(Subtask subtask) {
        Subtask newSubtask = super.addSubtask(subtask);
        save();
        return newSubtask;
    }


    @Override
    public void deleteTask(int id) {
        super.deleteTask(id);
        save();
    }

    @Override
    public void deleteEpic(int id) {
        super.deleteEpic(id);
        save();
    }

    @Override
    public void deleteSubtask(int id) {
        super.deleteSubtask(id);
        save();
    }

    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
        save();
    }

    @Override
    public void deleteAllEpics() {
        super.deleteAllEpics();
        save();
    }

    @Override
    public void deleteAllSubtasks() {
        super.deleteAllSubtasks();
        save();
    }

    @Override
    public void deleteAllSubtasksByEpic(Epic epic) {
        super.deleteAllSubtasksByEpic(epic);
        save();
    }

    @Override
    public Task getTask(int id) {
        Task task = super.getTask(id);
        save();
        return task;
    }

    @Override
    public Epic getEpic(int id) {
        Epic epic = super.getEpic(id);
        save();
        return epic;
    }

    @Override
    public Subtask getSubtask(int id) {
        Subtask subtask = super.getSubtask(id);
        save();
        return subtask;
    }
}
