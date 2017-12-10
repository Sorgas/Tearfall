package stonering.game.core.view.tilemaps;

import stonering.enums.blocks.BlockTypesEnum;
import stonering.enums.blocks.BlocksTileMapping;
import stonering.enums.materials.MaterialMap;
import stonering.enums.trees.TreeTileMapping;
import stonering.game.core.model.GameContainer;
import stonering.game.core.model.LocalMap;
import stonering.objects.local_actors.unit.Unit;

/**
 * Created by Alexander on 03.08.2017.
 *
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
        materialMap = new MaterialMap();
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
//        //unit
//        if (container.getUnits().stream().anyMatch(unit -> (unit.getPosition().equals(x, y, z)))) {
//            container.getUnits().stream().filter(unit -> (unit.getPosition().equals(x, y, z))).findFirst().get();
//            localTileMap.setTile(x, y, z, 0, 0, 2, null);
//            return;
//        }
//        //building
//        if (container.getBuildingContainer().stream().anyMatch(unit -> (unit.getPosition().equals(x, y, z)))) {
//            container.getBuildingContainer().stream().filter(unit -> (unit.getPosition().equals(x, y, z))).findFirst().get();
//            localTileMap.setTile(x, y, z, 0, 0, 3, null);
//            return;
//        }
        //plants
        if (localMap.getPlantBlock(x, y, z) != null) {
            int atlasX = (TreeTileMapping.getType((byte) localMap.getPlantBlock(x, y, z).getBlockType()).getAtlasX());
            int atlasY = materialMap.getAtlasY(localMap.getPlantBlock(x, y, z).getMaterial());
            localTileMap.setTile(x, y, z, atlasX, atlasY, 1, null);
            return;
        }
        //blocks
        byte blockType = localMap.getBlockType(x, y, z);
        if (blockType != 0) {
            int atlasY = materialMap.getMaterial(localMap.getMaterial(x, y, z)).getAtlasY();
            int atlasX;
            if (blockType == BlockTypesEnum.RAMP.getCode()) {
                atlasX = countRamp(x, y, z);
            } else {
                atlasX = BlocksTileMapping.getType(blockType).getAtlasX();
            }
            localTileMap.setTile(x, y, z, atlasX, atlasY, 0, null);
        }
    }

    public void updateTileUnit(Unit unit) {
        localTileMap.setTile(unit.getPosition().getX(), unit.getPosition().getY(), unit.getPosition().getZ(), 0, 0, 2, null);
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
        return 0;
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
