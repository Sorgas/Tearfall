package test.stonering.game.model.system.unit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import stonering.entity.job.Task;
import stonering.entity.job.action.Action;
import stonering.entity.job.action.MoveAction;
import stonering.entity.unit.Unit;
import stonering.entity.unit.aspects.JobsAspect;
import stonering.entity.unit.aspects.PlanningAspect;
import stonering.enums.blocks.BlockTypesEnum;
import stonering.enums.unit.CreatureType;
import stonering.game.GameMvc;
import stonering.game.model.GameModel;
import stonering.game.model.MainGameModel;
import stonering.game.model.local_map.LocalMap;
import stonering.game.model.system.task.TaskContainer;
import stonering.game.model.system.unit.UnitContainer;
import stonering.util.geometry.Position;

import static org.junit.jupiter.api.Assertions.*;
import static stonering.enums.TaskStatusEnum.*;

/**
 * @author Alexander on 01.11.2019.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CreatureTaskPerformingTest {
    private Unit unit;
    private UnitContainer unitContainer;
    private TaskContainer taskContainer;
    private PlanningAspect aspect;

    @BeforeEach
    void prepare() {
        LocalMap map = new LocalMap(5, 5, 1);
        for (int x = 0; x < 5; x++) {
            for (int y = 0; y < 5; y++) {
                map.setBlock(x, y, 0, BlockTypesEnum.FLOOR, 1);
            }
        }
        GameModel model = new MainGameModel(map);
        GameMvc.createInstance(model);
        model.put(unitContainer = new UnitContainer());
        model.put(taskContainer = new TaskContainer());
        unit = new Unit(new Position(), new CreatureType());
        unit.addAspect(aspect = new PlanningAspect(unit));
        unit.addAspect(new JobsAspect(unit));
        model.get(UnitContainer.class).addUnit(unit);
        map.initAreas();
    }

    @Test
    void testNothingOnWait() {
        Action action = new MoveAction(new Position(4, 4, 0));
        Task task = new Task("test_task", action, 1);
        aspect.task = task;
        task.status = ACTIVE;
        task.performer = unit;
        aspect.movementNeeded = false;
        unitContainer.taskSystem.update(unit); // target is reachable
        assertTrue(aspect.movementNeeded);
    }

    @Test
    void testFailUnreachableTarget() {
        Action action = new MoveAction(new Position(5, 4, 0)); // out of map
        Task task = new Task("test_task", action, 1);
        aspect.task = task;
        task.status = ACTIVE;
        task.performer = unit;
        unitContainer.taskSystem.update(unit); // target is not reachable
        assertEquals(FAILED, task.status);
    }

    @Test
    void testPerformingAction() {
        Action action = new MoveAction(new Position(0, 0, 0));
        Task task = new Task("test_task", action, 1);
        task.performer = unit;
        task.status = ACTIVE;
        aspect.task = task;
        unitContainer.taskSystem.update(unit); // ok check before performing
        assertTrue(aspect.actionChecked);
        float oldWorkAmount = task.nextAction.workAmount;
        unitContainer.taskSystem.update(unit);
        assertNotEquals(oldWorkAmount, task.nextAction.workAmount);
    }

    /**
     * Actions are checked by system right before performing,
     * and when unit starts to move to next action(after finishing previous one).
     */
    @Test
    void testCheckActionsBeforeAndAfterPerforming() {
        Action action = new MoveAction(new Position(0, 0, 0));
        Task task = new Task("test_task", action, 1);
        task.addFirstPostAction(new MoveAction(new Position(4, 4, 0)));
        task.nextAction.workAmount = 0.01f; // to finish action in 1 perform()
        task.nextAction.baseSpeed = 0.02f;
        task.performer = unit;
        task.status = ACTIVE;
        aspect.task = task;
        unitContainer.taskSystem.update(unit); // ok check before performing
        assertTrue(aspect.actionChecked);
        unitContainer.taskSystem.update(unit); // perform and finish first action
        assertNotEquals(action, task.nextAction);
        assertFalse(aspect.actionChecked);
    }
}
