package stonering.game.core.model.local_map;

import stonering.util.geometry.Position;

import java.util.HashSet;
import java.util.Set;

/**
 * Fills isolated areas.
 *
 * @author Alexander on 02.02.2019.
 */
public class AreaFiller {
    private LocalMap localMap;
    private Set<Position> openSet;

    public AreaFiller(LocalMap localMap) {
        this.localMap = localMap;
        openSet = new HashSet<>();
    }

    public int fill(Position start, byte value) {
        int counter = 0;
        openSet.add(start);
        for(;!openSet.isEmpty(); counter++) {
            Position position = openSet.iterator().next();
            openSet.remove(position);
            localMap.getPassageMap().getArea().setValue(position, value);
            openSet.addAll(getNeighbours(position, value));
        }
        return counter;
    }

    private Set<Position> getNeighbours(Position center, byte blockedArea) {
        Set<Position> neighbours = new HashSet<>();
        for (int x = center.x - 1; x < center.x + 2; x++) {
            for (int y = center.y - 1; y < center.y + 2; y++) {
                for (int z = center.z - 1; z < center.z + 2; z++) {
                    if (x == center.x || y == center.y || z == center.z) continue; // same position
                    Position position = new Position(center.x + x, center.y + y, center.z + z);
                    if (!localMap.inMap(position)) continue;             // neighbour is out of map
                    if (localMap.getPassageMap().getArea().getValue(position) == blockedArea) continue; // same area
                    if (localMap.hasPathBetween(center, position)) neighbours.add(position);
                }
            }
        }
        return neighbours;
    }
}
