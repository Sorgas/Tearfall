package stonering.test_chamber.model;

import stonering.entity.building.Building;
import stonering.game.model.EntitySelector;
import stonering.game.model.system.*;
import stonering.generators.buildings.BuildingGenerator;
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
        get(EntitySelector.class).setPosition(4, 4, 2);
        createItems();
    }

    private Building createBuilding() {
        return new BuildingGenerator().generateBuilding("campfire", new Position(4, 4, 2));
    }

    private void createItems() {
        get(ItemContainer.class).addItem(new ItemGenerator().generateItem("piece", "meat", new Position(0,0,2)));
        get(ItemContainer.class).addItem(new ItemGenerator().generateItem("log", "pine", new Position(1,0,2)));
        get(ItemContainer.class).addItem(new ItemGenerator().generateItem("log", "pine", new Position(2,0,2)));
    }

    @Override
    public String toString() {
        return "WorkbenchModel";
    }
}
