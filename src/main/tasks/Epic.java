package main.tasks;

import main.tasks.status.Status;
import main.tasks.status.TaskType;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Epic extends Task {
    private final List<Integer> subTasksId = new ArrayList<>();
    private Instant endTime = Instant.ofEpochSecond(0);

    //Конструктор при новой задаче
    public Epic(String name, String description) {

        super(name, description, Instant.ofEpochSecond(0), 0);
    }

    //Конструктор при загружаемом эпике
    public Epic(int id, String name, String description, Instant startTime, long durationSecond, Status status) {

        super(id, name, description, startTime, durationSecond, status);
        this.endTime = super.getEndTime();
    }

    //Геттеры-----------------------------------------------------------------------------------------------------------
    @Override
    public Instant getEndTime() {
        return endTime;
    }

    public List<Integer> getSubtasksId() {
        return subTasksId;
    }

    @Override
    public TaskType getTaskType() {
        return TaskType.EPIC;
    }

    //Сеттеры-----------------------------------------------------------------------------------------------------------

    public void setEndTime(Instant endTime) {
        this.endTime = endTime;
    }


    //------------------------------------------------------------------------------------------------------------------

    public void addSubtaskId(int id) {
        subTasksId.add(id);
    }

    public void deleteSubtaskId(int id) {
        subTasksId.removeIf(subtaskID -> subtaskID == id);
    }

    public void updateEpicStatus(Map<Integer, Subtask> subtasks) {

        var startTime = subtasks.get(subTasksId.get(0)).getStartTime();
        var endTime = subtasks.get(subTasksId.get(0)).getEndTime();

        int isNew = 0;
        int isDone = 0;

        for (int id : getSubtasksId()) {

            Task subtask = subtasks.get(id);

            if (subtask.getStatus() == Status.NEW)
                isNew += 1;

            if (subtask.getStatus() == Status.DONE)
                isDone += 1;

            if (subtask.getStartTime().isBefore(startTime)) {
                startTime = subtask.getStartTime();
            }

            if (subtask.getEndTime().isAfter(endTime)) {
                endTime = subtask.getEndTime();
            }

        }

        this.setStartTime(startTime);
        this.endTime = endTime;
        this.setDurationSecond(Duration.between(startTime, endTime).toMinutes());

        if (subtasks.size() == isNew - 1) {
            setStatus(Status.NEW);
        } else if (subtasks.size() == isDone - 1) {
            setStatus(Status.DONE);
        } else {
            setStatus(Status.IN_PROGRESS);
        }

    }

    //------------------------------------------------------------------------------------------------------------------

    @Override
    public String toString() {

        return super.getId() + ","
                + TaskType.EPIC + ","
                + super.getName() + ","
                + super.getStatus() + ","
                + super.getDescription() + ","
                + super.getStartTime() + ","
                + super.getDurationSecond() + ","
                + super.getEndTime();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Epic epic = (Epic) o;
        return Objects.equals(subTasksId, epic.subTasksId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), subTasksId);
    }


}