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

    public void fill(Position start, byte value) {
        openSet.add(start);
        while (!openSet.isEmpty()) {
            Position position = openSet.iterator().next();
            openSet.remove(position);
            localMap.getPassageMap().getArea().setValue(position, value);
            openSet.addAll(getNeighbours(position, value));
        }
    }

    private Set<Position> getNeighbours(Position center, byte blockedArea) {
        Set<Position> neighbours = new HashSet<>();
        for (int x = center.x - 1; x < center.x + 2; x++) {
            for (int y = center.y - 1; y < center.y + 2; y++) {
                for (int z = center.z - 1; z < center.z + 2; z++) {
                    if (x != center.x && y != center.y && z != center.z
                            && localMap.getPassageMap().getArea().getValue(center.x + x, center.y + y, center.z + z) != blockedArea) {
                        Position position = new Position(center.x + x, center.y + y, center.z + z);
                        if (localMap.hasPathBetween(center, position))
                            neighbours.add(position);
                    }
                }
            }
        }
        return neighbours;
    }
}
