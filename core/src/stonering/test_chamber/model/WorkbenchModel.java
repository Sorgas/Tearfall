package stonering.test_chamber.model;

import stonering.entity.item.Item;
import stonering.entity.unit.Unit;
import stonering.enums.OrientationEnum;
import stonering.game.model.entity_selector.EntitySelectorSystem;
import stonering.game.model.system.building.BuildingContainer;
import stonering.game.model.system.item.ItemContainer;
import stonering.game.model.system.unit.UnitContainer;
import stonering.generators.buildings.BuildingGenerator;
import stonering.generators.creatures.CreatureGenerator;
import stonering.generators.items.ItemGenerator;
import stonering.util.geometry.Position;

/**
 * Model with workbench for testing ui and crafting tasks.
 *
 * @author Alexander_Kuzyakov on 25.06.2019.
 */
public class WorkbenchModel extends TestModel {
    private BuildingGenerator buildingGenerator;

    public WorkbenchModel() {
        buildingGenerator = new BuildingGenerator();
    }

    @Override
    public void init() {
        super.init();
        get(BuildingContainer.class).addBuilding(buildingGenerator.generateBuilding("sawing_rack", new Position(2, 4, 2), OrientationEnum.N));
        get(BuildingContainer.class).addBuilding(buildingGenerator.generateBuilding("campfire", new Position(4, 4, 2), OrientationEnum.N));
        get(BuildingContainer.class).addBuilding(buildingGenerator.generateBuilding("carpenter_workbench", new Position(6, 4, 2), OrientationEnum.N));
        get(BuildingContainer.class).addBuilding(buildingGenerator.generateBuilding("bed", new Position(8, 4, 2), OrientationEnum.N));
        get(BuildingContainer.class).addBuilding(buildingGenerator.generateBuilding("kitchen", new Position(4, 2, 2), OrientationEnum.N));
        get(UnitContainer.class).addUnit(createUnit());
        get(EntitySelectorSystem.class).selector.position.set(4, 4, 2);
        createItems();
    }

    private Unit createUnit() {
        return new CreatureGenerator().generateUnit(new Position(getMapSize() / 2, getMapSize() / 2, 2), "human");
    }

    private void createItems() {
        ItemGenerator generator = new ItemGenerator();
        ItemContainer container = get(ItemContainer.class);
        container.onMapItemsSystem.putNewItem(generator.generateItem("meat_piece", "meat", null), new Position(0, 0, 2));
        container.onMapItemsSystem.putNewItem(generator.generateItem("log", "wood", null), new Position(1, 0, 2));
        container.onMapItemsSystem.putNewItem(generator.generateItem("log", "wood", null), new Position(2, 0, 2));
        container.onMapItemsSystem.putNewItem(generator.generateItem("log", "wood", null), new Position(3, 0, 2));
        container.onMapItemsSystem.putNewItem(generator.generateItem("log", "wood", null), new Position(4, 0, 2));
        container.onMapItemsSystem.putNewItem(generator.generateItem("bar", "copper", null), new Position(5, 0, 2));
        container.onMapItemsSystem.putNewItem(generator.generateItem("bar", "copper", null), new Position(6, 0, 2));
        container.onMapItemsSystem.putNewItem(generator.generateItem("bar", "copper", null), new Position(7, 0, 2));
        container.onMapItemsSystem.putNewItem(generator.generateItem("bar", "copper", null), new Position(8, 0, 2));
        container.onMapItemsSystem.putNewItem(generator.generateItem("bar", "copper", null), new Position(9, 0, 2));
        container.onMapItemsSystem.putNewItem(generator.generateItem("bar", "iron", null), new Position(0, 1, 2));
        container.onMapItemsSystem.putNewItem(generator.generateItem("bar", "iron", null), new Position(1, 1, 2));
        container.onMapItemsSystem.putNewItem(generator.generateItem("bar", "iron", null), new Position(2, 1, 2));
        container.onMapItemsSystem.putNewItem(generator.generateItem("bar", "iron", null), new Position(3, 1, 2));
        container.onMapItemsSystem.putNewItem(generator.generateItem("bar", "iron", null), new Position(4, 1, 2));
        container.onMapItemsSystem.putNewItem(generator.generateItem("bar", "iron", null), new Position(6, 1, 2));

    }

    @Override
    public String toString() {
        return "WorkbenchModel";
    }
}
