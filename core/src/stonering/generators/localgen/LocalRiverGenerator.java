package stonering.generators.localgen;

import stonering.game.core.model.LocalMap;
import stonering.game.enums.BlockType;
import stonering.generators.worldgen.WorldMap;
import stonering.utils.Position;
import stonering.utils.Vector;

/**
 * Created by Alexander on 10.07.2017.
 */
public class LocalRiverGenerator {
    private WorldMap map;
    private LocalMap localMap;
    private Position location;
    private int localElevation;

    public LocalRiverGenerator(WorldMap map, LocalMap localMap, Position location, int localElevation) {
        this.map = map;
        this.localMap = localMap;
        this.location = location;
        this.localElevation = localElevation;
    }

    public void execute() {
        generateRivers();
    }

    private void generateRivers() {
        int[][] rivers = checkRivers();
        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 3; y++) {
                if (rivers[x][y] != 0) {
                    if (x != 1 || y != 1) {
                        placeRiver(x - 1, y - 1);
                    }
                }
            }
        }
    }

    private void placeRiver(int dx, int dy) {
        System.out.println("placing river");
        int x = localMap.getxSize() / 2;
        int y = localMap.getySize() / 2;
        while (inLocalMap(x, y)) {
            localMap.setBlock(x, y, localElevation - 1, BlockType.SPACE);
            x += dx;
            y += dy;
        }
    }

    private int[][] checkRivers() {
        //1 for incoming stream, -1 for outcoming stream
        int[][] rivers = new int[3][3];
        for (int x = -1; x < 2; x++) {
            for (int y = -1; y < 2; y++) {
                if (inWorldMap(location.getX() + x, location.getY() + y)) {
                    Position neighbourRegion = new Position(location.getX() + x, location.getY() + y, 0);
                    if (map.getRivers().containsKey(neighbourRegion)) {
                        for (Vector vector : map.getRivers().get(neighbourRegion)
                                ) {
                            if (vector.getEndPoint().equals(location)) {
                                rivers[x + 1][y + 1] = 1;
                            }
                        }
                    }
                }
            }
        }
        if (map.getRivers().containsKey(location)) {
            for (Vector vector : map.getRivers().get(location)) {
                if (neihgbourToLocation(vector.getEndPoint())) {
                    rivers[1 + vector.getEndPoint().getX() - location.getX()][1 + vector.getEndPoint().getY() - location.getY()] = -1;
                }
            }
        }
        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 3; y++) {
                System.out.println(rivers[x][y]);
            }
        }
        return rivers;
    }

    private boolean inWorldMap(int x, int y) {
        if (x < 0 || x >= map.getWidth()) return false;
        if (y < 0 || y >= map.getHeight()) return false;
        return true;
    }

    private boolean inLocalMap(int x, int y) {
        if (x < 0 || x >= localMap.getxSize()) return false;
        if (y < 0 || y >= localMap.getySize()) return false;
        return true;
    }

    private boolean neihgbourToLocation(Position position) {
        if ((location.getX() - position.getX()) >= -1 && (location.getX() - position.getX()) <= 1) {
            if ((location.getY() - position.getY()) >= -1 && (location.getY() - position.getY()) <= 1) {
                return true;
            }
        }
        return false;
    }
}