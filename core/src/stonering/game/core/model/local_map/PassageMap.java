package stonering.game.core.model.local_map;

import stonering.game.core.model.util.UtilByteArray;
import stonering.util.geometry.Position;
import stonering.util.pathfinding.a_star.AStar;

import java.util.*;

/**
 * Manages isolated areas on localMap to prevent pathfinding between them.
 * Updates areas on local map change.
 *
 * @author Alexander on 05.11.2018.
 */
public class PassageMap {
    private LocalMap localMap;
    private UtilByteArray area; // number of area
    private UtilByteArray passage; // stores
    private Map<Byte, Integer> areaNumbers; // counts number of cells in areas

    public PassageMap(LocalMap localMap) {
        this.localMap = localMap;
        areaNumbers = new HashMap<>();
        area = new UtilByteArray(localMap.getxSize(), localMap.getySize(), localMap.getzSize());
        passage = new UtilByteArray(localMap.getxSize(), localMap.getySize(), localMap.getzSize());
    }

    /**
     * Called when local map passage is updated. If cell becomes non-passable, it may split area into two.
     */
    public void updateCell(int x, int y, int z) {
        if (localMap.isWalkPassable(x, y, z)) { // areas should be merged
            area.setValue(x, y, z, 0);
            passage.setValue(x, y, z, 1);
            Set<Byte> areas = observeAreasAround(x, y, z);
            if (areas.size() > 1) {
                mergeAreas(areas);
            }
        } else { // areas may split
            passage.setValue(x, y, z, 0);
            Set<Position> positions = hasNonPassableNear(x, y, z);
            if (!positions.isEmpty()) splitAreas(positions, new Position(x, y, z));
        }
    }

    private Set<Byte> observeAreasAround(int cx, int cy, int cz) {
        Set<Byte> neighbours = new HashSet<>();
        for (int x = cx - 1; x < cx + 2; x++) {
            for (int y = cy - 1; y < cy + 2; y++) {
                for (int z = cz - 1; z < cz + 2; z++) {
                    if (localMap.inMap(x, y, z))
                        neighbours.add(area.getValue(x, y, z));
                }
            }
        }
        neighbours.remove((byte) 0);
        return neighbours;
    }

    /**
     * Observes only non-diagonal cells.
     */
    private Set<Position> hasNonPassableNear(int cx, int cy, int cz) {
        Set<Position> blockedPositions = new HashSet<>();
        if (localMap.isWalkPassable(cx + 1, cy, cz)) blockedPositions.add(new Position(1, 0, 0));
        if (localMap.isWalkPassable(cx - 1, cy, cz)) blockedPositions.add(new Position(-1, 0, 0));
        if (localMap.isWalkPassable(cx, cy + 1, cz)) blockedPositions.add(new Position(0, 1, 0));
        if (localMap.isWalkPassable(cx, cy - 1, cz)) blockedPositions.add(new Position(0, -1, 0));
        return blockedPositions;
    }

    /**
     * Refills areas to the left from given positions.
     *
     * @param positions Should be offsets from center, and have only 1 non-zero value.
     */
    private void splitAreas(Set<Position> positions, Position center) {
        positions.remove(positions.iterator().next()); // single wall does not split areas
        positions.forEach(position -> {
            int x = position.x;
            int y = position.y;
            Position target = Position.add(center, x == 0 ? -y : x, y == 0 ? x : y, 0); // "counter clockwise"
            Position target1 = Position.add(center, x == 0 ? y : x, y == 0 ? -x : y, 0);
            AStar aStar = new AStar(localMap);
            if (aStar.makeShortestPath(target, target1, true) == null) { // no path anymore, split
                for (byte i = 0; i < Byte.MAX_VALUE; i++) {
                    if (!areaNumbers.keySet().contains(i)) {
                        new AreaFiller(localMap).fill(target, i); // refill isolated area with new number
                    }
                }
            }
        });
    }

    private void mergeAreas(Set<Byte> areas) {
        Optional<Byte> largestArea = areas.stream().max(Comparator.comparingInt(o -> areaNumbers.get(o)));
        if (!largestArea.isPresent()) return; //TODO
        areas.remove(largestArea);
        HashMap<Byte, Integer> areaSizes = new HashMap<>();
        areas.forEach(aByte -> areaSizes.put(aByte, areaNumbers.get(aByte)));
        for (int x = 0; x < localMap.getxSize(); x++) {
            for (int y = 0; y < localMap.getySize(); y++) {
                for (int z = 0; z < localMap.getzSize(); z++) {
                    if (areas.contains(area.getValue(x, y, z))) {
                        area.setValue(x, y, z, largestArea.get());
                    }
                }
            }
        }
    }

    //sells should be adjacent
    private boolean hasPathBetween(int x, int y, int z, int x2, int y2, int z2) {
        if (localMap.isWalkPassable(x, y, z) && localMap.isWalkPassable(x2, y2, z2)) {
            if (z == z2) {
                return true;
            } else if (x == x2 && y == y2) {
                return localMap.isWorkingStair(x, y, Math.min(z, z2));
            } else {
                if (z < z2) {
                    return localMap.isWorkingRamp(x, y, z);
                } else {
                    return localMap.isWorkingRamp(x2, y2, z2);
                }
            }
        }
        return false;
    }

    public UtilByteArray getArea() {
        return area;
    }

    public UtilByteArray getPassage() {
        return passage;
    }
}
