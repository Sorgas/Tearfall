package stonering.generators.localgen.generators;

import stonering.enums.BlockTypesEnum;
import stonering.game.core.model.LocalMap;
import stonering.generators.localgen.LocalGenContainer;
import stonering.utils.Position;

/**
 * Created by Alexander on 17.10.2017.
 */
public class LocalRampPlacer {
    private LocalGenContainer container;
    private LocalMap map;

    int wallCode = BlockTypesEnum.WALL.getCode();
    int spaceCode = BlockTypesEnum.SPACE.getCode();
    int rampCode = BlockTypesEnum.RAMP.getCode();

    private void addRamps() {
        for (int x = 0; x < map.getxSize(); x++) {
            for (int y = 0; y < map.getxSize(); y++) {
                for (int z = 0; z < map.getzSize(); z++) {
                    if (isGround(x, y, z)) {
                        map.setBlock(x, y, z, BlockTypesEnum.WALL, adjacentWallMaterial(x,y,z));
                    }
                }
            }
        }
    }

    private void placeRamp(int x, int y, int z, int walls) {

//        int rampCode = 0;
//        if ((walls & 0b00001010) == 0b00001010) {
//            rampCode = ;//NW
//        } else if ((walls & 0b01010000) == 0b01010000) {
//            map.getCell(pos).setCellTypeId(3);//SE
//        } else if ((walls & 0b00010010) == 0b00010010) {
//            map.getCell(pos).setCellTypeId(4);//SW
//        } else if ((walls & 0b01001000) == 0b01001000) {
//            map.getCell(pos).setCellTypeId(5);//NE
//
//        } else if ((walls & 0b00010000) != 0) {
//            map.getCell(pos).setCellTypeId(6);//E
//        } else if ((walls & 0b01000000) != 0) {
//            map.getCell(pos).setCellTypeId(7);//S
//        } else if ((walls & 0b00000010) != 0) {
//            map.getCell(pos).setCellTypeId(8);//N
//        } else if ((walls & 0b00001000) != 0) {
//            map.getCell(pos).setCellTypeId(9);//W
//
//        } else if ((walls & 0b10000000) != 0) {
//            map.getCell(pos).setCellTypeId(10);//se
//        } else if ((walls & 0b00000100) != 0) {
//            map.getCell(pos).setCellTypeId(11);//ne
//        } else if ((walls & 0b00100000) != 0) {
//            map.getCell(pos).setCellTypeId(12);//sw
//        } else if ((walls & 0b00000001) != 0) {
//            map.getCell(pos).setCellTypeId(13);//nw
//        }
//        map.setBlock();
    }

    private boolean isGround(int x, int y, int z) {
        if (map.getBlockType(x, y, z) == spaceCode) {
            if (z > 0) {
                if (map.getBlockType(x, y, z - 1) == wallCode) {
                    return true;
                }
            }
        }
        return false;
    }

    private int adjacentWallMaterial(int x, int y, int z) {
        for (int yOffset = -1; yOffset < 2; yOffset++) {
            for (int xOffset = -1; xOffset < 2; xOffset++) {
                if (checkPosition(x + xOffset, y + yOffset, z)) {
                    if (map.getBlockType(x + xOffset, y + yOffset, z) == wallCode) {
                        map.getMaterial(x + xOffset, y + yOffset, z);
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
                        if (map.getBlockType(x + xOffset, y + yOffset, z) == wallCode) {
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
        if ((x < map.getxSize()) && (x >= 0)) {
            if ((y < map.getySize()) && (y >= 0)) {
                if ((z < map.getzSize()) && (z >= 0)) {
                    return true;
                }
            }
        }
        return false;
    }
}
