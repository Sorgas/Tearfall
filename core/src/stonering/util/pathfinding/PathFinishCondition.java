package stonering.util.pathfinding;

import static stonering.enums.action.ActionTargetTypeEnum.*;
import static stonering.enums.blocks.BlockTypeEnum.RAMP;
import static stonering.enums.blocks.PassageEnum.PASSABLE;

import stonering.enums.action.ActionTargetTypeEnum;
import stonering.game.GameMvc;
import stonering.game.model.local_map.LocalMap;
import stonering.util.geometry.Position;
import stonering.util.geometry.PositionUtil;

import java.util.HashSet;
import java.util.Set;

/**
 * Stores positions in which path can end and checks given position.
 *
 * @author Alexander on 15.11.2019.
 */
public class PathFinishCondition {
    public final Set<Position> acceptable = new HashSet<>();

    public PathFinishCondition(Position target, ActionTargetTypeEnum targetType) {
        if (targetType == EXACT || targetType == ANY) acceptable.add(target);
        if(targetType == NEAR || targetType == ANY) { // add near tiles
            LocalMap map = GameMvc.model().get(LocalMap.class); // add near tiles
            PositionUtil.allNeighbour.stream()
                    .map(delta -> Position.add(target, delta))
                    .filter(map::inMap)
                    .filter(pos -> map.passageMap.passage.get(pos) == PASSABLE.VALUE)
                    .forEach(acceptable::add);
            PositionUtil.allNeighbour.stream()
                    .map(delta -> Position.add(target, delta))
                    .map(position -> position.add(0, 0, -1))
                    .filter(map::inMap)
                    .filter(pos -> map.blockType.get(pos) == RAMP.CODE)
                    .forEach(acceptable::add);
        }
        if (targetType != NEAR && targetType != ANY) return;
    }

    public boolean check(Position current) {
        return acceptable.contains(current);
    }
}
