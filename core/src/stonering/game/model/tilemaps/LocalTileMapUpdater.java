package stonering.game.model.tilemaps;

import static stonering.enums.blocks.BlockTypeEnum.RAMP;
import static stonering.enums.blocks.BlockTypeEnum.SPACE;

import com.badlogic.gdx.graphics.Color;

import stonering.enums.blocks.BlockTypeEnum;
import stonering.enums.blocks.BlockTileMapping;
import stonering.enums.materials.MaterialMap;
import stonering.game.GameMvc;
import stonering.game.model.local_map.LocalMap;
import stonering.util.geometry.Position;
import stonering.util.geometry.PositionUtil;

/**
 * Updates LocalTileMap when blocks or plants on LocalMap are changed.
 * Is called from {@link LocalMap}.
 *
 * @author Alexander Kuzyakov on 03.08.2017.
 */
public class LocalTileMapUpdater {
    private LocalMap localMap;
    private LocalTileMap localTileMap;
    private Position cachePosition = new Position();

    /**
     * Updates all tiles on local map.
     */
    public void flushLocalMap() {
        if (GameMvc.instance() == null) return;
        localMap = GameMvc.model().get(LocalMap.class);
        for (int x = 0; x < localMap.xSize; x++) {
            for (int y = 0; y < localMap.ySize; y++) {
                for (int z = 0; z < localMap.zSize; z++) {
                    updateTile(cachePosition.set(x, y, z));
                }
            }
        }
    }

    /**
     * Updates single tile. Called from {@link LocalMap} when tile is changed.
     */
    public void updateTile(Position position) {
        localMap = GameMvc.model().get(LocalMap.class);
        localTileMap = GameMvc.model().get(LocalTileMap.class);
        BlockTypeEnum blockType = localMap.blockType.getEnumValue(position);
        if(blockType == SPACE) {
            localTileMap.removeTile(position);
        } else {
            int tileX = blockType == RAMP
                    ? countRamp(position) // select ramp tile
                    : BlockTileMapping.getType(blockType.CODE).ATLAS_X; // select tile from block type
            localTileMap.setTile(position, tileX, MaterialMap.getMaterial(localMap.blockType.getMaterial(position)).atlasY, Color.WHITE);
        }
        updateRampsAround(position);
    }

    /**
     * Observes tiles around given one, and updates atlasX for ramps.
     */
    private void updateRampsAround(Position center) {
        PositionUtil.allNeighbour.stream()
                .map(delta -> Position.add(center, delta)) // get absolute position
                .filter(localMap::inMap)
                .filter(pos -> localMap.blockType.get(pos) == BlockTypeEnum.RAMP.CODE)
                .forEach(pos -> {
                    SpriteDescriptor triple = localTileMap.get(pos);
                    localTileMap.setTile(pos, countRamp(pos), triple.y, triple.color);
                });
    }

    /**
     * Chooses ramp tile by surrounding walls.
     *
     * @return ramp atlas X
     */
    private byte countRamp(Position position) {
        int walls = observeWalls(position.x, position.y, position.z);
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