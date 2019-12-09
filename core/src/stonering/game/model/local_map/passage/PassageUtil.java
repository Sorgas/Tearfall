package stonering.game.model.local_map.passage;

import stonering.entity.Entity;
import stonering.game.model.local_map.LocalMap;
import stonering.util.geometry.Position;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Class for checking paths existence by game entities.
 *
 * @author Alexander on 13.11.2019.
 */
public class PassageUtil {
    private LocalMap map;
    private PassageMap passage;
    private Position cachePosition;

    public PassageUtil(LocalMap map, PassageMap passage) {
        this.map = map;
        this.passage = passage;
        cachePosition = new Position();
    }

    public boolean positionReachable(Position from, Position to, boolean acceptNearTarget) {
        if (passage.area.get(to) == passage.area.get(from)) return true; // target in same area
        return acceptNearTarget && new NeighbourPositionStream(to)
                .stream.map(passage.area::get)
                .anyMatch(Predicate.isEqual(passage.area.get(from))); // near tile in same area
    }

    //TODO replace with util stream
    public <T extends Entity> List<T> filterEntitiesByReachability(List<T> entities, Position target) {
        return entities.stream().
                filter(entity -> entity.position != null).
                filter(entity -> passage.area.get(entity.position) == passage.area.get(target)).
                collect(Collectors.toList());
    }
}
