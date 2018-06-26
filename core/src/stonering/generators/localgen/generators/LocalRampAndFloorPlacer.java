package stonering.generators.localgen.generators;

import stonering.enums.blocks.BlockTypesEnum;
import stonering.enums.materials.MaterialMap;
import stonering.game.core.model.LocalMap;
import stonering.generators.localgen.LocalGenConfig;
import stonering.generators.localgen.LocalGenContainer;
import stonering.generators.worldgen.WorldMap;

/**
 * @author Alexander Kuzyakov on 17.10.2017.
 *
 * fills local map with ramps on z-layer borders
 */
public class LocalRampAndFloorPlacer {
    private LocalMap localMap;
    private LocalGenConfig config;
    private LocalGenContainer container;

    public LocalRampAndFloorPlacer(LocalGenContainer container) {
        this.config = container.getConfig();
        this.container = container;
    }

    int wallCode = BlockTypesEnum.WALL.getCode();
    int spaceCode = BlockTypesEnum.SPACE.getCode();
    int rampCode = BlockTypesEnum.RAMP.getCode();

    public void execute() {
        System.out.println("placing ramps");
        this.localMap = container.getLocalMap();
        fillRamps();
        fillFloors();
    }

    private void fillRamps() {
        for (int x = 0; x < localMap.getxSize(); x++) {
            for (int y = 0; y < localMap.getxSize(); y++) {
                for (int z = 1; z < localMap.getzSize(); z++) {
                    if (isGround(x, y, z) && hasAdjacentWall(x,y,z)) {

                        localMap.setBlock(x, y, z, (byte) rampCode, adjacentWallMaterial(x, y, z));
                    }
                }
            }
        }
    }

    private void fillFloors() {
        for (int x = 0; x < localMap.getxSize(); x++) {
            for (int y = 0; y < localMap.getySize(); y++) {
                for (int z = localMap.getzSize() - 1; z > 0; z--) {
                    if (isFloorCell(x, y, z)) { //non space sell
                        localMap.setBlock(x, y, z, BlockTypesEnum.FLOOR.getCode(), localMap.getMaterial(x, y, z - 1));
                    }
                }
            }
        }
    }

    private boolean isGround(int x, int y, int z) {
        return localMap.getBlockType(x, y, z) == spaceCode && localMap.getBlockType(x, y, z - 1) == wallCode;
    }

    private boolean hasAdjacentWall(int xc, int yc, int z) {
        for (int x = xc - 1; x < xc + 1; x++) {
            for (int y = yc - 1; y < yc + 1; y++) {
                if (localMap.inMap(x, y, z)) {
                    if (localMap.getBlockType(x, y, z) == wallCode) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private int adjacentWallMaterial(int xc, int yc, int z) {
        for (int x = xc - 1; x < xc + 1; x++) {
            for (int y = yc - 1; y < yc + 1; y++) {
                if (localMap.inMap(x, y, z)) {
                    if (localMap.getBlockType(x, y, z) == wallCode) {
                        return localMap.getMaterial(x, y, z);
                    }
                }
            }
        }
        return 0;
    }

    private boolean isFloorCell(int x, int y, int z) {
        return localMap.getBlockType(x, y, z) == BlockTypesEnum.SPACE.getCode() &&
                localMap.getBlockType(x, y, z - 1) == BlockTypesEnum.WALL.getCode();
    }
}