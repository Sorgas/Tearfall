package stonering.util.pathfinding.a_star;

import stonering.enums.action.ActionTargetTypeEnum;
import stonering.game.GameMvc;
import stonering.game.model.local_map.LocalMap;
import stonering.game.model.local_map.passage.NeighbourPositionStream;
import stonering.util.geometry.Position;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static stonering.enums.action.ActionTargetTypeEnum.EXACT;
import static stonering.enums.action.ActionTargetTypeEnum.NEAR;

/**
 * Stores positions in which path can end and checks given position.
 *
 * @author Alexander on 15.11.2019.
 */
public class PathFinishCondition {
    private Set<Position> neighbours;

    public PathFinishCondition(Position target, ActionTargetTypeEnum targetType) {
        neighbours = new HashSet<>();
        switch (targetType) {
            case EXACT:
                break;
            case NEAR:
                break;
            case ANY:
                break;
        }
        if(targetType != NEAR) neighbours.add(target); // exact or any
        if(targetType != EXACT) { // near or any
            neighbours = new NeighbourPositionStream(target, GameMvc.instance().model().get(LocalMap.class).passage)
                    .filterByPassability().stream.collect(Collectors.toSet());
        }
    }

    public boolean check(Position current) {
        return neighbours.contains(current);
    }
}
