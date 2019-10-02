package stonering.test_chamber.model;

import stonering.entity.building.Building;
import stonering.entity.unit.Unit;
import stonering.game.model.EntitySelector;
import stonering.game.model.system.*;
import stonering.game.model.system.building.BuildingContainer;
import stonering.game.model.system.units.UnitContainer;
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
        get(ItemContainer.class).addItem(new ItemGenerator().generateItem("meat_piece", "meat", new Position(0,0,2)));
        get(ItemContainer.class).addItem(new ItemGenerator().generateItem("log", "wood", new Position(1,0,2)));
        get(ItemContainer.class).addItem(new ItemGenerator().generateItem("log", "wood", new Position(2,0,2)));
    }

    @Override
    public String toString() {
        return "WorkbenchModel";
    }
}
