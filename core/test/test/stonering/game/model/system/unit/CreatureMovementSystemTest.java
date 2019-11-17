package test.stonering.game.model.system.unit;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import stonering.entity.job.Task;
import stonering.entity.job.action.MoveAction;
import stonering.entity.unit.Unit;
import stonering.entity.unit.aspects.MovementAspect;
import stonering.entity.unit.aspects.PlanningAspect;
import stonering.enums.blocks.BlockTypesEnum;
import stonering.enums.unit.CreatureType;
import stonering.game.GameMvc;
import stonering.game.model.MainGameModel;
import stonering.game.model.local_map.LocalMap;
import stonering.game.model.system.task.TaskContainer;
import stonering.game.model.system.unit.CreatureMovementSystem;
import stonering.game.model.system.unit.UnitContainer;
import stonering.util.geometry.Position;
import stonering.util.pathfinding.a_star.AStar;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Alexander on 23.10.2019.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CreatureMovementSystemTest {
    private Unit unit;
    private CreatureMovementSystem system;
    private MovementAspect movementAspect;
    private LocalMap localMap;
    private UnitContainer unitContainer;
    private TaskContainer taskContainer;
    public PlanningAspect planningAspect;

    @BeforeAll
    public void prepare() {
        createModel();
        createUnit();
        createTask();
    }

    @Test
    public void testPathCreationForNewTask() {
        system.update(unit);
        assertEquals(planningAspect.getTarget(), movementAspect.target); // movement took target from planning
        assertNotNull(movementAspect.path); // path has been created
    }

    @Test
    public void testNewTarget() {
        system.update(unit);
        Position newTarget = new Position(0, 4, 0);
        planningAspect.task.addFirstPreAction(new MoveAction(newTarget));
        planningAspect.movementNeeded = true;
        assertEquals(planningAspect.getTarget(), newTarget); // new target taken by planning
        List<Position> oldPath = movementAspect.path;
        system.update(unit);
        assertEquals(newTarget, movementAspect.target); // movement took new target from planning
        assertNotNull(movementAspect.path); // path has been created
        assertNotEquals(movementAspect.path, oldPath); // path changed
    }

    private void createModel() {
        localMap = new LocalMap(5, 5, 1);
        for (int x = 0; x < 5; x++) {
            for (int y = 0; y < 5; y++) {
                localMap.setBlock(x, y, 0, BlockTypesEnum.FLOOR, 1);
            }
        }
        GameMvc.createInstance(new MainGameModel(localMap));
        GameMvc.instance().model().put(unitContainer = new UnitContainer());
        GameMvc.instance().model().put(taskContainer = new TaskContainer());
        GameMvc.instance().model().put(new AStar());
        localMap.initAreas();
        system = new CreatureMovementSystem();
    }

    private void createUnit() {
        CreatureType type = new CreatureType();
        type.title = "test_unit";
        unit = new Unit(new Position(0, 0, 0), type);
        unit.addAspect(movementAspect = new MovementAspect(unit));
        unit.addAspect(planningAspect = new PlanningAspect(unit));
        GameMvc.instance().model().get(UnitContainer.class).addUnit(unit);
    }

    private void createTask() {
        MoveAction action = new MoveAction(new Position(4, 4, 0));
        Task task = new Task("test_task", action, 1);
        planningAspect.task = task;
        planningAspect.movementNeeded = true;
        planningAspect.actionChecked = false;
    }
}
