package main.managers;

import main.managers.exeption.TaskIsNull;
import main.managers.exeption.TaskAlreadyExistsException;
import main.managers.exeption.TaskIntersectsException;
import main.managers.exeption.TaskNotFound;
import main.managers.history.HistoryManager;
import main.tasks.Epic;
import main.tasks.Subtask;
import main.tasks.Task;
import main.tasks.status.Status;

import java.time.Duration;
import java.time.Instant;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    protected int id = 0;

    protected final Map<Integer, Task> tasks = new HashMap<>();
    protected final Map<Integer, Subtask> subtasks = new HashMap<>();
    protected final Map<Integer, Epic> epics = new HashMap<>();

    protected final Set<Task> prioritizedTasks = new TreeSet<>(Comparator.comparing(Task::getStartTime));

    protected final HistoryManager historyManager = Managers.getDefaultHistory();

    //get---------------------------------------------------------------------------------------------------------------
    private int generateId() {
        return ++id;
    }

    @Override
    public Task getTaskById(int id) {
        if (tasks.get(id) != null) {
            historyManager.add(tasks.get(id));
            return tasks.get(id);
        } else {
            System.out.println("Задачи с ID=" + id + " не существует");
            return null;
        }
    }

    @Override
    public Epic getEpicById(int id) {
        if (epics.get(id) != null) {
            historyManager.add(epics.get(id));
            return epics.get(id);
        } else {
            System.out.println("Эпика с ID=" + id + " не существует");
            return null;
        }
    }

    @Override
    public Subtask getSubtaskById(int id) {
        if (subtasks.get(id) != null) {
            historyManager.add(subtasks.get(id));
            return subtasks.get(id);
        } else {
            System.out.println("Подзадачи с ID=" + id + " не существует");
            return null;
        }
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

    public List<Task> getPrioritizedTasks() {
        return new ArrayList<>(prioritizedTasks);
    }

    //add-----------------------------------------------------------------------------------------------------------

    @Override
    public Task addTask(Task task) {
        if (task == null) throw new TaskIsNull("Задача не передана.");
        if (tasks.containsKey(task.getId())) throw new TaskAlreadyExistsException("Задача существует");
        if (taskIntersects(task)) throw new TaskIntersectsException("Задача пересекается по времени выполнения.");

        try {
            assigningID(task);
            tasks.put(task.getId(), task);
            addPrioritizedTasks(task);
            return task;
        } catch (TaskIntersectsException | TaskIsNull | TaskAlreadyExistsException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    @Override
    public Epic addEpic(Epic epic) {
        if (epic == null) throw new TaskIsNull("Эпик не передан.");
        if (epics.containsKey(epic.getId())) throw new TaskAlreadyExistsException("Эпик уже существует");

        try {
            assigningID(epic);
            epics.put(epic.getId(), epic);
            return epic;
        } catch (TaskIsNull | TaskAlreadyExistsException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    @Override
    public Subtask addSubtask(Subtask subtask) {
        if (subtask == null) throw new TaskIsNull("Подзадача не передана.");
        if (subtasks.containsKey(subtask.getId())) throw new TaskAlreadyExistsException("Подзадача уже существует");
        if (!epics.containsKey(subtask.getEpicID())) throw new TaskNotFound("Эпик не найден.");
        if (taskIntersects(subtask)) throw new TaskIntersectsException("Задача пересекается по времени выполнения.");

        try {
            assigningID(subtask);
            Epic epic = epics.get(subtask.getEpicID());
            subtasks.put(subtask.getId(), subtask);
            epic.addSubtaskId(subtask.getId());
            updateEpicStatus(epic);//epic.updateEpicStatus(subtasks);
            addPrioritizedTasks(subtask);
            return subtask;

        } catch (TaskIntersectsException | TaskIsNull | TaskNotFound | TaskAlreadyExistsException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    private void addPrioritizedTasks(Task task) {
        prioritizedTasks.add(task);
    }

    private void assigningID(Task task) {
        if (task.getId() == 0) {
            task.setId(generateId());
        }
    }

    //delete------------------------------------------------------------------------------------------------------------
    @Override
    public boolean deleteTaskById(int id) {
        if (!tasks.containsKey(id)) throw new TaskNotFound("Задача не найдена.");

        try {
            deletePrioritizedTasks(tasks.get(id));
            historyManager.remove(id);
            tasks.remove(id);
            return true;
        } catch (TaskNotFound e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    @Override
    public boolean deleteEpicById(int id) {
        Epic epic = epics.get(id);
        if (epic == null) throw new TaskNotFound("Эпик не найден");

        try {
            epic.getSubtasksId().forEach(this::deleteSubtaskById);//Удаляем подзадачу
            historyManager.remove(id);//Удаляем из истории
            epics.remove(id);//Удаляем эпик
            return true;
        } catch (TaskNotFound e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    @Override
    public boolean deleteSubtaskById(int id) {
        Subtask subtask = subtasks.get(id);
        if (subtask == null) throw new TaskNotFound("Подзадача не найдена");

        try {
            Epic epic = epics.get(subtask.getEpicID());
            deletePrioritizedTasks(subtasks.get(id));//удаляем из списка приоритета
            historyManager.remove(id);//Удаляем из истории
            epics.get(subtask.getEpicID()).deleteSubtaskId(subtask.getId());//Удаляем из эпика
            subtasks.remove(id);//Удаляем из менеджера
            updateEpicStatus(epic);//epic.updateEpicStatus(subtasks);
            return true;
        } catch (TaskNotFound e) {
            System.out.println(e.getMessage());
            return false;
        }
    }


    @Override
    public void deleteAllTasks() {
        tasks.forEach((key, value) -> historyManager.remove(key));
        tasks.forEach((key, value) -> prioritizedTasks.remove(value));
        tasks.clear();
    }

    @Override
    public void deleteAllEpics() {
        subtasks.forEach((key, value) -> prioritizedTasks.remove(value));
        epics.forEach((key, value) -> prioritizedTasks.remove(value));

        subtasks.forEach((key, value) -> historyManager.remove(key));
        epics.forEach((key, value) -> historyManager.remove(key));

        subtasks.clear();
        epics.clear();
    }

    @Override
    public void deleteAllSubtasks() {

        subtasks.forEach((key, value) -> historyManager.remove(key));
        subtasks.forEach((key, value) -> prioritizedTasks.remove(value));

        epics.forEach((key, value) -> value.deleteAllSubtasks());

        subtasks.clear();
    }

    @Override
    public void deleteAllSubtasksByEpic(Epic epic) {

        if (epic == null) throw new TaskIsNull("Эпик не передан");

        try {
            epics.get(epic.getId()).getSubtasksId().forEach(subtasks::remove);
            epic.deleteAllSubtasks();
        } catch (TaskIsNull e) {
            System.out.println(e.getMessage());
        }
    }

    private void deletePrioritizedTasks(Task task) {
        prioritizedTasks.remove(task);
    }

    private boolean taskIntersects(Task task) {
        for (Task prioritizedTask : prioritizedTasks) {
            if (prioritizedTask.getId() != task.getId() &
                    (!task.getEndTime().isAfter(prioritizedTask.getStartTime()) &&
                            !task.getStartTime().isBefore(prioritizedTask.getEndTime()))) {
                return true;
            }
        }
        return false;
    }

    //update------------------------------------------------------------------------------------------------------------

    @Override
    public void updateTask(Task task) {
        if (task == null) throw new TaskIsNull("Подзадача не передана.");
        if (!tasks.containsKey(task.getId())) throw new TaskNotFound("Подзадача не найдена.");

        try {
            tasks.put(task.getId(), task);
        } catch (TaskNotFound | TaskIsNull e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void updateEpic(Epic epic) {
        if (epic == null) throw new TaskIsNull("Эпик не передан.");
        if (!tasks.containsKey(epic.getId())) throw new TaskNotFound("Эпик не найден.");

        try {
            epics.put(epic.getId(), epic);
            updateEpicStatus(epic);
        } catch (TaskNotFound | TaskIsNull e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        if (subtask == null) throw new TaskIsNull("Подзадача не передана.");
        if (!subtasks.containsKey(subtask.getId())) throw new TaskNotFound("Подзадача не найдена.");
        if (taskIntersects(subtask)) throw new TaskIntersectsException("Подзадача пересекается по времени выполнения.");

        try {
            subtasks.put(subtask.getId(), subtask);
            updateEpic(epics.get(subtask.getEpicID()));
        } catch (TaskIntersectsException | TaskIsNull | TaskNotFound e) {
            System.out.println(e.getMessage());
        }
    }

    private void updateEpicStatus(Epic epic) {
        epicUpdateStatus(epic);
        epicUpdateEndTime(epic);
    }

    private void epicUpdateStatus(Epic epic) {
        if (epic.getSubtasksId().size() == 0) {
            epic.setStatus(Status.NEW);
        } else {
            int countDone = 0;
            int countNew = 0;


            Status subTaskStatus;
            for (int subTaskId : epic.getSubtasksId()) {
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

            if (countDone == epic.getSubtasksId().size()) {
                epic.setStatus(Status.DONE);
            } else if (countNew == epic.getSubtasksId().size()) {
                epic.setStatus(Status.NEW);
            } else {
                epic.setStatus(Status.IN_PROGRESS);
            }
        }
    }


    private void epicUpdateEndTime(Epic epic) {

        if (epic.getSubtasksId().size() > 0) {
            Instant startTime = subtasks.get(epic.getSubtasksId().get(0)).getStartTime();
            Instant endTime = subtasks.get(epic.getSubtasksId().get(0)).getEndTime();

            for (int id : epic.getSubtasksId()) {

                Task subtask = subtasks.get(id);

                if (subtask.getStartTime().isBefore(startTime)) {
                    startTime = subtask.getStartTime();
                }
                if (subtask.getEndTime().isAfter(endTime)) {
                    endTime = subtask.getEndTime();
                }
            }

            epic.setStartTime(startTime);
            epic.setEndTime(endTime);
            epic.setDurationSecond(Duration.between(startTime, endTime).toMinutes());

        }
    }


    //------------------------------------------------------------------------------------------------------------------
    @Override
    public String toString() {
        return "Manager{" +
                "tasks=" + tasks +
                ", subtasks=" + subtasks +
                ", epics=" + epics +
                '}';
    }

}