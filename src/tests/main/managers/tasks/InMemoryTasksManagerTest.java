package main.managers.tasks;

import main.tasks.Epic;
import main.tasks.Task;
import main.tasks.status.Status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTasksManagerTest extends TaskManagerTest<InMemoryTasksManager> {
    @BeforeEach
    public void setUp() {
        manager = new InMemoryTasksManager();
    }

}