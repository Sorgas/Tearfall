package stonering.entity.job;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import stonering.entity.job.action.Action;
import stonering.entity.job.action.MoveAction;
import stonering.util.geometry.Position;

import static junit.framework.Assert.assertEquals;

/**
 * @author Alexander on 21.10.2019.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TaskTest {
    private Action initialAction;
    private Task task;

    @BeforeAll
    void prepare() {
        initialAction = new MoveAction(new Position());
        task = new Task(initialAction);
    }

    @Test
    void testAddingPreAction() {
        assertEquals(task.nextAction, initialAction);
        Action preAction = new MoveAction(new Position());
        task.addFirstPreAction(preAction);
        assertEquals(task.nextAction, preAction);
    }

    @Test
    void testAddPostAction() {
        assertEquals(task.nextAction, initialAction);
        Action postAction = new MoveAction(new Position());
        task.addFirstPostAction(postAction);
        assertEquals(task.nextAction, initialAction);
    }
}
