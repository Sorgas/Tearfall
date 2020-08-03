package stonering.game.model.local_map.passage;

import stonering.game.model.local_map.LocalMap;
import stonering.util.geometry.Position;
import stonering.util.geometry.PositionUtil;

/**
 * Class for checking paths existence by game entities.
 *
 * @author Alexander on 13.11.2019.
 */
public class PassageUtil {
    private PassageMap passage;
    private LocalMap map;
    
    public PassageUtil(LocalMap map, PassageMap passage) {
        this.map = map;
        this.passage = passage;
    }

    public boolean positionReachable(Position from, Position to, boolean acceptNearTarget) {
        if (from == null || to == null) return false;
        byte fromArea = passage.area.get(from);
        if (passage.area.get(to) == fromArea) return true; // target in same area
        
        return acceptNearTarget && PositionUtil.allNeighbour.stream()
                .map(pos -> Position.add(to, pos))
                .filter(map::inMap)
                .map(passage.area::get)
                .anyMatch(area -> area == fromArea);
    }
}
