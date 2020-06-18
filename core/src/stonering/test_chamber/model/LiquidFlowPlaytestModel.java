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
            localMap.blockType.setBlock(x, 9, 0, BlockTypeEnum.FLOOR, MaterialMap.getId("soil"));
        }
        localMap.blockType.setBlock(1, 9, 1, BlockTypeEnum.SPACE, MaterialMap.getId("air"));
        localMap.blockType.setBlock(1, 9, 2, BlockTypeEnum.SPACE, MaterialMap.getId("air"));
        localMap.blockType.setBlock(9, 9, 1, BlockTypeEnum.SPACE, MaterialMap.getId("air"));
        localMap.blockType.setBlock(9, 9, 2, BlockTypeEnum.SPACE, MaterialMap.getId("air"));
        container.createLiquidSource(new Position(1, 9, 5), MaterialMap.getId("water"), 1);

//        for (int x = 1; x < 5; x++) {
//            localMap.blockType.setBlock(x, 1, 2, BlockTypeEnum.SPACE, MaterialMap.getId("air"));
//            localMap.blockType.setBlock(x, 1, 1, BlockTypeEnum.FLOOR, MaterialMap.getId("soil"));
//        }
//        container.setAmount(new Position(1, 1, 1), 7);
//        container.setAmount(new Position(2, 1, 1), 6);
//        container.setAmount(new Position(3, 1, 1), 7);
//        container.setAmount(new Position(4, 1, 1), 7);

//        container.setAmount(new Position(5, 10, 2), 7);
//        container.createLiquidSource(new Position(1, 1, 6), MaterialMap.getId("water"), 1);
//        container.createLiquidSource(new Position(3, 3, 6), MaterialMap.getId("water"), 1);
//        container.createLiquidSource(new Position(5, 5, 6), MaterialMap.getId("water"), 1);
        
        get(EntitySelectorSystem.class).setSelectorPosition(new Position(5, 5, 2));
    }
}
