package com.model.generator.local;

import com.model.localmap.LocalMap;
import com.model.localmap.MapCell;
import com.model.utils.Position;

/**
 * factory class for creating LocalMap objects
 */
public class LocalMapGenerator {
    private LocalMap map;

    public void createFlatMap(int xSize, int ySize) {
        map = new LocalMap(xSize, ySize, 256);
        generateFlat(100);
        createHill(new Position(xSize / 2, ySize / 2, 100), 5);
        addRamps();
    }

    private void generateFlat(int groundLeveel) {
        for (int x = 0; x < map.getxSize(); x++) {
            for (int y = 0; y < map.getxSize(); y++) {
                for (int z = 0; z < groundLeveel; z++) {
                    Position position = new Position(x, y, z);
                    MapCell cell = new MapCell(position, 1, true, true);
                    map.setCell(cell);
                }
                for (int z = groundLeveel; z < 256; z++) {
                    Position position = new Position(x, y, z);
                    MapCell cell = new MapCell(position, 0, false, false);
                    map.setCell(cell);
                }
            }
        }
    }

    private void createHill(Position pos, int height) {
        Position center = new Position(pos.getX(), pos.getY(), pos.getZ() - height * 4);
        int radius = height * 5;
        for (int x = center.getX() - radius; x < center.getX() + radius; x++) {
            for (int y = center.getY() - radius; y < center.getY() + radius; y++) {
                for (int z = center.getZ() - radius; z < center.getZ() + radius; z++) {
                    Position position = new Position(x, y, z);
                    if (checkPosition(position)) {
                        if (Math.sqrt(Math.pow(center.getX() - x, 2) + Math.pow(center.getY() - y, 2) + Math.pow(center.getZ() - z, 2)) <= radius) {
                            MapCell cell = new MapCell(position, 1, true, true);
                            map.setCell(cell);
                        }
                    }
                }
            }
        }
    }

    private void addRamps() {
        int count = 0;
        for (int x = 0; x < map.getxSize(); x++) {
            for (int y = 0; y < map.getxSize(); y++) {
                for (int z = 0; z < map.getzSize(); z++) {
                    if (isGround(new Position(x, y, z))) {
                        count++;
                        Position pos = new Position(x, y, z);
                        placeRamp(pos, observeWalls(pos));
                    }
                }
            }
        }
    }

    private int observeWalls(Position pos) {
        int bitpos = 1;
        int walls = 0;
        for (int yOffset = -1; yOffset < 2; yOffset++) {
            for (int xOffset = -1; xOffset < 2; xOffset++) {
                if ((xOffset != 0) || (yOffset != 0)) {
                    Position pos2 = new Position(pos.getX() + xOffset, pos.getY() + yOffset, pos.getZ());
//                    System.out.println(pos2.toString());
//                    System.out.println(map.getCell(pos2).getCellTypeId());
                    if (checkPosition(pos2)) {
                        if (map.getCell(pos2).getCellTypeId() == 1) {
                            walls |= bitpos;
                        }
                    }
                    bitpos *= 2;
                }
            }
        }
        return walls;
    }

    private void placeRamp(Position pos, int walls) {
        if ((walls & 0b00001010) == 0b00001010) {
            map.getCell(pos).setCellTypeId(2);//NW
        } else if ((walls & 0b01010000) == 0b01010000) {
            map.getCell(pos).setCellTypeId(3);//SE
        } else if ((walls & 0b00010010) == 0b00010010) {
            map.getCell(pos).setCellTypeId(4);//SW
        } else if ((walls & 0b01001000) == 0b01001000) {
            map.getCell(pos).setCellTypeId(5);//NE

        } else if ((walls & 0b00010000) != 0) {
            map.getCell(pos).setCellTypeId(6);//E
        } else if ((walls & 0b01000000) != 0) {
            map.getCell(pos).setCellTypeId(7);//S
        } else if ((walls & 0b00000010) != 0) {
            map.getCell(pos).setCellTypeId(8);//N
        } else if ((walls & 0b00001000) != 0) {
            map.getCell(pos).setCellTypeId(9);//W

        } else if ((walls & 0b10000000) != 0) {
            map.getCell(pos).setCellTypeId(10);//se
        } else if ((walls & 0b00000100) != 0) {
            map.getCell(pos).setCellTypeId(11);//ne
        } else if ((walls & 0b00100000) != 0) {
            map.getCell(pos).setCellTypeId(12);//sw
        } else if ((walls & 0b00000001) != 0) {
            map.getCell(pos).setCellTypeId(13);//nw
        }
    }

    private boolean checkPosition(Position pos) {
        if ((pos.getX() < map.getxSize()) && (pos.getX() >= 0)) {
            if ((pos.getY() < map.getySize()) && (pos.getY() >= 0)) {
                if ((pos.getZ() < map.getzSize()) && (pos.getZ() >= 0)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isGround(Position pos) {
        MapCell cell = map.getCell(pos);
        if (cell.getCellTypeId() == 0) {
            if (pos.getZ() > 0) {
                if ((map.getCell(pos.getX(), pos.getY(), pos.getZ() - 1).getCellTypeId() == 1)) {
                    return true;
                }
            }
        }
        return false;
    }

    public LocalMap getMap() {
        return map;
    }

    public void setMap(LocalMap map) {
        this.map = map;
    }
}