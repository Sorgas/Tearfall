package stonering.test_chamber.model;

import stonering.entity.local.building.Building;
import stonering.game.model.lists.*;
import stonering.generators.buildings.BuildingGenerator;
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
    }

    private Building createBuilding() {
        return new BuildingGenerator().generateBuilding("forge", new Position(0, 0, 1));
    }

    @Override
    public String toString() {
        return "WorkbenchModel";
    }
}
