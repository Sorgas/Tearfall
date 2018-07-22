package stonering.game.core.view.tilemaps;

import stonering.enums.blocks.BlockTypesEnum;
import stonering.enums.blocks.BlocksTileMapping;
import stonering.enums.materials.Material;
import stonering.enums.materials.MaterialMap;
import stonering.game.core.model.GameContainer;
import stonering.game.core.model.LocalMap;

/**
 * Updates LocalTileMap when blocks or plants on LocalMap are changed.
 * Is called from localMap, reference from other places not required.
 *
 * @author Alexander Kuzyakov on 03.08.2017.
 */
public class LocalTileMapUpdater {
    private GameContainer container;
    private LocalMap localMap;
    private LocalTileMap localTileMap;
    private MaterialMap materialMap;

    public LocalTileMapUpdater(GameContainer container) {
        this.container = container;
        localMap = container.getLocalMap();
        localTileMap = container.getLocalTileMap();
        materialMap = MaterialMap.getInstance();
    }

    public void flushLocalMap() {
        for (int x = 0; x < localMap.getxSize(); x++) {
            for (int y = 0; y < localMap.getySize(); y++) {
                for (int z = 0; z < localMap.getzSize(); z++) {
                    updateTile(x, y, z);
                }
            }
        }
    }

    public void updateTile(int x, int y, int z) {
        localTileMap.setTile(x, y, z, 0, 0, -1, null);
        byte blockType = localMap.getBlockType(x, y, z);
        if (blockType > 0) { // non space
            Material material = materialMap.getMaterial(localMap.getMaterial(x, y, z));
            int atlasX;
            if (blockType == BlockTypesEnum.RAMP.getCode()) {
                atlasX = countRamp(x, y, z);
            } else {
                atlasX = BlocksTileMapping.getType(blockType).getAtlasX();
            }
            localTileMap.setTile(x, y, z,
                    atlasX,
                    material != null ? material.getAtlasY() : 0,
                    0, null);
        }
//        updateRampsAround(x, y, z);
    }

    private void updateRampsAround(int xc, int yc, int z) {
        for (int y = yc - 1; y < yc + 2; y++) {
            for (int x = xc - 1; x < xc + 2; x++) {
                if (localMap.inMap(x, y, z) && localMap.getBlockType(x, y, z) == BlockTypesEnum.RAMP.getCode()) {
                    localTileMap.setTile(x, y, z, countRamp(x, y, z), localTileMap.getAtlasY(x, y, z), 0, null);
                }
            }
        }
    }

    /**
     * Chooses ramp tile by surrounding walls.
     *
     * @param x
     * @param y
     * @param z
     * @return ramp atlas X
     */
    private byte countRamp(int x, int y, int z) {
        int walls = observeWalls(x, y, z);
        if ((walls & 0b00001010) == 0b00001010) {
            return BlocksTileMapping.RAMP_SW.getAtlasX();
        } else if ((walls & 0b01010000) == 0b01010000) {
            return BlocksTileMapping.RAMP_NE.getAtlasX();
        } else if ((walls & 0b00010010) == 0b00010010) {
            return BlocksTileMapping.RAMP_SE.getAtlasX();
        } else if ((walls & 0b01001000) == 0b01001000) {
            return BlocksTileMapping.RAMP_NW.getAtlasX();
        } else if ((walls & 0b00010000) != 0) {
            return BlocksTileMapping.RAMP_E.getAtlasX();
        } else if ((walls & 0b01000000) != 0) {
            return BlocksTileMapping.RAMP_N.getAtlasX();
        } else if ((walls & 0b00000010) != 0) {
            return BlocksTileMapping.RAMP_S.getAtlasX();
        } else if ((walls & 0b00001000) != 0) {
            return BlocksTileMapping.RAMP_W.getAtlasX();
        } else if ((walls & 0b10000000) != 0) {
            return BlocksTileMapping.RAMP_NEO.getAtlasX();
        } else if ((walls & 0b00000100) != 0) {
            return BlocksTileMapping.RAMP_SEO.getAtlasX();
        } else if ((walls & 0b00100000) != 0) {
            return BlocksTileMapping.RAMP_NWO.getAtlasX();
        } else if ((walls & 0b00000001) != 0) {
            return BlocksTileMapping.RAMP_SWO.getAtlasX();
        } else
            return BlocksTileMapping.FLOOR.getAtlasX();
    }

    /**
     * Counts near walls to choose ramp type and orientation.
     *
     * @param x
     * @param y
     * @param z
     * @return int representing adjacent walls.
     */
    public int observeWalls(int x, int y, int z) {
        int bitpos = 1;
        int walls = 0;
        for (int yOffset = -1; yOffset < 2; yOffset++) {
            for (int xOffset = -1; xOffset < 2; xOffset++) {
                if ((xOffset != 0) || (yOffset != 0)) {
                    if (localMap.inMap(x + xOffset, y + yOffset, z)) {
                        if (localMap.getBlockType(x + xOffset, y + yOffset, z) == BlockTypesEnum.WALL.getCode()) {
                            walls |= bitpos;
                        }
                    }
                    bitpos *= 2;
                }
            }
        }
        return walls;
    }
}
