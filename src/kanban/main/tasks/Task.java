package main.tasks;

import main.tasks.status.Status;
import main.tasks.status.TaskType;

import java.time.Instant;
import java.util.Objects;

public class Task  {
    private int id;
    private String name;
    private String description;
    private Instant startTime;
    private long durationSecond;
    private Status status;

    private final byte SECONDS_IN_ONE_MINUTE = 60;

    //Конструктор при новой задаче
    public Task(String name, String description, Instant startTime, long durationSecond) {
        this.name = name;
        this.description = description;
        this.status = Status.NEW;
        this.startTime = startTime;
        this.durationSecond = durationSecond;
    }

    //Конструктор при загружаемой задаче
    public Task(int id, String name, String description, Instant startTime, long durationSecond, Status status) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = status;
        this.startTime = startTime;
        this.durationSecond = durationSecond;
    }

    //Геттеры-----------------------------------------------------------------------------------------------------------

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Instant getStartTime() {
        return startTime;
    }

    public long getDurationSecond() {
        return durationSecond;
    }

    public Instant getEndTime() {

        return startTime.plusSeconds(durationSecond * SECONDS_IN_ONE_MINUTE);
    }

    public Status getStatus() {
        return status;
    }

    public TaskType getTaskType() {
        return TaskType.TASK;
    }

    //Сеттеры-----------------------------------------------------------------------------------------------------------

    public void setId(int id) {
        this.id = id;
    }

    public void setStartTime(Instant startTime) {
        this.startTime = startTime;
    }

    public void setDurationSecond(long durationSecond) {
        this.durationSecond = durationSecond;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    //----------------------------------------------------------------------------

    @Override
    public String toString() {

        return id + ","
                + TaskType.TASK + ","
                + name + ","
                + status + ","
                + description + ","
                + getStartTime() + ","
                + durationSecond + ","
                + getEndTime();
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (!(o instanceof Task)) return false;

        Task that = (Task) o;

        return Objects.equals(this.name, that.name)
                && Objects.equals(this.description, that.description)
                && Objects.equals(this.id, that.id)
                && Objects.equals(this.status, that.status);

    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, status);
    }

}