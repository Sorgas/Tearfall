package stonering.test_chamber.model;

import stonering.enums.blocks.BlockTypesEnum;
import stonering.enums.materials.MaterialMap;
import stonering.game.model.local_map.LocalMap;

/**
 * Model for testing water pond and water plants.
 *
 * @author Alexander on 22.02.2019.
 */
public class PondPlantsModel extends TestModel {
    private static final int MAP_CENTER = MAP_SIZE / 2;

    @Override
    public void init() {
        super.init();
    }

    @Override
    protected void updateLocalMap() {
        LocalMap localMap = get(LocalMap.class);
        MaterialMap materialMap = MaterialMap.instance();
        for (int x = 0; x < localMap.xSize; x++) {
            for (int y = 0; y < localMap.ySize; y++) {
                localMap.setBlock(x, y, 0, BlockTypesEnum.WALL, materialMap.getId("soil"));
                if (Math.pow(x - MAP_CENTER, 2) + Math.pow(y - MAP_CENTER, 2) <= 9) {
                    localMap.setBlock(x, y, 1, BlockTypesEnum.FLOOR, materialMap.getId("soil"));
                    localMap.setFlooding(x, y, 1, 7);
                } else {
                    localMap.setBlock(x, y, 1, BlockTypesEnum.WALL, materialMap.getId("soil"));
                    localMap.setBlock(x, y, 2, BlockTypesEnum.FLOOR, materialMap.getId("soil"));
                }
            }
        }
    }
}
