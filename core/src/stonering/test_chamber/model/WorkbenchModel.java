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
    private static final int MAP_SIZE = 9;

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
        LocalMap localMap = new LocalMap(MAP_SIZE, MAP_SIZE, 2);
        for (int x = 0; x < MAP_SIZE; x++) {
            for (int y = 0; y < MAP_SIZE; y++) {
                localMap.setBlock(x, y, 0, BlockTypesEnum.WALL, MaterialMap.getInstance().getId("soil"));
                localMap.setBlock(x, y, 1, BlockTypesEnum.FLOOR, MaterialMap.getInstance().getId("soil"));
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
