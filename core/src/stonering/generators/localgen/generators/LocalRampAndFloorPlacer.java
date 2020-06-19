package stonering.generators.localgen.generators;

import stonering.enums.blocks.BlockTypeEnum;
import stonering.game.model.local_map.LocalMap;
import stonering.generators.localgen.LocalGenContainer;
import stonering.util.logging.Logger;

/**
 * fills local map with ramps on z-layer borders.
 *
 * @author Alexander Kuzyakov on 17.10.2017.
 */
public class LocalRampAndFloorPlacer extends LocalGenerator {
    private LocalMap localMap;
    private int wallCode = BlockTypeEnum.WALL.CODE;
    private int spaceCode = BlockTypeEnum.SPACE.CODE;
    private int rampCode = BlockTypeEnum.RAMP.CODE;

    public LocalRampAndFloorPlacer(LocalGenContainer container) {
        super(container);
    }

    public void execute() {
        Logger.GENERATION.logDebug("placing ramps");
        localMap = container.model.get(LocalMap.class);
        fillRamps();
        fillFloors();
    }

    private void fillRamps() {
        for (int x = 0; x < localMap.xSize; x++) {
            for (int y = 0; y < localMap.xSize; y++) {
                for (int z = 1; z < localMap.zSize; z++) {
                    if (isGround(x, y, z) && hasAdjacentWall(x,y,z)) {
                        localMap.blockType.setBlock(x, y, z, (byte) rampCode, adjacentWallMaterial(x, y, z));
                    }
                }
            }
        }
    }

    private void fillFloors() {
        for (int x = 0; x < localMap.xSize; x++) {
            for (int y = 0; y < localMap.ySize; y++) {
                for (int z = localMap.zSize - 1; z > 0; z--) {
                    if (isFloorCell(x, y, z)) { //non space sell
                        localMap.blockType.setBlock(x, y, z, BlockTypeEnum.FLOOR.CODE, localMap.blockType.getMaterial(x, y, z - 1));
                    }
                }
            }
        }
    }

    private boolean isGround(int x, int y, int z) {
        return localMap.blockType.get(x, y, z) == spaceCode && localMap.blockType.get(x, y, z - 1) == wallCode;
    }

    private boolean hasAdjacentWall(int xc, int yc, int z) {
        for (int x = xc - 1; x <= xc + 1; x++) {
            for (int y = yc - 1; y <= yc + 1; y++) {
                if (localMap.inMap(x, y, z)) {
                    if (localMap.blockType.get(x, y, z) == wallCode) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private int adjacentWallMaterial(int xc, int yc, int z) {
        for (int x = xc - 1; x <= xc + 1; x++) {
            for (int y = yc - 1; y <= yc + 1; y++) {
                if (localMap.inMap(x, y, z)) {
                    if (localMap.blockType.get(x, y, z) == wallCode) {
                        return localMap.blockType.getMaterial(x, y, z);
                    }
                }
            }
        }
        return 0;
    }

    private boolean isFloorCell(int x, int y, int z) {
        return localMap.blockType.get(x, y, z) == BlockTypeEnum.SPACE.CODE &&
                localMap.blockType.get(x, y, z - 1) == BlockTypeEnum.WALL.CODE;
    }
}