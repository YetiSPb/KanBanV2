package main;

import main.managers.Managers;
import main.managers.manager_tasks.TaskManager;
import main.tasks.Epic;
import main.tasks.Subtask;
import main.tasks.Task;

import java.time.Instant;

public class Main {

    public static void main(String[] args) {

        TaskManager manager = Managers.getDefault();

        Task task1 = new Task("Задача 1", "Описание задачи 1", Instant.EPOCH, 600);
        Task task2 = new Task("Задача 1", "Описание задачи 1", Instant.EPOCH, 600);

        manager.addTask(task1);
        manager.addTask(task2);

        Epic epic1= new Epic("Эпик 1", "Описание эпика 1");
        Epic epic2= new Epic("Эпик 2", "Описание эпика 2");

        manager.addEpic(epic1);
        manager.addEpic(epic2);
        
        Subtask subtask11=new Subtask(
                "Подзадача 11", "Описание подзадачи 11", Instant.EPOCH,600, epic1.getId());
        Subtask subtask12=new Subtask(
                "Подзадача 12", "Описание подзадачи 12", Instant.EPOCH,600, epic1.getId());
        Subtask subtask13=new Subtask(
                "Подзадача 13", "Описание подзадачи 13", Instant.EPOCH,600, epic1.getId());

        Subtask subtask21=new Subtask(
                "Подзадача 21", "Описание подзадачи 21", Instant.EPOCH,600, epic2.getId());
        Subtask subtask22=new Subtask(
                "Подзадача 22", "Описание подзадачи 22", Instant.EPOCH,600, epic2.getId());

        manager.addSubtask(subtask11);
        manager.addSubtask(subtask12);
        manager.addSubtask(subtask13);
        manager.addSubtask(subtask21);
        manager.addSubtask(subtask22);

        manager.getEpic(epic2.getId());
        System.out.println(manager.getHistory());

        manager.getTask(task1.getId());
        System.out.println(manager.getHistory());

        manager.getSubtask(subtask12.getId());
        System.out.println(manager.getHistory());

        manager.getSubtask(subtask22.getId());
        System.out.println(manager.getHistory());

        manager.getTask(task1.getId());
        System.out.println(manager.getHistory());

        manager.deleteSubtask(subtask22.getId());
        System.out.println(manager.getHistory());

        manager.getSubtask(subtask12.getId());
        System.out.println(manager.getHistory());


    }

}