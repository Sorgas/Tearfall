package stonering.game.model.local_map.passage;

import stonering.game.model.local_map.LocalMap;
import stonering.util.geometry.Position;

import java.util.function.Predicate;

/**
 * Class for checking paths existence by game entities.
 *
 * @author Alexander on 13.11.2019.
 */
public class PassageUtil {
    private PassageMap passage;

    public PassageUtil(LocalMap map, PassageMap passage) {
        this.passage = passage;
    }

    public boolean positionReachable(Position from, Position to, boolean acceptNearTarget) {
        if (from == null || to == null) return false;
        int fromArea = passage.area.get(from);
        if (passage.area.get(to) == fromArea) return true; // target in same area
        return acceptNearTarget && new NeighbourPositionStream(to)
                .stream.map(passage.area::get)
                .anyMatch(Predicate.isEqual(fromArea)); // near tile in same area
    }
}
