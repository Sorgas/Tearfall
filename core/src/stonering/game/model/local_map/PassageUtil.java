package stonering.game.model.local_map;

import stonering.entity.Entity;
import stonering.util.geometry.Position;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Class for checking paths existence by game entities.
 *
 * @author Alexander on 13.11.2019.
 */
public class PassageUtil {
    private LocalMap map;
    private Position cachePosition;

    public PassageUtil(LocalMap map) {
        this.map = map;
        cachePosition = new Position();
    }


    public boolean positionReachable(Position from, Position to, boolean acceptNearTarget) {
        if (map.passage.area.get(to) == map.passage.area.get(from)) return true; // target in same area
        return acceptNearTarget && getNeighbours(to).stream().anyMatch(position -> map.passage.area.get(position) == map.passage.area.get(from)); // near tile in same area
    }

    public <T extends Entity> List<T> filterEntitiesByReachability(List<T> entities, Position target) {
        return entities.stream().
                filter(entity -> entity.position != null).
                filter(entity -> map.passage.area.get(entity.position) == map.passage.area.get(target)).
                collect(Collectors.toList());
    }

    public boolean entityReachable(Entity entity, Position from) {
        return entity.position != null && map.passage.area.get(from) == map.passage.area.get(entity.position);
    }

    /**
     * Returns neighbour positions, accessible from given one.
     */
    private Set<Position> getNeighbours(Position center) {
        Set<Position> neighbours = new HashSet<>();
        for (int x = center.x - 1; x < center.x + 2; x++) {
            for (int y = center.y - 1; y < center.y + 2; y++) {
                for (int z = center.z - 1; z < center.z + 2; z++) {
                    if (x == center.x && y == center.y && z == center.z) continue; // same position
                    if (map.inMap(x, y, z))
                        neighbours.add(cachePosition.set(center.x + x, center.y + y, center.z + z).clone());
                }
            }
        }
        return neighbours;
    }
}
