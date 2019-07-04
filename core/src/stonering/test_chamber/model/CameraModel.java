package stonering.test_chamber.model;

import stonering.entity.local.environment.GameCalendar;
import stonering.entity.World;
import stonering.enums.blocks.BlockTypesEnum;
import stonering.enums.materials.MaterialMap;
import stonering.game.model.lists.PlantContainer;
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
        Random random = new Random();
        MaterialMap materialMap = MaterialMap.getInstance();
        for (int i = 0; i < 200; i++) {
            int x = random.nextInt(size);
            int y = random.nextInt(size);
            localMap.setBlock(x, y, 2, BlockTypesEnum.WALL, materialMap.getId("soil"));
            localMap.setBlock(x, y, 3, BlockTypesEnum.FLOOR, materialMap.getId("soil"));
        }
        return localMap;
    }

    @Override
    protected int getMapSize() {
        return MAP_SIZE;
    }
}
