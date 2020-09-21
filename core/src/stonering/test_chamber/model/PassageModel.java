package stonering.test_chamber.model;

import stonering.entity.job.Task;
import stonering.entity.job.action.MoveAction;
import stonering.entity.unit.Unit;
import stonering.enums.blocks.BlockTypeEnum;
import stonering.enums.materials.MaterialMap;
import stonering.game.model.entity_selector.EntitySelectorSystem;
import stonering.game.model.system.unit.UnitContainer;
import stonering.game.model.system.task.TaskContainer;
import stonering.game.model.local_map.LocalMap;
import stonering.game.model.local_map.passage.PassageMap;
import stonering.generators.creatures.CreatureGenerator;
import stonering.util.geometry.Position;

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
        updateLocalMap();
        get(UnitContainer.class).add(createUnit());
        get(TaskContainer.class).addTask(createTask());
        get(EntitySelectorSystem.class).selector.position.set(MAP_SIZE / 2, MAP_SIZE / 2, 5);
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
            localMap.blockType.setBlock(MAP_SIZE / 2, y, 2, BlockTypeEnum.WALL, MaterialMap.getId("soil"));
            localMap.blockType.setBlock(MAP_SIZE / 2, y, 3, BlockTypeEnum.WALL, MaterialMap.getId("soil"));
            localMap.blockType.setBlock(MAP_SIZE / 2, y, 4, BlockTypeEnum.WALL, MaterialMap.getId("soil"));
            localMap.blockType.setBlock(MAP_SIZE / 2, y, 5, BlockTypeEnum.FLOOR, MaterialMap.getId("soil"));
        }
        for (int z = 2; z < 5; z++) {
            localMap.blockType.setBlock(MAP_SIZE / 2 + 1, MAP_SIZE / 2, z, BlockTypeEnum.STAIRS, MaterialMap.getId("marble"));
            localMap.blockType.setBlock(MAP_SIZE / 2 - 1, MAP_SIZE / 2, z, BlockTypeEnum.STAIRS, MaterialMap.getId("marble"));
        }
        localMap.blockType.setBlock(MAP_SIZE / 2 + 1, MAP_SIZE / 2, 5, BlockTypeEnum.DOWNSTAIRS, MaterialMap.getId("marble"));
        localMap.blockType.setBlock(MAP_SIZE / 2 - 1, MAP_SIZE / 2, 5, BlockTypeEnum.DOWNSTAIRS, MaterialMap.getId("marble"));
//        addWall(2, 3);
//        addWall(2, 4);
//        addWall(2, 5);
//        addWall(2, 6);
//        addWall(3, 3);
//        addWall(3, 6);
//        addWall(4, 3);
//        addWall(4, 6);
    }

    private void addWall(int x, int y) {
        LocalMap localMap = get(LocalMap.class);
        localMap.blockType.setBlock(MAP_SIZE / x, y, 2, BlockTypeEnum.WALL, MaterialMap.getId("soil"));
        localMap.blockType.setBlock(MAP_SIZE / x, y, 3, BlockTypeEnum.FLOOR, MaterialMap.getId("soil"));
    }

    private Unit createUnit() {
        return new CreatureGenerator().generateUnit(new Position(3, 4, 2), "human");
    }

    private Task createTask() {
        return new Task(new MoveAction(new Position(10, 10, 2)));
    }
}
