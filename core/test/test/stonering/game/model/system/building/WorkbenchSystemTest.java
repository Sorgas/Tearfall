package test.stonering.game.model.system.building;

import com.badlogic.gdx.scenes.scene2d.ui.Button;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import stonering.entity.building.Building;
import stonering.entity.building.BuildingType;
import stonering.entity.building.aspects.WorkbenchAspect;
import stonering.entity.crafting.ItemOrder;
import stonering.entity.unit.Unit;
import stonering.entity.unit.aspects.JobsAspect;
import stonering.entity.unit.aspects.PlanningAspect;
import stonering.enums.blocks.BlockTypesEnum;
import stonering.enums.items.recipe.Ingredient;
import stonering.enums.items.recipe.RawRecipe;
import stonering.enums.items.recipe.Recipe;
import stonering.enums.unit.CreatureType;
import stonering.game.GameMvc;
import stonering.game.model.GameModel;
import stonering.game.model.MainGameModel;
import stonering.game.model.local_map.LocalMap;
import stonering.game.model.system.building.BuildingContainer;
import stonering.game.model.system.building.WorkbenchSystem;
import stonering.game.model.system.task.TaskContainer;
import stonering.game.model.system.unit.UnitContainer;
import stonering.util.geometry.Position;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static stonering.enums.OrderStatusEnum.OPEN;
import static stonering.enums.OrderStatusEnum.SUSPENDED;

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
                map.setBlock(x, y, 0, BlockTypesEnum.FLOOR, 1);
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
        unit.addAspect(new PlanningAspect(unit));
        unit.addAspect(new JobsAspect(unit));
        GameMvc.instance().getModel().get(UnitContainer.class).addUnit(unit);
        workbench = new Building(new Position(), new BuildingType());
        workbench.addAspect(aspect = new WorkbenchAspect(workbench));
        aspect.recipes.add(createRecipe());
    }

    private Recipe createRecipe() {
        RawRecipe raw = new RawRecipe();
        raw.name = "make_axe";
        raw.itemName = "axe";
        Recipe recipe = new Recipe(raw);
        recipe.parts.put("head", new Ingredient(Arrays.asList("bar"), "metal"));
        return recipe;
    }

    @Test
    void testOrderCreation() {
        assert(aspect.entries.isEmpty());
        assert(!aspect.hasActiveOrders);
        ItemOrder order = new ItemOrder(aspect.recipes.get(0));
        system.addOrder(aspect, order);
        assertEquals(1, aspect.entries.size());
        assertEquals(order, aspect.entries.get(0).order);
        assert(aspect.hasActiveOrders);
    }

    @Test
    void failCreateOrderWithWrongRecipe() {
        assert(aspect.entries.isEmpty());
        assert(!aspect.hasActiveOrders);
        ItemOrder order = new ItemOrder(new Recipe("qwer"));
        system.addOrder(aspect, order);
        assert(aspect.entries.isEmpty());
        assert(!aspect.hasActiveOrders);
    }

    @Test
    void testTaskCreation() {
        assert(aspect.entries.isEmpty());
        assert(!aspect.hasActiveOrders);
        ItemOrder order = new ItemOrder(aspect.recipes.get(0));
        system.addOrder(aspect, order);
        assertEquals(1, aspect.entries.size());
        assertEquals(order, aspect.entries.get(0).order);
        system.updateWorkbenchState(workbench);
        assert(aspect.entries.get(0).task != null);
        assert(aspect.hasActiveOrders);
    }

    @Test
    void testRemoveOrder() {
        ItemOrder order = new ItemOrder(aspect.recipes.get(0));
        system.addOrder(aspect, order);
        assertEquals(1, aspect.entries.size());
        assertEquals(order, aspect.entries.get(0).order);
        system.removeOrder(aspect, order);
        assert(aspect.entries.isEmpty());
        assert(!aspect.hasActiveOrders);
    }

    @Test
    void testSetOrderSuspended() {
        ItemOrder order = new ItemOrder(aspect.recipes.get(0));
        system.addOrder(aspect, order);
        assert(aspect.hasActiveOrders);
        assertEquals(order, aspect.entries.get(0).order);

        system.setOrderSuspended(aspect, order, true);
        assert(!aspect.hasActiveOrders);
        assertEquals(SUSPENDED, aspect.entries.get(0).order.status);

        system.setOrderSuspended(aspect, order, false);
        assert(aspect.hasActiveOrders);
        assertEquals(OPEN, aspect.entries.get(0).order.status);
    }

    @Test
    void testSetOrderRepeated() {
        ItemOrder order = new ItemOrder(aspect.recipes.get(0));
        system.addOrder(aspect, order);
        assert(aspect.hasActiveOrders);
        assertEquals(order, aspect.entries.get(0).order);

        system.setOrderRepeated(aspect, order, true);
        assert(aspect.hasActiveOrders);
        assert(order.repeated);

        system.setOrderRepeated(aspect, order, false);
        assert(aspect.hasActiveOrders);
        assert(!order.repeated);
    }

    @Test
    void testHandleOpenOrder() {
        ItemOrder order = new ItemOrder(aspect.recipes.get(0));
        system.addOrder(aspect, order);
        assert(aspect.hasActiveOrders);
        assertEquals(1, aspect.entries.size());
        assertEquals(order, aspect.entries.get(0).order);
        system.updateWorkbenchState(workbench);

    }
}
