package stonering.test_chamber.model;

import stonering.enums.blocks.BlockTypesEnum;
import stonering.enums.materials.MaterialMap;
import stonering.game.core.model.LocalMap;

public class SingleTreeModel {
    private LocalMap localMap;

    private void createMap() {
        localMap = new LocalMap(11, 11, 20);
        MaterialMap materialMap = MaterialMap.getInstance();
        for (int x = 0; x < 11; x++) {
            for (int y = 0; y < 11; y++) {
                localMap.setBlock(x, y, 0, BlockTypesEnum.WALL, materialMap.getId("soil"));
                localMap.setBlock(x, y, 1, BlockTypesEnum.FLOOR, materialMap.getId("soil"));
            }
        }
    }
}
