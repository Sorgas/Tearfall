package stonering.game.core.model.local_map;

import stonering.game.core.model.util.UtilByteArray;
import stonering.util.geometry.Position;
import stonering.util.global.TagLoggersEnum;
import stonering.util.pathfinding.a_star.AStar;

import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

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
        area = new UtilByteArray(localMap.xSize, localMap.ySize, localMap.zSize);
        passage = new UtilByteArray(localMap.xSize, localMap.ySize, localMap.zSize);
    }

    /**
     * Called when local map passage is updated. If cell becomes non-passable, it may split area into two.
     */
    public void updateCell(int x, int y, int z) {
        if (localMap.isWalkPassable(x, y, z)) { // areas should be merged
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

    /**
     * Return area numbers around given position.
     */
    private Set<Byte> observeAreasAround(int cx, int cy, int cz) {
        Set<Byte> neighbours = new HashSet<>();
        for (int x = cx - 1; x < cx + 2; x++) {
            for (int y = cy - 1; y < cy + 2; y++) {
                for (int z = cz - 1; z < cz + 2; z++) {
                    if (localMap.inMap(x, y, z) && !(x == cx && y == cy && z == cz))
                        neighbours.add(area.getValue(x, y, z));
                }
            }
        }
        neighbours.remove((byte) 0);
        return neighbours;
    }

    /**
     * Refills areas to the left from given positions.
     *
     * @param positions Should be offsets from center, and have only 1 non-zero value.
     */
    private void splitAreas(Set<Position> positions, Position center) {
        TagLoggersEnum.PATH.logDebug("Splitting areas around " + center + " in positions " + positions);
        positions.remove(positions.iterator().next()); // single wall does not split areas
        positions.forEach(offset -> {
            TagLoggersEnum.PATH.logDebug("offset " + offset);
            int x = offset.x;
            int y = offset.y;
            Position target = Position.add(center, x == 0 ? -y : x, y == 0 ? x : y, 0); // "counter clockwise"
            Position target1 = Position.add(center, x == 0 ? y : x, y == 0 ? -x : y, 0);
            TagLoggersEnum.PATH.logDebug("target1 " + target + " target2 " + target1);
            if (localMap.inMap(target) && localMap.inMap(target1) && area.getValue(target) == area.getValue(target1)) {
                if (new AStar(localMap).makeShortestPath(target, target1, true) != null) return;
                for (byte i = 0; i < Byte.MAX_VALUE; i++) {
                    if (areaNumbers.keySet().contains(i)) continue; // skip unused number
                    int filledCells = new AreaFiller(localMap).fill(target, i); // refill isolated area with new number
                    if (areaNumbers.get(area.getValue(target)) != filledCells) {
                        TagLoggersEnum.PATH.logWarn("Cells loss on areas splitting!");
                    }
                    areaNumbers.put(i, filledCells);
                    TagLoggersEnum.PATH.logDebug("New area created: " + i + " size: " + areaNumbers.get(i));
                    return;
                }
            }
        });
    }

    private void mergeAreas(Set<Byte> areas) {
        TagLoggersEnum.PATH.logDebug("Merging areas " + areas);
        Optional<Byte> largestAreaOptional = areas.stream().max(Comparator.comparingInt(o -> areaNumbers.get(o)));
        if (!largestAreaOptional.isPresent()) return; //TODO
        byte largestArea = largestAreaOptional.get();
        areas.remove(largestArea);
        HashMap<Byte, Integer> areaSizes = new HashMap<>();
        areas.forEach(aByte -> areaSizes.put(aByte, areaNumbers.get(aByte)));
        for (int x = 0; x < localMap.xSize; x++) {
            for (int y = 0; y < localMap.ySize; y++) {
                for (int z = 0; z < localMap.zSize; z++) {
                    if (areas.contains(area.getValue(x, y, z))) {
                        area.setValue(x, y, z, largestArea);
                    }
                }
            }
        }
        // update merged area counters
        areas.forEach(areaNumber -> areaNumbers.put(largestArea, areaNumbers.remove(areaNumber) + areaNumbers.get(largestArea)));
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

    public UtilByteArray getArea() {
        return area;
    }

    public Map<Byte, Integer> getAreaNumbers() {
        return areaNumbers;
    }
}
