package test.stonering.entity.job.action.target;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mock;
import stonering.entity.Entity;
import stonering.entity.job.Task;
import stonering.entity.job.action.Action;
import stonering.entity.job.action.target.ActionTarget;
import stonering.entity.job.action.target.EntityActionTarget;
import stonering.entity.unit.Unit;
import stonering.game.GameMvc;
import stonering.game.model.GameModel;
import stonering.game.model.local_map.LocalMap;
import stonering.util.geometry.Position;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * @author Alexander on 26.09.2019.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ActionTargetTest {
    private ActionTarget actionTarget;
    private Entity entityMock;
    @Mock
    LocalMap localMapMock;
    @Mock
    GameModel gameModelMock;
    @Mock
    Action actionMock;
    @Mock
    Task taskMock;

    private Position targetPosition = new Position(0, 0, 0);


    @BeforeAll
    void prepare() {
        initMocks(this);
        entityMock = new Unit(targetPosition, null);
        GameMvc.createInstance(gameModelMock);
        doReturn(localMapMock).when(gameModelMock).get(LocalMap.class);
        doReturn(taskMock).when(actionMock).getTask();
    }

    private void prepareList() {
        List<Position> nearPositions = new ArrayList<>();
        nearPositions.add(new Position(1, 0, 0));
        nearPositions.add(new Position(0, 1, 0));
        nearPositions.add(new Position(1, 1, 0));
        doReturn(nearPositions).when(localMapMock).getFreeBlockNear(targetPosition);
    }

    private void prepareEmptyList() {
        doReturn(new ArrayList<>()).when(localMapMock).getFreeBlockNear(targetPosition);
    }

    @Test
    void testCheckingExactTarget() {
        actionTarget = new EntityActionTarget(entityMock, ActionTarget.EXACT);
        actionTarget.setAction(actionMock);
        assertEquals(ActionTarget.READY, actionTarget.check(targetPosition));
        assertEquals(ActionTarget.WAIT, actionTarget.check(new Position(1, 0, 0)));
        assertEquals(ActionTarget.WAIT, actionTarget.check(new Position(0, 1, 0)));
        assertEquals(ActionTarget.WAIT, actionTarget.check(new Position(4, 4, 0)));
    }

    @Test
    void testCheckingNearTarget() {
        actionTarget = new EntityActionTarget(entityMock, ActionTarget.NEAR);
        actionTarget.setAction(actionMock);
        prepareList();
        assertEquals(ActionTarget.NEW, actionTarget.check(targetPosition));
        prepareEmptyList();
        assertEquals(ActionTarget.FAIL, actionTarget.check(targetPosition));
        assertEquals(ActionTarget.READY, actionTarget.check(new Position(1, 0, 0)));
        assertEquals(ActionTarget.READY, actionTarget.check(new Position(0, 1, 0)));
        assertEquals(ActionTarget.WAIT, actionTarget.check(new Position(3, 3, 0)));
    }

    @Test
    void testCheckingAnyTarget() {
        actionTarget = new EntityActionTarget(entityMock, ActionTarget.ANY);
        actionTarget.setAction(actionMock);
        assertEquals(ActionTarget.READY, actionTarget.check(targetPosition));
        assertEquals(ActionTarget.READY, actionTarget.check(new Position(1, 0, 1)));
        assertEquals(ActionTarget.READY, actionTarget.check(new Position(0, 1, 1)));
        assertEquals(ActionTarget.WAIT, actionTarget.check(new Position(3, 3, 1)));
    }
}
