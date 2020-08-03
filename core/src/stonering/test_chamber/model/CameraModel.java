package stonering.test_chamber.model;

import stonering.enums.blocks.BlockTypeEnum;
import stonering.enums.materials.MaterialMap;
import stonering.game.model.local_map.LocalMap;

import java.util.Random;

/**
 * Model with wide map for testing camera movement
 *
 * @author Alexander on 06.06.2019.
 */
public class CameraModel extends TestModel {
    private static final int MAP_SIZE = 200;

    @Override
    public void init() {
        super.init();
    }

    /**
     * Creates flat layers of soil.
     * //TODO add more complex relief
     */
    @Override
    protected void updateLocalMap() {
        LocalMap localMap = get(LocalMap.class);
        Random random = new Random();
        MaterialMap materialMap = MaterialMap.instance();
        for (int i = 0; i < 200; i++) {
            int x = random.nextInt(localMap.xSize);
            int y = random.nextInt(localMap.ySize);
            localMap.blockType.setBlock(x, y, 2, BlockTypeEnum.WALL, MaterialMap.getId("soil"));
            localMap.blockType.setBlock(x, y, 3, BlockTypeEnum.FLOOR, MaterialMap.getId("soil"));
        }
    }
}
