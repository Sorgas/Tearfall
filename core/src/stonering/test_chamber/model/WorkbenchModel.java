package stonering.test_chamber.model;

import stonering.entity.item.Item;
import stonering.entity.unit.Unit;
import stonering.game.model.EntitySelector;
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
        get(BuildingContainer.class).addBuilding(buildingGenerator.generateBuilding("sawing_rack", new Position(2, 4, 2)));
        get(BuildingContainer.class).addBuilding(buildingGenerator.generateBuilding("campfire", new Position(4, 4, 2)));
        get(BuildingContainer.class).addBuilding(buildingGenerator.generateBuilding("carpenter_workbench", new Position(6, 4, 2)));
        get(BuildingContainer.class).addBuilding(buildingGenerator.generateBuilding("bed", new Position(8, 4, 2)));
        get(UnitContainer.class).addUnit(createUnit());
        get(EntitySelector.class).position.set(4, 4, 2);
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
        Item item = generator.generateItem("log", "wood", null);
        item.setOrigin("willow");
        container.onMapItemsSystem.putNewItem(item, new Position(3, 0, 2));
        item = generator.generateItem("log", "wood", null);
        item.setOrigin("willow");
        container.onMapItemsSystem.putNewItem(item, new Position(4, 0, 2));
    }

    @Override
    public String toString() {
        return "WorkbenchModel";
    }
}
