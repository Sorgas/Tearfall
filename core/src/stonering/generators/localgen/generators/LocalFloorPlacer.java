package stonering.generators.localgen.generators;

import stonering.enums.blocks.BlockTypesEnum;
import stonering.game.core.model.LocalMap;
import stonering.generators.localgen.LocalGenContainer;

/**
 * Created by Alexander on 30.12.2017.
 */
public class LocalFloorPlacer {
    private LocalMap localMap;

    public LocalFloorPlacer(LocalGenContainer container) {
        localMap = container.getLocalMap();
    }

    public void execute() {
        for (int x = 0; x < localMap.getxSize(); x++) {
            for (int y = 0; y < localMap.getySize(); y++) {
                for (int z = localMap.getzSize() - 1; z > 0; z--) {
                    if (isFloorCell(x, y, z)) { //non space sell
                        localMap.setBlock(x, y, z, BlockTypesEnum.FLOOR.getCode(), localMap.getMaterial(x, y, z - 1));
                    }
                }
            }
        }
        System.out.println("floor");
    }

    private boolean isFloorCell(int x, int y, int z) {
        return localMap.getBlockType(x, y, z) == BlockTypesEnum.SPACE.getCode() &&
                localMap.getBlockType(x, y, z - 1) == BlockTypesEnum.WALL.getCode();
    }
}
