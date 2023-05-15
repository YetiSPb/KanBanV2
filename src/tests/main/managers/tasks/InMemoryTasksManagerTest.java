package main.managers.tasks;

import main.managers.InMemoryTaskManager;
import org.junit.jupiter.api.BeforeEach;

class InMemoryTasksManagerTest extends TaskManagerTest<InMemoryTaskManager> {
    @BeforeEach
    public void setUp() {
        manager = new InMemoryTaskManager();
    }

}