package stonering.generators.localgen;

import stonering.game.core.model.LocalMap;
import stonering.generators.localgen.generators.LocalHeightsGenerator;
import stonering.generators.localgen.generators.LocalRiverGenerator;
import stonering.generators.localgen.generators.LocalStoneLayersGenerator;
import stonering.generators.worldgen.WorldMap;
import stonering.utils.Position;
import stonering.utils.Vector;

/**
 * Created by Alexander on 27.08.2017.
 */
public class LocalGeneratorContainer {
    private LocalGenContainer localGenContainer;

    private LocalHeightsGenerator localHeightsGenerator;
    private LocalStoneLayersGenerator localStoneLayersGenerator;
    private LocalRiverGenerator riverGenerator;

    private WorldMap world;
    private LocalMap localMap;
    private Position location;
    private LocalGenConfig config;

    public LocalGeneratorContainer(LocalGenConfig config, WorldMap world) {
        this.world = world;
        this.location = config.getLocation();
        this.config = config;
        init();
    }

    public void init() {
        localGenContainer = new LocalGenContainer(config, world);
        localHeightsGenerator = new LocalHeightsGenerator(localGenContainer);
        localStoneLayersGenerator = new LocalStoneLayersGenerator(localGenContainer);

    }

    public void execute() {
        localHeightsGenerator.execute();
        localStoneLayersGenerator.execute();
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
        return localGenContainer.getLocalMap();
    }

    public void setWorld(WorldMap world) {
        this.world = world;
    }

    public void setLocation(Position location) {
        this.location = location;
    }
}
