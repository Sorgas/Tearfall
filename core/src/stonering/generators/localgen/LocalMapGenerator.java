package stonering.generators.localgen;

import stonering.game.core.model.LocalMap;
import stonering.game.enums.BlockType;
import stonering.generators.worldgen.WorldMap;
import stonering.utils.Position;
import stonering.utils.Vector;

/**
 * factory class for creating LocalMap objects
 */
public class LocalMapGenerator {
    private LocalMap localMap;
    private WorldMap world;
    private Position location;
    private LocalGenConfig config;
    private int progress;

    private float[][] poligonTops;

    public LocalMapGenerator() {
        config = new LocalGenConfig();
    }

    public void execute() {
        generateFlat();
    }

    private void generateFlat() {
        localMap = new LocalMap(config.getAreaSize(), config.getAreaSize(), config.getAreaHight());
        System.out.println();
        System.out.print("generating local");
        progress = 0;
        int localElevation = world.getElevation(location.getX(), location.getY());
        if (validateWorldAndLocation()) {
            for (int x = 0; x < config.getAreaSize(); x++) {
                for (int y = 0; y < config.getAreaSize(); y++) {
                    for (int z = 0; z < config.getAreaHight(); z++) {
                        if (z < localElevation) {
                            localMap.setBlock(x, y, z, BlockType.WALL);
                        } else {
                            localMap.setBlock(x, y, z, BlockType.SPACE);
                        }
                    }
                }
            }
        }
    }

    private void generatreMap() {
        if (validateWorldAndLocation()) {
            localMap = new LocalMap(config.getAreaSize(), config.getAreaSize(), config.getAreaHight());
            int elevation = world.getElevation(location.getX(), location.getY()) * config.getWorldToLocalElevationModifier() + config.getLocalSeaLevel();
            for (int x = 0; x < config.getAreaSize(); x++) {
                for (int y = 0; y < config.getAreaSize(); y++) {
                    for (int z = 0; z < elevation; z++) {

                    }
                }
            }
        }
    }

    private void countPoligonTops() {
        poligonTops = new float[3][3];
        for (int x = 0; x < 2; x++) {
            for (int y = 0; y < 2; y++) {
                poligonTops[x * 2][y * 2] = calculateMidElevation(location.getX() - 1 + x, location.getY() - 1 + y);
            }
        }

    }

    private float calculateMidElevation(int x, int y) {
        float midElevation = world.getElevation(x, y);
        midElevation += world.getElevation(x + 1, y);
        midElevation += world.getElevation(x, y + 1);
        return (midElevation + world.getElevation(x + 1, y + 1)) / 4;
    }

    private Vector calculateSlope() {
        int[][] area = new int[3][3];
        Vector vector = new Vector(0, 0, 0, 0);
        for (int x = location.getX() - 1; x < location.getX() + 1; x++) {
            for (int y = location.getY() - 1; y < location.getY() + 1; y++) {
                area[x][y] = world.getElevation(x, y) * 8 + 100;
            }
        }

        return vector;
    }

    private void addRamps() {
        for (int x = 0; x < localMap.getxSize(); x++) {
            for (int y = 0; y < localMap.getxSize(); y++) {
                for (int z = 0; z < localMap.getzSize(); z++) {
                    if (isGround(new Position(x, y, z))) {
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
//                    System.out.println(localMap.getCell(pos2).getCellTypeId());
                    if (checkPosition(pos2)) {
//                        if (localMap.getCell(pos2).getCellTypeId() == 1) {
//                            walls |= bitpos;
//                        }
                    }
                    bitpos *= 2;
                }
            }
        }
        return walls;
    }

    private void placeRamp(Position pos, int walls) {
//        if ((walls & 0b00001010) == 0b00001010) {
//            localMap.getCell(pos).setCellTypeId(2);//NW
//        } else if ((walls & 0b01010000) == 0b01010000) {
//            localMap.getCell(pos).setCellTypeId(3);//SE
//        } else if ((walls & 0b00010010) == 0b00010010) {
//            localMap.getCell(pos).setCellTypeId(4);//SW
//        } else if ((walls & 0b01001000) == 0b01001000) {
//            localMap.getCell(pos).setCellTypeId(5);//NE
//
//        } else if ((walls & 0b00010000) != 0) {
//            localMap.getCell(pos).setCellTypeId(6);//E
//        } else if ((walls & 0b01000000) != 0) {
//            localMap.getCell(pos).setCellTypeId(7);//S
//        } else if ((walls & 0b00000010) != 0) {
//            localMap.getCell(pos).setCellTypeId(8);//N
//        } else if ((walls & 0b00001000) != 0) {
//            localMap.getCell(pos).setCellTypeId(9);//W
//
//        } else if ((walls & 0b10000000) != 0) {
//            localMap.getCell(pos).setCellTypeId(10);//se
//        } else if ((walls & 0b00000100) != 0) {
//            localMap.getCell(pos).setCellTypeId(11);//ne
//        } else if ((walls & 0b00100000) != 0) {
//            localMap.getCell(pos).setCellTypeId(12);//sw
//        } else if ((walls & 0b00000001) != 0) {
//            localMap.getCell(pos).setCellTypeId(13);//nw
//        }
    }

    private boolean checkPosition(Position pos) {
        if ((pos.getX() < localMap.getxSize()) && (pos.getX() >= 0)) {
            if ((pos.getY() < localMap.getySize()) && (pos.getY() >= 0)) {
                if ((pos.getZ() < localMap.getzSize()) && (pos.getZ() >= 0)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isGround(Position pos) {
//        MapCell cell = localMap.getCell(pos);
//        if (cell.getCellTypeId() == 0) {
//            if (pos.getZ() > 0) {
//                if ((localMap.getCell(pos.getX(), pos.getY(), pos.getZ() - 1).getCellTypeId() == 1)) {
//                    return true;
//                }
//            }
//        }
        return false;
    }

    private boolean validateWorldAndLocation() {
        return world != null && location != null &&
                location.getX() >= 0 && location.getX() < world.getWidth() &&
                location.getY() >= 0 && location.getY() < world.getHeight();
    }

    public LocalMap getLocalMap() {
        return localMap;
    }

    public void setWorld(WorldMap world) {
        this.world = world;
    }

    public void setLocation(Position location) {
        this.location = location;
    }

    public int getProgress() {
        return progress;
    }
}