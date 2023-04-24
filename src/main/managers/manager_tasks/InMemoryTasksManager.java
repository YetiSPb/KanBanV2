package main.managers.manager_tasks;

import main.managers.Managers;
import main.managers.manager_history.HistoryManager;
import main.tasks.Epic;
import main.tasks.Subtask;
import main.tasks.Task;

import java.util.*;

public class InMemoryTasksManager implements TaskManager {
    protected int id = 0;

    private final Map<Integer, Task> tasks = new HashMap<>();
    private final Map<Integer, Subtask> subtasks = new HashMap<>();
    private final Map<Integer, Epic> epics = new HashMap<>();

    private final HistoryManager historyManager = Managers.getDefaultHistory();

    //get---------------------------------------------------------------------------------------------------------------
    private int generateId() {
        return ++id;
    }

    @Override
    public Task getTask(int id) {
        historyManager.add(tasks.get(id));
        return tasks.get(id);
    }

    @Override
    public Epic getEpic(int id) {
        historyManager.add(epics.get(id));
        return epics.get(id);
    }

    @Override
    public Subtask getSubtask(int id) {
        historyManager.add(subtasks.get(id));
        return subtasks.get(id);
    }

    @Override
    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public List<Epic> getAllEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public List<Subtask> getAllSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    @Override
    public List<Subtask> getAllSubtasksByEpic(int id) {
        List<Subtask> subtasksSample = new ArrayList<>();
        epics.get(id).getSubtasksId().forEach(subtaskId -> subtasksSample.add(subtasks.get(subtaskId)));
        return subtasksSample;
    }

    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    //add-----------------------------------------------------------------------------------------------------------
    @Override
    public Task addTask(Task task) {
        if (task == null) return null;
        assigningID(task);
        tasks.put(task.getId(), task);
        return task;
    }

    @Override
    public Epic addEpic(Epic epic) {
        if (epic == null) return null;
        assigningID(epic);
        epics.put(epic.getId(), epic);
        return epic;
    }

    @Override
    public Subtask addSubtask(Subtask subtask) {
        if (subtask == null) return null;
        assigningID(subtask);
        if (epics.containsKey(subtask.getEpicID())) {
            Epic epic = epics.get(subtask.getEpicID());
            subtasks.put(subtask.getId(), subtask);
            epic.addSubtaskId(subtask.getId());
            return subtask;
        } else {
            System.out.println("Эпик не найден");
            return null;
        }
    }

    private void assigningID(Task task) {
        if (task.getId() == 0) {
            task.setId(generateId());
        }
    }

    //delete------------------------------------------------------------------------------------------------------------
    @Override
    public void deleteTask(int id) {
        if (tasks.containsKey(id)) {
            historyManager.remove(id);
            tasks.remove(id);
        } else {
            System.out.println("Задача не найдена");
        }
    }

    @Override
    public void deleteEpic(int id) {
        Epic epic = epics.get(id);
        if (epic != null) {

            while (epic.getSubtasksId().size() > 0) {
                deleteSubtask(epic.getSubtasksId().get(0));//Удаляем подзадачу
            }

            historyManager.remove(id);//Удаляем из истории
            epics.remove(id);//Удаляем эпик
        } else System.out.println("Эпик не найден");
    }

    @Override
    public void deleteSubtask(int id) {
        Subtask subtask = subtasks.get(id);
        if (subtask != null) {
            historyManager.remove(id);//Удаляем из истории
            epics.get(subtask.getEpicID()).deleteSubtaskId(subtask.getId());//Удаляем из эпика
            subtasks.remove(id);//Удаляем из менеджера
        } else {
            System.out.println("Подзадача не найдена");
        }
    }

    @Override
    public void deleteAllTasks() {
        tasks.clear();
    }

    @Override
    public void deleteAllEpics() {
        subtasks.clear();
        epics.clear();
    }

    @Override
    public void deleteAllSubtasks() {
        for (Epic epic : epics.values()) {
            for (int subtaskId : epic.getSubtasksId()) {
                subtasks.remove(subtaskId);
            }
            epic.getSubtasksId().clear();
        }
    }

    @Override
    public void deleteAllSubtasksByEpic(Epic epic) {
        if (epic != null) {
            for (int subtaskId : epic.getSubtasksId()) {
                subtasks.remove(subtaskId);
            }
            epic.getSubtasksId().clear();
        }
    }


    //update------------------------------------------------------------------------------------------------------------

    /*
   @Override
    public void updateTask(Task task) {
        if (task != null && tasks.containsKey(task.getId())) {
            tasks.put(task.getId(), task);
            prioritizedTasks.removeIf(task1 -> (task1.getId()) == task.getId());
            addToPrioritizedTasks(task);
            checkIntersections();
        } else {
            System.out.println("Задача не найдена");
        }
    }


    @Override
    public void updateEpic(Epic epic) {
        if (epic != null && epics.containsKey(epic.getId())) {
            epics.put(epic.getId(), epic);
            updateStatusEpic(epic);
        } else {
            System.out.println("Эпик не найден");
        }
    }


    private void updateStatusEpic(Epic epic) {
        if (epic.getSubtaskIds().size() == 0) {
            epic.setStatus(Status.NEW);
        } else {
            int countDone = 0;
            int countNew = 0;


            Status subTaskStatus;
            for (int subTaskId : epic.getSubtaskIds()) {
                subTaskStatus = subtasks.get(subTaskId).getStatus();
                if (subTaskStatus == Status.DONE) {
                    countDone++;
                }
                if (subTaskStatus == Status.NEW) {
                    countNew++;
                }
                if (subTaskStatus == Status.IN_PROGRESS) {
                    epic.setStatus(Status.IN_PROGRESS);
                    return;
                }
            }

            if (countDone == epic.getSubtaskIds().size()) {
                epic.setStatus(Status.DONE);
            } else if (countNew == epic.getSubtaskIds().size()) {
                epic.setStatus(Status.NEW);
            } else {
                epic.setStatus(Status.IN_PROGRESS);
            }
        }
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        if (subtask != null && subtasks.containsKey(subtask.getId())) {
            subtasks.put(subtask.getId(), subtask);
            Epic epic = epics.get(subtask.getEpicID());
            epic.updateEpicState(subtasks);
            updateStatusEpic(epic);
            checkIntersections();
        } else {
            System.out.println("Подзадача не найдена");
        }
    }
*/

    //------------------------------------------------------------------------------------------------------------------
    @Override
    public String toString() {
        return "Manager{" +
                "tasks=" + tasks +
                ", subtasks=" + subtasks +
                ", epics=" + epics +
                '}';
    }

/*    public List<Task> getPrioritizedTasks() {
        return new ArrayList<>(prioritizedTasks);
    }

    @Override // сравнение тасков по getStartTime()
    public int compare(Task o1, Task o2) {

        return o1.getStartTime().compareTo(o2.getStartTime());

    }*/

  /*  private void addToPrioritizedTasks(Task task) {
        prioritizedTasks.add(task);
    }*/

}