package stonering.test_chamber.model;

import stonering.enums.blocks.BlockTypeEnum;
import stonering.enums.materials.MaterialMap;
import stonering.game.model.entity_selector.EntitySelectorSystem;
import stonering.game.model.local_map.LocalMap;
import stonering.game.model.system.liquid.LiquidContainer;
import stonering.util.geometry.Position;

/**
 * @author Alexander on 17.06.2020.
 */
public class LiquidFlowPlaytestModel extends TestModel {
    private LocalMap localMap;
    private LiquidContainer container;

    @Override
    public void init() {
        super.init();
        localMap = get(LocalMap.class);
        container = get(LiquidContainer.class);
//        Int3dBounds bounds = new Int3dBounds(0,0,3, MAP_SIZE - 1, MAP_SIZE - 1, 4);
//        bounds.iterate(position -> localMap.blockType.setBlock(position, BlockTypeEnum.WALL, MaterialMap.getId("soil")));
        for (int x = 1; x < 10; x++) {
            localMap.blockType.setBlock(x, 9, 2, BlockTypeEnum.SPACE, MaterialMap.getId("air"));
            localMap.blockType.setBlock(x, 9, 1, BlockTypeEnum.FLOOR, MaterialMap.getId("soil"));
        }
//        get(LiquidContainer.class).createLiquidSource(new Position(1, 9, 5), MaterialMap.getId("water"), 1);
        container.setAmount(new Position(5, 5, 2), 7);
        get(EntitySelectorSystem.class).setSelectorPosition(new Position(5, 5, 2));
    }
}
