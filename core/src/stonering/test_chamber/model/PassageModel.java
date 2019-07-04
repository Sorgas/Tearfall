package stonering.test_chamber.model;

import stonering.entity.local.environment.GameCalendar;
import stonering.entity.World;
import stonering.enums.blocks.BlockTypesEnum;
import stonering.enums.materials.MaterialMap;
import stonering.game.model.lists.PlantContainer;
import stonering.game.model.local_map.LocalMap;
import stonering.game.model.local_map.PassageMap;

/**
 * Model for testing {@link PassageMap} functionality.
 * //TODO add unit able to dig and build.
 *
 * @author Alexander_Kuzyakov on 16.05.2019.
 */
public class PassageModel extends TestModel {
    private static final int MAP_SIZE = 11;

    @Override
    public void init() {
        super.init();
        get(GameCalendar.class).addListener("minute", get(World.class).getStarSystem());
        get(GameCalendar.class).addListener("minute", get(PlantContainer.class));
    }

    /**
     * Creates flat layers of soil.
     * //TODO add more complex relief
     */
    @Override
    protected LocalMap createLocalMap(int size) {
        LocalMap localMap = super.createLocalMap(size);
        MaterialMap materialMap = MaterialMap.getInstance();
        for (int y = 0; y < MAP_SIZE; y++) {
            localMap.setBlock(MAP_SIZE / 2, y, 2, BlockTypesEnum.WALL, materialMap.getId("soil"));
            localMap.setBlock(MAP_SIZE / 2, y, 3, BlockTypesEnum.FLOOR, materialMap.getId("soil"));
        }
        localMap.setBlock(MAP_SIZE / 2, MAP_SIZE / 2, 3, BlockTypesEnum.WALL, materialMap.getId("soil"));
        localMap.setBlock(MAP_SIZE / 2, MAP_SIZE / 2, 4, BlockTypesEnum.FLOOR, materialMap.getId("soil"));
        return localMap;
    }
}
