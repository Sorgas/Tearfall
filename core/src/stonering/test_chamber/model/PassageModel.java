package stonering.test_chamber.model;

import stonering.enums.blocks.BlockTypesEnum;
import stonering.enums.materials.MaterialMap;
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
    }

    /**
     * Creates flat layers of soil.
     */
    @Override
    protected void updateLocalMap() {
        super.updateLocalMap();
        LocalMap localMap = get(LocalMap.class);
        MaterialMap materialMap = MaterialMap.instance();
        for (int y = 0; y < MAP_SIZE; y++) {
            localMap.setBlock(MAP_SIZE / 2, y, 2, BlockTypesEnum.WALL, materialMap.getId("soil"));
            localMap.setBlock(MAP_SIZE / 2, y, 3, BlockTypesEnum.WALL, materialMap.getId("soil"));
            localMap.setBlock(MAP_SIZE / 2, y, 4, BlockTypesEnum.WALL, materialMap.getId("soil"));
            localMap.setBlock(MAP_SIZE / 2, y, 5, BlockTypesEnum.FLOOR, materialMap.getId("soil"));
        }
        for (int z = 2; z < 5; z++) {
            localMap.setBlock(MAP_SIZE / 2 + 1, MAP_SIZE / 2, z, BlockTypesEnum.STAIRS, materialMap.getId("marble"));
            localMap.setBlock(MAP_SIZE / 2 - 1, MAP_SIZE / 2, z, BlockTypesEnum.STAIRS, materialMap.getId("marble"));
        }
        localMap.setBlock(MAP_SIZE / 2 + 1, MAP_SIZE / 2, 5, BlockTypesEnum.DOWNSTAIRS, materialMap.getId("marble"));
        localMap.setBlock(MAP_SIZE / 2 - 1, MAP_SIZE / 2, 5, BlockTypesEnum.DOWNSTAIRS, materialMap.getId("marble"));
    }
}
