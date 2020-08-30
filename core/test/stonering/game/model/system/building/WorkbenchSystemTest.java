package stonering.game.model.system.building;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import stonering.entity.building.Building;
import stonering.enums.OrientationEnum;
import stonering.enums.buildings.BuildingType;
import stonering.entity.building.aspects.WorkbenchAspect;
import stonering.entity.crafting.ItemOrder;
import stonering.entity.job.action.item.CraftItemAction;
import stonering.entity.unit.Unit;
import stonering.entity.unit.aspects.job.JobsAspect;
import stonering.entity.unit.aspects.TaskAspect;
import stonering.enums.action.TaskStatusEnum;
import stonering.enums.blocks.BlockTypeEnum;
import stonering.enums.items.ItemTagEnum;
import stonering.enums.items.recipe.Ingredient;
import stonering.enums.items.recipe.RawRecipe;
import stonering.enums.items.recipe.Recipe;
import stonering.enums.unit.race.CreatureType;
import stonering.game.GameMvc;
import stonering.game.model.GameModel;
import stonering.game.model.MainGameModel;
import stonering.game.model.local_map.LocalMap;
import stonering.game.model.system.task.TaskContainer;
import stonering.game.model.system.unit.UnitContainer;
import stonering.util.geometry.Position;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static stonering.enums.OrderStatusEnum.*;

