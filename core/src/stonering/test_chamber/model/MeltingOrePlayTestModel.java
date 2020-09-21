package stonering.test_chamber.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import stonering.entity.building.BuildingOrder;
import stonering.entity.crafting.IngredientOrder;
import stonering.entity.item.selectors.ConfiguredItemSelector;
import stonering.entity.item.selectors.ItemSelector;
import stonering.entity.unit.Unit;
import stonering.entity.unit.aspects.MovementAspect;
import stonering.enums.buildings.blueprint.Blueprint;
import stonering.enums.buildings.blueprint.BlueprintsMap;
import stonering.enums.materials.MaterialMap;
import stonering.game.model.entity_selector.EntitySelectorSystem;
import stonering.game.model.system.item.ItemContainer;
import stonering.game.model.system.task.DesignationSystem;
import stonering.game.model.system.task.TaskContainer;
import stonering.game.model.system.unit.UnitContainer;
import stonering.generators.buildings.BuildingGenerator;
import stonering.generators.creatures.CreatureGenerator;
import stonering.generators.items.ItemGenerator;
import stonering.util.geometry.Position;

/**
 * @author Alexander on 01.06.2020.
 */
public class MeltingOrePlayTestModel extends TestModel {
    private BuildingGenerator buildingGenerator;

    public MeltingOrePlayTestModel() {
        buildingGenerator = new BuildingGenerator();
    }

    @Override
    public void init() {
        super.init();
        get(UnitContainer.class).add(createUnit());
        get(EntitySelectorSystem.class).selector.position.set(4, 4, 2);
        createItems();
        createOrders();
    }

    private Unit createUnit() {
        Unit unit = new CreatureGenerator().generateUnit(new Position(MAP_SIZE / 2, MAP_SIZE / 2, 2), "human");
        unit.get(MovementAspect.class).speed = 0.2f;
        return unit;
    }

    private void createItems() {
        ItemGenerator generator = new ItemGenerator();
        ItemContainer container = get(ItemContainer.class);
        for (int i = 0; i < 40; i++) {
            container.onMapItemsSystem.addNewItemToMap(generator.generateItem("log", "wood", null), new Position(0, 0, 2));
        }
    }

    private void createOrders() {
        DesignationSystem designationSystem = get(TaskContainer.class).designationSystem;
        designationSystem.submitBuildingDesignation(createConstructionOrder("build_wall", new Position(5, 5, 2)), 1);
        designationSystem.submitBuildingDesignation(createConstructionOrder("build_wall", new Position(5, 6, 2)), 1);
        designationSystem.submitBuildingDesignation(createConstructionOrder("build_wall", new Position(5, 7, 2)), 1);
        designationSystem.submitBuildingDesignation(createConstructionOrder("build_wall", new Position(6, 5, 2)), 1);
        designationSystem.submitBuildingDesignation(createConstructionOrder("build_wall", new Position(7, 5, 2)), 1);
        designationSystem.submitBuildingDesignation(createConstructionOrder("build_wall", new Position(6, 7, 2)), 1);
        designationSystem.submitBuildingDesignation(createConstructionOrder("build_wall", new Position(7, 7, 2)), 1);
    }

    private BuildingOrder createConstructionOrder(String blueprintName, Position position) {
        Blueprint blueprint = BlueprintsMap.getInstance().getBlueprint(blueprintName);
        BuildingOrder order = new BuildingOrder(blueprint, position);
        blueprint.ingredients.forEach((key, ingredient) -> {
            System.out.println("creating ingredient for " + key);
            Map<String, Set<Integer>> materialsMap = new HashMap<>();
            Set<Integer> materials = new HashSet<>();
            materials.add(MaterialMap.getId("wood"));
            materialsMap.put("log", materials);
            ItemSelector woodSelector = new ConfiguredItemSelector(materialsMap);
            order.ingredientOrders.put(key, new IngredientOrder(ingredient, woodSelector));
        });
        return order;
    }

    @Override
    public String toString() {
        return "WorkbenchModel";
    }
}
