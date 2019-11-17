package test.stonering.entity.job.action.target;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import stonering.entity.job.Task;
import stonering.entity.job.action.Action;
import stonering.entity.job.action.MoveAction;
import stonering.entity.job.action.target.ActionTarget;
import stonering.entity.job.action.target.ActionTargetStatusEnum;
import stonering.entity.job.action.target.PositionActionTarget;
import stonering.enums.action.ActionTargetTypeEnum;
import stonering.enums.blocks.BlockTypesEnum;
import stonering.game.GameMvc;
import stonering.game.model.GameModel;
import stonering.game.model.MainGameModel;
import stonering.game.model.local_map.LocalMap;
import stonering.util.geometry.Position;
import stonering.util.pathfinding.a_star.AStar;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Alexander on 26.09.2019.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ActionTargetTest {
    private ActionTarget actionTarget;
    private LocalMap localMap;
    private GameModel gameModel;
    private Action actionMock;

    private Position targetPosition = new Position(0, 0, 0);

    @BeforeEach
    void prepare() {
        createGameModel();
        actionMock = new MoveAction(targetPosition);
    }

    private void createGameModel() {
        createLocalMap();
        gameModel = new MainGameModel(localMap);
        gameModel.put(new AStar());
        GameMvc.createInstance(gameModel);
        localMap.initAreas();
    }

    private void createLocalMap() {
        localMap = new LocalMap(5, 5, 3);
        for (int x = 0; x < 5; x++) {
            for (int y = 0; y < 5; y++) {
                localMap.setBlock(x, y, 0, BlockTypesEnum.FLOOR, 1);
            }
        }
    }

    @Test
    void testExactTarget() {
        actionTarget = new PositionActionTarget(targetPosition, ActionTargetTypeEnum.EXACT);
        assertEquals(ActionTargetStatusEnum.READY, actionTarget.check(targetPosition));
        assertEquals(ActionTargetStatusEnum.WAIT, actionTarget.check(new Position(1, 0, 0)));
        assertEquals(ActionTargetStatusEnum.WAIT, actionTarget.check(new Position(0, 1, 0)));
        assertEquals(ActionTargetStatusEnum.WAIT, actionTarget.check(new Position(4, 4, 0)));
    }

    @Test
    void testNearTargetSuccess() {
        actionTarget = new PositionActionTarget(targetPosition, ActionTargetTypeEnum.NEAR);
        Action action = new MoveAction(new Position());
        Task task = new Task("test_task", action, 1);
        actionTarget.setAction(action);
        assertEquals(ActionTargetStatusEnum.NEW, actionTarget.check(targetPosition)); // new action should be created when checking from same position
        assertEquals(ActionTargetStatusEnum.READY, actionTarget.check(new Position(1, 0, 0)));
        assertEquals(ActionTargetStatusEnum.READY, actionTarget.check(new Position(0, 1, 0)));
        assertEquals(ActionTargetStatusEnum.WAIT, actionTarget.check(new Position(3, 3, 0)));
    }

    @Test
    void testNearTargetFailInWalls() {
        actionTarget = new PositionActionTarget(targetPosition, ActionTargetTypeEnum.NEAR);
        //surround target position with impassable walls
        localMap.setBlock(0, 1, 0, BlockTypesEnum.WALL, 1);
        localMap.setBlock(1, 0, 0, BlockTypesEnum.WALL, 1);
        localMap.setBlock(1, 1, 0, BlockTypesEnum.WALL, 1);
        assertEquals(ActionTargetStatusEnum.FAIL, actionTarget.check(targetPosition));
    }

    @Test
    void testAnyTarget() {
        actionTarget = new PositionActionTarget(targetPosition, ActionTargetTypeEnum.ANY);
        actionTarget.setAction(actionMock);
        assertEquals(ActionTargetStatusEnum.READY, actionTarget.check(targetPosition));
        assertEquals(ActionTargetStatusEnum.READY, actionTarget.check(new Position(1, 0, 1)));
        assertEquals(ActionTargetStatusEnum.READY, actionTarget.check(new Position(0, 1, 1)));
        assertEquals(ActionTargetStatusEnum.WAIT, actionTarget.check(new Position(3, 3, 1)));
    }

    void testOutOfMap() {
        actionTarget = new PositionActionTarget(targetPosition, ActionTargetTypeEnum.ANY);
        assertEquals(ActionTargetStatusEnum.FAIL, actionTarget.check(new Position(-1, -1, 0)));
    }
}