/**
 * @author Alexander on 04.11.2019.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class WorkbenchSystemTest {
    private Unit unit;
    private UnitContainer unitContainer;
    private BuildingContainer buildingContainer;
    private TaskContainer taskContainer;
    private WorkbenchAspect aspect;
    private WorkbenchSystem system;
    private Building workbench;

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
        model.put(buildingContainer = new BuildingContainer());
        system = buildingContainer.workbenchSystem;
        createEntities();
        map.initAreas();
    }

    private void createEntities() {
        unit = new Unit(new Position(), new CreatureType());
        unit.add(new TaskAspect(unit));
        unit.add(new JobsAspect(unit));
        GameMvc.model().get(UnitContainer.class).addUnit(unit);
        
        workbench = new Building(new Position(), new BuildingType(), OrientationEnum.N);
        workbench.add(aspect = new WorkbenchAspect(workbench));
        aspect.recipes.add(createRecipe());
    }

    private Recipe createRecipe() {
        RawRecipe raw = new RawRecipe();
        raw.name = "make_axe";
        raw.itemName = "axe";
        Recipe recipe = new Recipe(raw);
        recipe.ingredients.put("head", new Ingredient("head", Arrays.asList("bar"), ItemTagEnum.METAL, 1));
        return recipe;
    }

    @Test
    void testOrderCreation() {
        assert(aspect.orders.isEmpty());
        ItemOrder order = new ItemOrder(aspect.recipes.get(0));
        system.addOrder(aspect, order);
        assertEquals(order, aspect.orders.get(0)); // order is added
        assertEquals(1, aspect.orders.size()); // order is single
        assertEquals(OPEN, order.status); // order is open
        assertNull(aspect.currentTask); // task not created
    }

//    @Test
//    void failCreateOrderWithWrongRecipe() {
//        assert(aspect.orders.isEmpty());
//        ItemOrder order = new ItemOrder(new Recipe("qwer")); // invalid recipe
//        system.addOrder(aspect, order);
//        assert(aspect.orders.isEmpty()); // order not added
//    }

    @Test
    void testTaskCreation() {
        assert(aspect.orders.isEmpty());
        ItemOrder order = new ItemOrder(aspect.recipes.get(0));
        system.addOrder(aspect, order);
        assertEquals(order, aspect.orders.get(0)); // order added
        system.update(workbench);
        assertEquals(ACTIVE, order.status); // order became active
        assertNotNull(aspect.currentTask); // task is created
        assertEquals(TaskStatusEnum.OPEN, aspect.currentTask.status); // task is open
        assert(aspect.currentTask.nextAction instanceof CraftItemAction); // task is for crafting
    }

    @Test
    void testRemoveOpenOrder() {
        ItemOrder order = new ItemOrder(aspect.recipes.get(0));
        system.addOrder(aspect, order);
        assertEquals(order, aspect.orders.get(0)); // order is added
        system.removeOrder(aspect, order);
        assert(aspect.orders.isEmpty()); // order is removed
    }

    @Test
    void testRemoveActiveOrder() {
        ItemOrder order = new ItemOrder(aspect.recipes.get(0));
        system.addOrder(aspect, order);
        assertEquals(order, aspect.orders.get(0)); // order is added
        system.update(workbench);
        assertNotNull(aspect.currentTask); // task is created
        system.removeOrder(aspect, order);
        assertNull(aspect.currentTask); // task is removed
        assert(aspect.orders.isEmpty()); // order is removed
    }

    @Test
    void testSetOpenOrderSuspended() {
        ItemOrder order = new ItemOrder(aspect.recipes.get(0));
        system.addOrder(aspect, order);
        assertEquals(order, aspect.orders.get(0));
        system.setOrderSuspended(aspect, order, true);
        assertEquals(SUSPENDED, aspect.orders.get(0).status);
        system.setOrderSuspended(aspect, order, false);
        assertEquals(OPEN, aspect.orders.get(0).status);
    }

    @Test
    void testSetActiveOrderSuspended() {
        ItemOrder order = new ItemOrder(aspect.recipes.get(0));
        system.addOrder(aspect, order);
        assertEquals(order, aspect.orders.get(0)); // order added
        system.update(workbench);
        assertNotNull(aspect.currentTask); // task is created
        system.setOrderSuspended(aspect, order, true);
        assertEquals(SUSPENDED, aspect.orders.get(0).status);
        assertNull(aspect.currentTask); // task is removed
        assert(!aspect.orders.isEmpty()); // order not removed
        system.setOrderSuspended(aspect, order, false);
        assertEquals(OPEN, aspect.orders.get(0).status);
        assertNull(aspect.currentTask); // task is not created
    }

    @Test
    void testSetOrderRepeated() {
        ItemOrder order = new ItemOrder(aspect.recipes.get(0));
        system.addOrder(aspect, order);
        assertEquals(order, aspect.orders.get(0));
        system.setOrderRepeated(aspect, order, true);
        assert(order.repeated);
        system.setOrderRepeated(aspect, order, false);
        assert(!order.repeated);
    }

    @Test
    void testHandleActiveOrder() {
        ItemOrder order = new ItemOrder(aspect.recipes.get(0));
        system.addOrder(aspect, order);
        system.update(workbench);
        assertEquals(ACTIVE, order.status); // order became active
        assertNotNull(aspect.currentTask); // task is created
        aspect.currentTask.status = TaskStatusEnum.COMPLETE;
        system.update(workbench);
        assertEquals(COMPLETE, order.status); // order set to complete

        order.status = OPEN;
        system.addOrder(aspect, order);
        system.update(workbench);
        aspect.currentTask.status = TaskStatusEnum.FAILED;
        system.update(workbench);
        assertEquals(FAILED, order.status); // order set to failed
    }

    @Test
    void testHandleCompleteOrder() {
        ItemOrder order = new ItemOrder(aspect.recipes.get(0));
        system.addOrder(aspect, order);
        order.status = COMPLETE;
        system.update(workbench);
        assert(aspect.orders.isEmpty());
    }

    @Test
    void testHandleCompleteRepeatedOrder() {
        ItemOrder order = new ItemOrder(aspect.recipes.get(0));
        system.addOrder(aspect, order);
        order.status = COMPLETE;
        order.repeated = true;
        system.update(workbench);
        assert(!aspect.orders.isEmpty());
        assertEquals(OPEN, order.status); // order reopened
    }

    @Test
    void testHandleFailedOrder() {
        ItemOrder order = new ItemOrder(aspect.recipes.get(0));
        system.addOrder(aspect, order);
        order.status = FAILED;
        system.update(workbench);
        assertEquals(SUSPENDED, order.status); // order suspended
    }

    @Test
    void testHandleFailedRepeatedOrder() {
        ItemOrder order = new ItemOrder(aspect.recipes.get(0));
        system.addOrder(aspect, order);
        order.status = FAILED;
        order.repeated = true;
        system.update(workbench);
        assertEquals(SUSPENDED, order.status); // order suspended
    }
}
