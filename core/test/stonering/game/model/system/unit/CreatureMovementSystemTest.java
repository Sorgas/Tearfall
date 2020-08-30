package stonering.game.model.system.unit;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import stonering.entity.job.Task;
import stonering.entity.job.action.MoveAction;
import stonering.entity.unit.Unit;
import stonering.entity.unit.aspects.MovementAspect;
import stonering.entity.unit.aspects.TaskAspect;
import stonering.enums.blocks.BlockTypeEnum;
import stonering.enums.unit.race.CreatureType;
import stonering.game.GameMvc;
import stonering.game.model.MainGameModel;
import stonering.game.model.local_map.LocalMap;
import stonering.game.model.system.task.TaskContainer;
import stonering.util.geometry.Position;
import stonering.util.pathfinding.AStar;

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
    public TaskAspect taskAspect;

    @BeforeAll
    public void prepare() {
        createModel();
        createUnit();
        createTask();
    }

    @Test
    public void testPathCreationForNewTask() {
        system.update(unit);
        assertEquals(taskAspect.getTarget(), movementAspect.target); // movement took target from planning
        assertNotNull(movementAspect.path); // path has been created
    }

    @Test
    public void testNewTarget() {
        system.update(unit);
        Position newTarget = new Position(0, 4, 0);
        taskAspect.task.addFirstPreAction(new MoveAction(newTarget));
        assertEquals(taskAspect.getTarget(), newTarget); // new target taken by planning
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
                localMap.blockType.setBlock(x, y, 0, BlockTypeEnum.FLOOR, 1);
            }
        }
        GameMvc.createInstance(new MainGameModel(localMap));
        GameMvc.model().put(unitContainer = new UnitContainer());
        GameMvc.model().put(taskContainer = new TaskContainer());
        GameMvc.model().put(new AStar());
        localMap.initAreas();
        system = new CreatureMovementSystem();
    }

    private void createUnit() {
        CreatureType type = new CreatureType();
        type.title = "test_unit";
        unit = new Unit(new Position(0, 0, 0), type);
        unit.add(movementAspect = new MovementAspect(unit));
        unit.add(taskAspect = new TaskAspect(unit));
        GameMvc.model().get(UnitContainer.class).addUnit(unit);
    }

    private void createTask() {
        MoveAction action = new MoveAction(new Position(4, 4, 0));
        Task task = new Task(action);
        taskAspect.task = task;
        taskAspect.actionChecked = false;
    }
}
