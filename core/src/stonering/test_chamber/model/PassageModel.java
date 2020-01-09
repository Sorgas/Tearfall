package stonering.test_chamber.model;

import stonering.entity.job.Task;
import stonering.entity.job.action.MoveAction;
import stonering.entity.unit.Unit;
import stonering.enums.blocks.BlockTypesEnum;
import stonering.enums.materials.MaterialMap;
import stonering.game.model.entity_selector.EntitySelector;
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
        get(UnitContainer.class).addUnit(createUnit());
        get(TaskContainer.class).addTask(createTask());
        get(EntitySelector.class).position.set(MAP_SIZE / 2, MAP_SIZE / 2, 5);
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

    private Unit createUnit() {
        return new CreatureGenerator().generateUnit(new Position(3, 3, 2), "human");
    }

    private Task createTask() {
        return new Task("move", new MoveAction(new Position(10, 10, 2)), 1);
    }
}
