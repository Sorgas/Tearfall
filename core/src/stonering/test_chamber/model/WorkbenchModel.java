package stonering.test_chamber.model;

import stonering.entity.building.Building;
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

    @Override
    public void init() {
        super.init();
        get(BuildingContainer.class).addBuilding(createBuilding());
        get(UnitContainer.class).addUnit(createUnit());
        get(EntitySelector.class).setPosition(4, 4, 2);
        createItems();
    }

    private Building createBuilding() {
        return new BuildingGenerator().generateBuilding("campfire", new Position(4, 4, 2));
    }

    private Unit createUnit() {
        return new CreatureGenerator().generateUnit(new Position(getMapSize() / 2, getMapSize() / 2, 2), "human");
    }

    private void createItems() {
        ItemGenerator generator = new ItemGenerator();
        get(ItemContainer.class).addAndPut(generator.generateItem("meat_piece", "meat", new Position(0, 0, 2)));
        get(ItemContainer.class).addAndPut(generator.generateItem("log", "wood", new Position(1, 0, 2)));
        get(ItemContainer.class).addAndPut(generator.generateItem("log", "wood", new Position(2, 0, 2)));
        Item item = generator.generateItem("log", "wood", new Position(3, 0, 2));
        item.setOrigin("willow");
        get(ItemContainer.class).addAndPut(item);
        item = generator.generateItem("log", "wood", new Position(4, 0, 2));
        item.setOrigin("willow");
        get(ItemContainer.class).addAndPut(item);
    }

    @Override
    public String toString() {
        return "WorkbenchModel";
    }
}
