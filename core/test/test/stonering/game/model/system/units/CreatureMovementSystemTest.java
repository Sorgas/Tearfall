package test.stonering.game.model.system.units;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mock;
import stonering.entity.job.Task;
import stonering.entity.job.action.MoveAction;
import stonering.entity.unit.Unit;
import stonering.entity.unit.aspects.MovementAspect;
import stonering.entity.unit.aspects.PlanningAspect;
import stonering.enums.blocks.BlockTypesEnum;
import stonering.game.GameMvc;
import stonering.game.model.MainGameModel;
import stonering.game.model.local_map.LocalMap;
import stonering.game.model.system.units.CreatureMovementSystem;
import stonering.game.model.system.units.UnitContainer;
import stonering.util.geometry.Position;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;

/**
 * @author Alexander on 23.10.2019.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CreatureMovementSystemTest {
    private Unit unit;
    private CreatureMovementSystem system;
    private MovementAspect movementAspect;
    private LocalMap localMap;
    private UnitContainer container;
    public PlanningAspect planningAspect;

    @BeforeAll
    public void prepare() {
        createModel();
        createUnit();
        createTask();
    }

    public void createModel() {
        localMap = new LocalMap(5, 5, 1);
        for (int x = 0; x < 5; x++) {
            for (int y = 0; y < 5; y++) {
                localMap.setBlock(x, y, 0, BlockTypesEnum.FLOOR, 1);
            }
        }
        GameMvc.createInstance(new MainGameModel(localMap));
        GameMvc.instance().getModel().put(container = new UnitContainer());
        system = new CreatureMovementSystem();
    }

    public void createUnit() {
        unit = new Unit(new Position(0, 0, 0), null);
        unit.addAspect(movementAspect = new MovementAspect(unit));
        unit.addAspect(planningAspect = new PlanningAspect(unit));
        GameMvc.instance().getModel().get(UnitContainer.class).addUnit(unit);
    }

    public void createTask() {
        MoveAction action = new MoveAction(new Position(4,4,0));
        Task task = new Task("test_task", action, 1);
        planningAspect.task = task;
    }

    @Test
    public void testNewAction() {
        assertNull(movementAspect.target);
        assertNull(movementAspect.path);
        planningAspect.turn();
        system.updateUnitPosition(unit);
        assertEquals(movementAspect.target, planningAspect.getTarget());
        assertNotNull(movementAspect.path);
    }
}
