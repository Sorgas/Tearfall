package stonering.game.model.system.unit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import stonering.entity.job.Task;
import stonering.entity.job.action.MoveAction;
import stonering.entity.unit.Unit;
import stonering.entity.unit.aspects.job.JobSkillAspect;
import stonering.entity.unit.aspects.TaskAspect;
import stonering.enums.blocks.BlockTypeEnum;
import stonering.enums.unit.race.CreatureType;
import stonering.game.GameMvc;
import stonering.game.model.GameModel;
import stonering.game.model.MainGameModel;
import stonering.game.model.local_map.LocalMap;
import stonering.game.model.system.task.TaskContainer;
import stonering.util.geometry.Position;

import static stonering.enums.action.TaskStatusEnum.*;

/**
 * @author Alexander on 30.10.2019.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CreaturePlanningSystemTest {
    private Unit unit;
    private UnitContainer unitContainer;
    private TaskContainer taskContainer;
    private TaskAspect aspect;

    @BeforeEach
    void prepare() {
        LocalMap map = new LocalMap(5, 5, 1);
        for (int x = 0; x < 5; x++) {
            for (int y = 0; y < 5; y++) {
                map.blockType.setBlock(x, y, 0, BlockTypeEnum.FLOOR, 1);
            }
        }
        GameModel model = new MainGameModel(map);
        GameMvc.createInstance(model);
        model.put(unitContainer = new UnitContainer());
        model.put(taskContainer = new TaskContainer());
        unit = new Unit(new Position(), new CreatureType());
        unit.add(aspect = new TaskAspect(unit));
        unit.add(new JobSkillAspect());
        model.get(UnitContainer.class).addUnit(unit);
        map.initAreas();
    }

    @Test
    void testSelectNewTask() {
        Task task = new Task(new MoveAction(new Position(4, 4, 0)));
        taskContainer.addTask(task);
        unitContainer.planningSystem.update(unit);
        assert (aspect.task == task);
    }

    @Test
    void testCannotSelectNewTask() {
        Task task = new Task(new MoveAction(new Position(4, 4, 0)));
        task.job = "Miner"; // unit is not a miner and will not be assigned to this task
        taskContainer.addTask(task);
        unitContainer.planningSystem.update(unit);
        assert (aspect.task == null);
    }

    @Test
    void testOpenTask() {
        Task task = new Task(new MoveAction(new Position(4, 4, 0)));
        taskContainer.addTask(task);
        taskContainer.claimTask(task);
        aspect.task = task;
        assert(task.status == OPEN);
        unitContainer.planningSystem.update(unit);
        assert(task.status == ACTIVE);
    }

    @Test
    void testFailedTask() {
        Task task = new Task(new MoveAction(new Position(4, 4, 0)));
        taskContainer.addTask(task);
        taskContainer.claimTask(task);
        aspect.task = task;
        task.status = FAILED;
        unitContainer.planningSystem.update(unit);
        assert(aspect.task == null);
        assert(task.performer == null);
    }

    @Test
    void testCompleteTask() {
        Task task = new Task(new MoveAction(new Position(4, 4, 0)));
        taskContainer.addTask(task);
        taskContainer.claimTask(task);
        aspect.task = task;
        task.status = COMPLETE;
        unitContainer.planningSystem.update(unit);
        assert(aspect.task == null);
        assert(task.performer == null);
    }
}
