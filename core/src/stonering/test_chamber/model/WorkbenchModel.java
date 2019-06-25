package stonering.test_chamber.model;

import stonering.entity.local.building.Building;
import stonering.enums.blocks.BlockTypesEnum;
import stonering.enums.materials.MaterialMap;
import stonering.game.model.EntitySelector;
import stonering.game.model.GameModel;
import stonering.game.model.lists.BuildingContainer;
import stonering.game.model.lists.ItemContainer;
import stonering.game.model.lists.PlantContainer;
import stonering.game.model.local_map.LocalMap;
import stonering.game.view.tilemaps.LocalTileMap;
import stonering.generators.buildings.BuildingGenerator;
import stonering.util.geometry.Position;

/**
 * @author Alexander_Kuzyakov on 25.06.2019.
 */
public class WorkbenchModel extends GameModel {

    public WorkbenchModel() {
        put(new PlantContainer());
        put(new BuildingContainer());
        put(new ItemContainer());
        put(createLocalMap());
        put(new LocalTileMap());
        put(new EntitySelector());
    }

    @Override
    public void init() {
        super.init();
        get(BuildingContainer.class).addBuilding(createBuilding());
    }

    private LocalMap createLocalMap() {
        LocalMap localMap = new LocalMap(1, 1, 2);
        localMap.setBlock(0, 0, 0, BlockTypesEnum.WALL, MaterialMap.getInstance().getId("soil"));
        localMap.setBlock(0, 0, 1, BlockTypesEnum.FLOOR, MaterialMap.getInstance().getId("soil"));
        return localMap;
    }

    private Building createBuilding() {
        return new BuildingGenerator().generateBuilding("forge", new Position(0, 0, 1));
    }
}
