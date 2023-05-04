package main;

import main.managers.Managers;
import main.managers.tasks.FileBackedTasksManager;
import main.managers.tasks.TaskManager;
import main.tasks.Epic;
import main.tasks.Subtask;
import main.tasks.Task;

import java.io.*;
import java.nio.file.Paths;
import java.time.Instant;

public class Main {

    public static void main(String[] args) throws IOException {

        TaskManager manager = Managers.getDefaultFileBackedTasksManager();

        Task task1 = new Task("Задача 1", "Описание задачи 1",
                Instant.EPOCH.plusSeconds(600 * 6 + 1), 600);
        manager.addTask(task1);

        Task task2 = new Task("Задача 2", "Описание задачи 2",
                Instant.EPOCH.plusSeconds(600 * 5 + 1), 600);
        manager.addTask(task2);

        System.out.println(manager.getPrioritizedTasks());

        Task task3 = new Task(task1.getId(),"Задача 3", "Описание задачи 3",
                Instant.EPOCH.plusSeconds(600 * 6 + 1), 600,task1.getStatus());

        manager.updateTask(task3);
        System.out.println(manager.getPrioritizedTasks());


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

        Subtask subtask13 = new Subtask("Подзадача 13", "Описание подзадачи 13",
                Instant.EPOCH.plusSeconds(600 * 3 + 1), 600, epic1.getId());
        manager.addSubtask(subtask13);

        Subtask subtask21 = new Subtask("Подзадача 21", "Описание подзадачи 21",
                Instant.EPOCH.plusSeconds(600), 600, epic2.getId());
        manager.addSubtask(subtask21);

        Subtask subtask22 = new Subtask("Подзадача 22", "Описание подзадачи 22",
                Instant.EPOCH, 600, epic2.getId());
        manager.addSubtask(subtask22);

        manager.deleteAllEpics();
        System.out.println(manager.getPrioritizedTasks());


        manager.getEpic(epic2.getId());
        System.out.println(manager.getHistory());

        System.out.println();
        manager.getTask(task1.getId());
        System.out.println(manager.getHistory());

        System.out.println();
        manager.getSubtask(subtask12.getId());
        System.out.println(manager.getHistory());

        System.out.println();
        manager.getSubtask(subtask22.getId());
        System.out.println(manager.getHistory());

        System.out.println();
        manager.getTask(task1.getId());
        System.out.println(manager.getHistory());

        System.out.println();
        manager.deleteSubtask(subtask22.getId());
        System.out.println(manager.getHistory());

        System.out.println();
        manager.getSubtask(subtask12.getId());
        System.out.println(manager.getHistory());

        System.out.println();
        System.out.println(manager.getPrioritizedTasks());

        TaskManager taskManagerLoad = FileBackedTasksManager.loadFromFile(Paths.get("src/resources/file4save.csv").toFile());

        taskManagerLoad.getTask(1);
        taskManagerLoad.getTask(1);
        System.out.println(taskManagerLoad);
        System.out.println();
        System.out.println(taskManagerLoad.getHistory());
    }

}