package main.tasks.status;

import java.util.Objects;

public enum TaskType {
    TASK("TASK"),
    EPIC("EPIC"),
    SUBTASK("SUBTASK");

    private final String taskType;

    TaskType(String done) {
        this.taskType = done;
    }

    public static TaskType whatTaskType(String taskTypeStr) {
        if (Objects.equals(taskTypeStr, TaskType.TASK.taskType)) {
            return TaskType.TASK;
        } else if (Objects.equals(taskTypeStr, TaskType.EPIC.taskType)) {
            return TaskType.EPIC;
        } else if (Objects.equals(taskTypeStr, TaskType.SUBTASK.taskType)) {
            return TaskType.SUBTASK;
        }
        return null;
    }

    public String getTaskType() {
        return taskType;
    }
}
