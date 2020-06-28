package stonering.util.pathfinding;

import stonering.enums.action.ActionTargetTypeEnum;
import stonering.enums.blocks.BlockTypeEnum;
import stonering.enums.blocks.PassageEnum;
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

    public PathFinishCondition(Position target, ActionTargetTypeEnum targetType) {
        neighbours = new HashSet<>();
        switch (targetType) {
            case EXACT:
                neighbours.add(target);
                break;
            case NEAR:
                neighbours.addAll(new NeighbourPositionStream(target)
                        .filterSameZLevel()
                        .filterByPassage(PassageEnum.PASSABLE)
                        .stream.collect(Collectors.toSet()));
                break;
            case ANY:
                neighbours.add(target);
                neighbours.addAll(new NeighbourPositionStream(target)
                        .filterSameZLevel()
                        .filterByPassage(PassageEnum.PASSABLE)
                        .stream.collect(Collectors.toSet()));
        }
    }

    public boolean check(Position current) {
        return neighbours.contains(current);
    }
}