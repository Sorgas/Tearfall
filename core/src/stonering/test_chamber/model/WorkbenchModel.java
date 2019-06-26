package stonering.test_chamber.model;

import stonering.entity.local.building.Building;
import stonering.enums.blocks.BlockTypesEnum;
import stonering.enums.materials.MaterialMap;
import stonering.game.model.EntitySelector;
import stonering.game.model.GameModel;
import stonering.game.model.lists.*;
import stonering.game.model.local_map.LocalMap;
import stonering.game.view.tilemaps.LocalTileMap;
import stonering.generators.buildings.BuildingGenerator;
import stonering.util.geometry.Position;

/**
 * @author Alexander_Kuzyakov on 25.06.2019.
 */
public class WorkbenchModel extends GameModel {

    public WorkbenchModel() {
        put(createLocalMap());
        put(new PlantContainer());
        put(new BuildingContainer());
        put(new ItemContainer());
        put(new LocalTileMap());
        put(new EntitySelector());
        put(new UnitContainer());
        put(new ZonesContainer());
    }

    @Override
    public void init() {
        super.init();
        get(BuildingContainer.class).addBuilding(createBuilding());
    }

    private LocalMap createLocalMap() {
        LocalMap localMap = new LocalMap(9, 9, 2);
        localMap.setBlock(0, 0, 0, BlockTypesEnum.WALL, MaterialMap.getInstance().getId("soil"));
        localMap.setBlock(0, 0, 1, BlockTypesEnum.FLOOR, MaterialMap.getInstance().getId("soil"));
        for (int x = 0; x < 9; x++) {
            for (int y = 0; y < 9; y++) {
            }
        }
        return localMap;
    }

    private void putItems() {

    }

    private void putUnit() {

    }

    private Building createBuilding() {
        return new BuildingGenerator().generateBuilding("forge", new Position(0, 0, 1));
    }

    @Override
    public String toString() {
        return "WorkbenchModel";
    }
}
