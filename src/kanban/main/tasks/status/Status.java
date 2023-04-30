package main.tasks.status;

import java.util.Objects;

public enum Status {
    NEW("NEW"),
    IN_PROGRESS("IN_PROGRESS"),
    DONE("DONE");

    private final String status;

    Status(String done) {
        this.status = done;
    }

    public static Status whatStatus(String statusStr) {
        if (Objects.equals(statusStr, Status.NEW.status)) {
            return Status.NEW;
        } else if (Objects.equals(statusStr, Status.IN_PROGRESS.status)) {
            return Status.IN_PROGRESS;
        } else if (Objects.equals(statusStr, Status.DONE.status)) {
            return Status.DONE;
        }
        return null;
    }

    public String getStatus() {
        return status;
    }
}


