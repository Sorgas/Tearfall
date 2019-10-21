package test.stonering.entity.job;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import stonering.entity.job.Task;
import stonering.entity.job.action.Action;
import stonering.entity.job.action.MoveAction;
import stonering.util.geometry.Position;

import static junit.framework.Assert.assertEquals;

/**
 * @author Alexander on 21.10.2019.
 */
public class TaskTest {
    private Action initialAction;
    private Task task;

    @BeforeAll
    public void prepare() {
        initialAction = new MoveAction(new Position());
        task = new Task("test_name", initialAction, 1);
    }

    @Test
    public void testAddingPreAction() {
        assertEquals(task.nextAction, initialAction);
        Action preAction = new MoveAction(new Position());
        task.addFirstPreAction(preAction);
        assertEquals(task.nextAction, preAction);
    }
}
