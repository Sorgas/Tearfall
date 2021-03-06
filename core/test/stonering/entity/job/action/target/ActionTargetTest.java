package stonering.entity.job.action.target;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import stonering.entity.Entity;
import stonering.entity.job.action.Action;
import stonering.entity.job.action.MoveAction;
import stonering.enums.action.ActionTargetTypeEnum;
import stonering.enums.blocks.BlockTypeEnum;
import stonering.game.GameMvc;
import stonering.game.model.GameModel;
import stonering.game.model.MainGameModel;
import stonering.game.model.local_map.LocalMap;
import stonering.util.geometry.Position;
import stonering.util.pathfinding.AStar;

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
    private Entity dummy;

    private Position targetPosition = new Position(0, 0, 0);

    @BeforeEach
    void prepare() {
        createGameModel();
        actionMock = new MoveAction(targetPosition);
        dummy = new Entity(){};
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
                localMap.blockType.setBlock(x, y, 0, BlockTypeEnum.FLOOR, 1);
            }
        }
    }

    @Test
    void testExactTarget() {
        actionTarget = new PositionActionTarget(targetPosition, ActionTargetTypeEnum.EXACT);
        dummy.position = targetPosition;
        assertEquals(ActionTargetStatusEnum.READY, actionTarget.check(dummy));
        dummy.position = new Position(1, 0, 0);
        assertEquals(ActionTargetStatusEnum.WAIT, actionTarget.check(dummy));
        dummy.position = new Position(0, 1, 0);
        assertEquals(ActionTargetStatusEnum.WAIT, actionTarget.check(dummy));
        dummy.position = new Position(4, 4, 0);
        assertEquals(ActionTargetStatusEnum.WAIT, actionTarget.check(dummy));
    }

    @Test
    void testNearTargetSuccess() {
        actionTarget = new PositionActionTarget(targetPosition, ActionTargetTypeEnum.NEAR);
        Action action = new MoveAction(new Position());
        actionTarget.action = action;
        dummy.position = targetPosition;
        assertEquals(ActionTargetStatusEnum.STEP_OFF, actionTarget.check(dummy)); // new action should be created when checking from same position
        dummy.position = new Position(1, 0, 0);
        assertEquals(ActionTargetStatusEnum.READY, actionTarget.check(dummy));
        dummy.position = new Position(0, 1, 0);
        assertEquals(ActionTargetStatusEnum.READY, actionTarget.check(dummy));
        dummy.position = new Position(3, 3, 0);
        assertEquals(ActionTargetStatusEnum.WAIT, actionTarget.check(dummy));
    }

    @Test
    void testNearTargetFailInWalls() {
        actionTarget = new PositionActionTarget(targetPosition, ActionTargetTypeEnum.NEAR);
        //surround target position with impassable walls
        localMap.blockType.setBlock(0, 1, 0, BlockTypeEnum.WALL, 1);
        localMap.blockType.setBlock(1, 0, 0, BlockTypeEnum.WALL, 1);
        localMap.blockType.setBlock(1, 1, 0, BlockTypeEnum.WALL, 1);
        dummy.position = targetPosition;
        assertEquals(ActionTargetStatusEnum.FAIL, actionTarget.check(dummy));
    }

    @Test
    void testAnyTarget() {
        actionTarget = new PositionActionTarget(targetPosition, ActionTargetTypeEnum.ANY);
        actionTarget.action = actionMock;
        dummy.position = targetPosition;
        assertEquals(ActionTargetStatusEnum.READY, actionTarget.check(dummy)); // same position
        dummy.position = new Position(1, 0, 0);
        assertEquals(ActionTargetStatusEnum.READY, actionTarget.check(dummy)); // near target
        dummy.position = new Position(1, 1, 0);
        assertEquals(ActionTargetStatusEnum.READY, actionTarget.check(dummy)); // near target
        dummy.position = new Position(0, 0, 1);
        assertEquals(ActionTargetStatusEnum.WAIT, actionTarget.check(dummy)); // above target
        dummy.position = new Position(1, 0, 1);
        assertEquals(ActionTargetStatusEnum.WAIT, actionTarget.check(dummy)); // above and near target
        dummy.position = new Position(3, 3, 1);
        assertEquals(ActionTargetStatusEnum.WAIT, actionTarget.check(dummy));
    }

    void testOutOfMap() {
        actionTarget = new PositionActionTarget(targetPosition, ActionTargetTypeEnum.ANY);
        dummy.position = new Position(-1, -1, 0);
        assertEquals(ActionTargetStatusEnum.FAIL, actionTarget.check(dummy));
    }
}
