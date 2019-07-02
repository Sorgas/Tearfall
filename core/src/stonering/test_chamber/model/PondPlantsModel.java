package stonering.test_chamber.model;

import stonering.entity.local.environment.GameCalendar;
import stonering.entity.world.World;
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
        get(GameCalendar.class).addListener("minute", get(World.class).getStarSystem());
    }

    @Override
    protected LocalMap createLocalMap(int size) {
        LocalMap localMap = new LocalMap(size, size, 20);
        MaterialMap materialMap = MaterialMap.getInstance();
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
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

        return localMap;
    }
}
