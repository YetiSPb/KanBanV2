package main;

import main.manager.InMemoryTasksManager;
import main.tasks.Epic;
import main.tasks.Subtask;
import main.tasks.Task;

import java.time.Instant;

public class Main {

    public static void main(String[] args) {
        InMemoryTasksManager taskManager = new InMemoryTasksManager();

        taskManager.addTask(
                new Task("Task 1", "Task 1 description", Instant.ofEpochSecond(10000), 50));
        taskManager.addTask(
                new Task("Task 2", "Task 2 description", Instant.ofEpochSecond(10000), 50));

        Epic epic1 = new Epic("Epic 1", "Epic 1 description");
        taskManager.addEpic(epic1);

        taskManager.addSubtask(
                new Subtask("Subtask 1", "Subtask 1 description",
                        Instant.ofEpochSecond(20000), 50, epic1.getId()));
        taskManager.addSubtask(
                new Subtask("Subtask 2", "Subtask 2 description",
                        Instant.ofEpochSecond(30000), 50, epic1.getId()));


        for (int i = 1; i < 18; i++) {
            taskManager.getTask(1);
        }
        taskManager.getTask(2);

        System.out.println(taskManager.getHistory());
    }
}