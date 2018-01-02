package stonering.game.core.view.tilemaps;

import stonering.enums.blocks.BlockTypesEnum;
import stonering.enums.blocks.BlocksTileMapping;
import stonering.enums.materials.MaterialMap;
import stonering.enums.trees.TreeTileMapping;
import stonering.game.core.model.GameContainer;
import stonering.game.core.model.LocalMap;
import stonering.global.utils.Position;
import stonering.objects.jobs.Task;
import stonering.objects.jobs.actions.Action;
import stonering.objects.jobs.actions.ActionTypeEnum;
import stonering.objects.local_actors.unit.Unit;

/**
 * Created by Alexander on 03.08.2017.
 * <p>
 * Updates LocalTileMap when blocks or plants on LocalMap changed
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
            int atlasY = materialMap.getMaterial(localMap.getMaterial(x, y, z)).getAtlasY();
            int atlasX;
            if (blockType == BlockTypesEnum.RAMP.getCode()) {
                atlasX = countRamp(x, y, z);
            } else {
                atlasX = BlocksTileMapping.getType(blockType).getAtlasX();
            }
            localTileMap.setTile(x, y, z, atlasX, atlasY, 0, null);
        }
        updateRampsAround(x, y, z);
    }

    private void updateRampsAround(int xc, int yc, int z) {
        for (int y = yc - 1; y < yc + 2; y++) {
            for (int x = xc - 1; x < xc + 2; x++) {
                if (checkPosition(x, y, z) && localMap.getBlockType(x, y, z) == BlockTypesEnum.RAMP.getCode()) {
                    localTileMap.setTile(x, y, z, countRamp(x, y, z), localTileMap.getAtlasY(x, y, z), 0, null);
                }
            }
        }
    }

    private byte countRamp(int x, int y, int z) {
        int walls = observeWalls(x, y, z);
        if ((walls & 0b00001010) == 0b00001010) {
            return BlocksTileMapping.RAMP_N2.getAtlasX();//NW
        } else if ((walls & 0b01010000) == 0b01010000) {
            return BlocksTileMapping.RAMP_S2.getAtlasX();//SE
        } else if ((walls & 0b00010010) == 0b00010010) {
            return BlocksTileMapping.RAMP_E2.getAtlasX();//SW
        } else if ((walls & 0b01001000) == 0b01001000) {
            return BlocksTileMapping.RAMP_W2.getAtlasX();//NE

        } else if ((walls & 0b00010000) != 0) {
            return BlocksTileMapping.RAMP_SE.getAtlasX();//E
        } else if ((walls & 0b01000000) != 0) {
            return BlocksTileMapping.RAMP_SW.getAtlasX();//S
        } else if ((walls & 0b00000010) != 0) {
            return BlocksTileMapping.RAMP_NE.getAtlasX();//N
        } else if ((walls & 0b00001000) != 0) {
            return BlocksTileMapping.RAMP_NW.getAtlasX();//W

        } else if ((walls & 0b10000000) != 0) {
            return BlocksTileMapping.RAMP_N.getAtlasX();//se
        } else if ((walls & 0b00000100) != 0) {
            return BlocksTileMapping.RAMP_E.getAtlasX();//ne
        } else if ((walls & 0b00100000) != 0) {
            return BlocksTileMapping.RAMP_W.getAtlasX();//sw
        } else if ((walls & 0b00000001) != 0) {
            return BlocksTileMapping.RAMP_S.getAtlasX();//nw
        }
        return BlocksTileMapping.RAMP_N.getAtlasX(); //similar to se
    }

    private int observeWalls(int x, int y, int z) {
        int bitpos = 1;
        int walls = 0;
        for (int yOffset = -1; yOffset < 2; yOffset++) {
            for (int xOffset = -1; xOffset < 2; xOffset++) {
                if ((xOffset != 0) || (yOffset != 0)) {
                    if (checkPosition(x + xOffset, y + yOffset, z)) {
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
