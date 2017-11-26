package stonering.generators.localgen.generators;

import stonering.enums.blocks.BlockTypesEnum;
import stonering.enums.materials.MaterialMap;
import stonering.game.core.model.LocalMap;
import stonering.generators.localgen.LocalGenConfig;
import stonering.generators.localgen.LocalGenContainer;
import stonering.generators.worldgen.WorldMap;

/**
 * Created by Alexander on 17.10.2017.
 */
public class LocalRampPlacer {
    private LocalGenContainer container;
    private LocalMap localMap;
    private MaterialMap materialMap;
    private WorldMap worldMap;
    private LocalGenConfig config;
    private int localAreaSize;

    public LocalRampPlacer(LocalGenContainer container) {
        this.container = container;
        this.config = container.getConfig();
        this.localAreaSize = config.getAreaSize();
        this.localMap = container.getLocalMap();
    }

    int wallCode = BlockTypesEnum.WALL.getCode();
    int spaceCode = BlockTypesEnum.SPACE.getCode();
    int rampCode = BlockTypesEnum.RAMP.getCode();

    public void execute() {
        for (int x = 0; x < localMap.getxSize(); x++) {
            for (int y = 0; y < localMap.getxSize(); y++) {
                for (int z = 1; z < localMap.getzSize(); z++) {
                    if (isGround(x, y, z) && hasAdjacentWall(x,y,z)) {

                        localMap.setBlock(x, y, z, BlockTypesEnum.RAMP, adjacentWallMaterial(x, y, z));
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
                if (checkPosition(x, y, z)) {
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
                if (checkPosition(x, y, z)) {
                    if (localMap.getBlockType(x, y, z) == wallCode) {
                        return localMap.getMaterial(x, y, z);
                    }
                }
            }
        }
        return 0;
    }

    private int observeWalls(int x, int y, int z) {
        int bitpos = 1;
        int walls = 0;
        for (int yOffset = -1; yOffset < 2; yOffset++) {
            for (int xOffset = -1; xOffset < 2; xOffset++) {
                if ((xOffset != 0) || (yOffset != 0)) {
                    if (checkPosition(x + xOffset, y + yOffset, z)) {
                        if (localMap.getBlockType(x + xOffset, y + yOffset, z) == wallCode) {
                            walls |= bitpos;
                        }
                    }
                    bitpos *= 2;
                }
            }
        }
        return walls;
    }

    private boolean checkPosition(int x, int y, int z) {
        if ((x < localMap.getxSize()) && (x >= 0)) {
            if ((y < localMap.getySize()) && (y >= 0)) {
                if ((z < localMap.getzSize()) && (z >= 0)) {
                    return true;
                }
            }
        }
        return false;
    }
}