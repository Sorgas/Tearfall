package stonering.util.pathfinding.a_star;

import stonering.entity.job.action.target.ActionTarget;
import stonering.game.GameMvc;
import stonering.game.model.local_map.LocalMap;
import stonering.game.model.local_map.passage.NeighbourPositionStream;
import stonering.util.geometry.Position;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Stores positions in which path can end and checks given position.
 *
 * @author Alexander on 15.11.2019.
 */
public class PathFinishCondition {
    private Set<Position> neighbours;
    private boolean exact;

    public PathFinishCondition(Position target, int targetPlacement) {
        this.exact = exact;
        neighbours = new HashSet<>();
        if(targetPlacement != ActionTarget.NEAR) neighbours.add(target); // exact or any
        if(targetPlacement != ActionTarget.EXACT) { // near or any
            neighbours = new HashSet<>(new NeighbourPositionStream(target, GameMvc.instance().model().get(LocalMap.class).passage)
                    .filterByPassability().stream.collect(Collectors.toSet()));
        }
    }

    public boolean check(Position current) {
        return neighbours.contains(current);
    }
}
