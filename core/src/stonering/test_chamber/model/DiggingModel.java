package stonering.test_chamber.model;

import static stonering.enums.blocks.BlockTypeEnum.*;
import static stonering.enums.blocks.BlockTypeEnum.RAMP;

import stonering.entity.item.Item;
import stonering.entity.unit.Unit;
import stonering.entity.unit.aspects.job.JobSkillAspect;
import stonering.enums.materials.MaterialMap;
import stonering.game.model.entity_selector.EntitySelectorSystem;
import stonering.game.model.system.item.ItemContainer;
import stonering.game.model.system.unit.UnitContainer;
import stonering.game.model.local_map.LocalMap;
import stonering.generators.creatures.CreatureGenerator;
import stonering.generators.items.ItemGenerator;
import stonering.util.geometry.Position;

/**
 * Model for testing farms.
 *
 * @author Alexander_Kuzyakov on 04.07.2019.
 */
public class DiggingModel extends TestModel {

    @Override
    public void init() {
        super.init();
        get(EntitySelectorSystem.class).selector.position.set(MAP_SIZE / 2, MAP_SIZE / 2, 10);
        get(UnitContainer.class).add(createUnit(new Position(1, 5, 10)));
        get(UnitContainer.class).add(createUnit(new Position(5, 5, 10)));
        Item pickaxe = new ItemGenerator().generateItem("pickaxe", "iron", null);
        get(ItemContainer.class).onMapItemsSystem.addNewItemToMap(pickaxe, new Position(0, 0, 10));
        pickaxe = new ItemGenerator().generateItem("pickaxe", "iron", null);
        get(ItemContainer.class).onMapItemsSystem.addNewItemToMap(pickaxe, new Position(0, 1, 10));
    }

    @Override
    protected void updateLocalMap() {
        LocalMap localMap = get(LocalMap.class);
        for (int x = 0; x < localMap.xSize; x++) {
            for (int y = 0; y < localMap.ySize; y++) {
                for (int z = 0; z < 10; z++) {
                    localMap.blockType.setBlock(x, y, z, WALL, MaterialMap.getId("soil"));
                }
                localMap.blockType.setBlock(x, y, 10, FLOOR, MaterialMap.getId("soil"));
            }
        }
        for (int y = 0; y < localMap.ySize; y++) {
            for (int x = 7; x < localMap.xSize; x++) {
                localMap.blockType.setBlock(x, y, 10, WALL, MaterialMap.getId("soil"));
                localMap.blockType.setBlock(x, y, 11, FLOOR, MaterialMap.getId("soil"));
            }
            localMap.blockType.setBlock(6, y, 10, RAMP, MaterialMap.getId("soil"));
        }
    }

    private Unit createUnit(Position position) {
        Unit unit =  new CreatureGenerator().generateUnit(position, "human");
        unit.get(JobSkillAspect.class).enabledJobs.add("miner");
        return unit;
    }
}
