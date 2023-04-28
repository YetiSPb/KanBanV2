package main.tasks;

import main.tasks.status.Status;
import main.tasks.status.TaskType;

import java.time.Instant;
import java.util.Objects;

public class Subtask extends Task {
    private final int epicID;

    //Конструктор при новой задаче
    public Subtask(String name, String description, Instant startTime, long durationSecond, int epicID) {
        super(name, description, startTime, durationSecond);
        this.epicID = epicID;
    }

    //Конструктор при загружаемой задаче
    public Subtask(int id, String name, String description, Instant startTime, long durationSecond, Status status, int epicId) {
        super(id, name, description, startTime, durationSecond, status);
        this.epicID = epicId;
    }

    //Геттеры-----------------------------------------------------------------------------------------------------------
    public int getEpicID() {
        return epicID;
    }

    @Override
    public TaskType getTaskType() {
        return TaskType.SUBTASK;
    }

    //------------------------------------------------------------------------------------------------------------------

    @Override
    public String toString() {

        return super.getId() + ","
                + TaskType.SUBTASK + ","
                + super.getName() + ","
                + super.getStatus() + ","
                + super.getDescription() + ","
                + super.getStartTime() + ","
                + super.getDurationSecond() + ","
                + super.getEndTime() + ","
                + epicID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Subtask subtask = (Subtask) o;
        return epicID == subtask.epicID;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), epicID);
    }


}