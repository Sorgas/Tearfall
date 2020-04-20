package stonering.game.model.tilemaps;

import stonering.enums.blocks.BlockTypeEnum;
import stonering.enums.blocks.BlockTileMapping;
import stonering.enums.materials.Material;
import stonering.enums.materials.MaterialMap;
import stonering.game.GameMvc;
import stonering.game.model.local_map.LocalMap;
import stonering.util.global.IntTriple;

/**
 * Updates LocalTileMap when blocks or plants on LocalMap are changed.
 * Is called from localMap, reference from other places not required.
 *
 * @author Alexander Kuzyakov on 03.08.2017.
 */
public class LocalTileMapUpdater {
    private LocalMap localMap;
    private LocalTileMap localTileMap;

    /**
     * Updates all tiles on local map.
     */
    public void flushLocalMap() {
        if (GameMvc.instance() == null) return;
        localMap = GameMvc.model().get(LocalMap.class);
        for (int x = 0; x < localMap.xSize; x++) {
            for (int y = 0; y < localMap.ySize; y++) {
                for (int z = 0; z < localMap.zSize; z++) {
                    updateTile(x, y, z);
                }
            }
        }
    }

    /**
     * Updates single tile. Called from {@link LocalMap} when tile is changed.
     */
    public void updateTile(int x, int y, int z) {
        localMap = GameMvc.model().get(LocalMap.class);
        localTileMap = GameMvc.model().get(LocalTileMap.class);
        MaterialMap materialMap = MaterialMap.instance();
        BlockTypeEnum blockType = localMap.blockType.getEnumValue(x, y, z);
        switch (blockType) {
            case SPACE: // remove tile sprite
                localTileMap.removeTile(x, y, z);
                break;
            case RAMP: // select sprite basing on surrounding tiles
                localTileMap.setTile(x, y, z,
                        countRamp(x, y, z),
                        materialMap.getMaterial(localMap.blockType.getMaterial(x, y, z)).atlasY,
                        0);
                break;
            default: // set sprite of the tile
                localTileMap.setTile(x, y, z,
                        BlockTileMapping.getType(blockType.CODE).ATLAS_X,
                        materialMap.getMaterial(localMap.blockType.getMaterial(x, y, z)).atlasY, 0);
        }
        updateRampsAround(x, y, z);
    }

    /**
     * Observes tiles around given one, and updates atlasX for ramps.
     */
    private void updateRampsAround(int xc, int yc, int z) {
        for (int y = yc - 1; y < yc + 2; y++) {
            for (int x = xc - 1; x < xc + 2; x++) {
                if (!localMap.inMap(x, y, z) || localMap.blockType.get(x, y, z) != BlockTypeEnum.RAMP.CODE) continue;
                IntTriple triple = localTileMap.get(x, y, z);
                localTileMap.setTile(x, y, z, countRamp(x, y, z), triple.getVal2(), triple.getVal3());
            }
        }
    }

    /**
     * Chooses ramp tile by surrounding walls.
     *
     * @return ramp atlas X
     */
    private byte countRamp(int x, int y, int z) {
        int walls = observeWalls(x, y, z);
        if ((walls & 0b00001010) == 0b00001010) {
            return BlockTileMapping.RAMP_SW.ATLAS_X;
        } else if ((walls & 0b01010000) == 0b01010000) {
            return BlockTileMapping.RAMP_NE.ATLAS_X;
        } else if ((walls & 0b00010010) == 0b00010010) {
            return BlockTileMapping.RAMP_SE.ATLAS_X;
        } else if ((walls & 0b01001000) == 0b01001000) {
            return BlockTileMapping.RAMP_NW.ATLAS_X;
        } else if ((walls & 0b00010000) != 0) {
            return BlockTileMapping.RAMP_E.ATLAS_X;
        } else if ((walls & 0b01000000) != 0) {
            return BlockTileMapping.RAMP_N.ATLAS_X;
        } else if ((walls & 0b00000010) != 0) {
            return BlockTileMapping.RAMP_S.ATLAS_X;
        } else if ((walls & 0b00001000) != 0) {
            return BlockTileMapping.RAMP_W.ATLAS_X;
        } else if ((walls & 0b10000000) != 0) {
            return BlockTileMapping.RAMP_NEO.ATLAS_X;
        } else if ((walls & 0b00000100) != 0) {
            return BlockTileMapping.RAMP_SEO.ATLAS_X;
        } else if ((walls & 0b00100000) != 0) {
            return BlockTileMapping.RAMP_NWO.ATLAS_X;
        } else if ((walls & 0b00000001) != 0) {
            return BlockTileMapping.RAMP_SWO.ATLAS_X;
        } else
            return BlockTileMapping.FLOOR.ATLAS_X;
    }

    /**
     * Counts neighbour walls to choose ramp type and orientation.
     *
     * @return int representing adjacent walls.
     */
    public int observeWalls(int cx, int cy, int cz) {
        int bitpos = 1;
        int walls = 0;
        for (int y = cy - 1; y <= cy + 1; y++) {
            for (int x = cx - 1; x <= cx + 1; x++) {
                if ((x == cx) && (y == cy)) continue;
                if (localMap.blockType.get(x, y, cz) == BlockTypeEnum.WALL.CODE) walls |= bitpos;
                bitpos *= 2; // shift to 1 bit
            }
        }
        return walls;
    }
}
