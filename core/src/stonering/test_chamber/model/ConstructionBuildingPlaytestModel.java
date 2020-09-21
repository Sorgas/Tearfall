package stonering.test_chamber.model;

import java.util.*;

import stonering.entity.building.BuildingOrder;
import stonering.entity.crafting.IngredientOrder;
import stonering.entity.item.selectors.ConfiguredItemSelector;
import stonering.entity.item.selectors.ItemSelector;
import stonering.entity.unit.Unit;
import stonering.entity.unit.aspects.job.JobSkillAspect;
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
 * Model with created orders for contructions.
 *
 * @author Alexander on 27.04.2020.
 */
public class ConstructionBuildingPlaytestModel extends TestModel {
    private BuildingGenerator buildingGenerator;

    public ConstructionBuildingPlaytestModel() {
        buildingGenerator = new BuildingGenerator();
    }

    @Override
    public void init() {
        super.init();
        get(UnitContainer.class).add(createUnit(new Position(0,0,2)));
        get(UnitContainer.class).add(createUnit(new Position(1,0,2)));
        get(EntitySelectorSystem.class).selector.position.set(4, 4, 2);
        createItems();
        createOrders();
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
        for (int x = 1; x < 6; x++) {
            designationSystem.submitBuildingDesignation(createConstructionOrder("build_wall", new Position(x, 5, 2)), 1);
        }
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

    private Unit createUnit(Position position) {
        Unit human = new CreatureGenerator().generateUnit(position, "human");
        human.get(JobSkillAspect.class).enabledJobs.add("builder");
        return human;
    }

    @Override
    public String toString() {
        return "WorkbenchModel";
    }
}
