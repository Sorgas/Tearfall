package stonering.entity.job.action;

import static stonering.entity.job.action.ActionConditionStatusEnum.FAIL;
import static stonering.entity.job.action.ActionConditionStatusEnum.OK;
import static stonering.enums.unit.need.NeedEnum.WATER;

import java.util.Objects;
import java.util.stream.Stream;

import stonering.entity.job.action.target.PositionActionTarget;
import stonering.entity.unit.aspects.health.HealthAspect;
import stonering.entity.unit.aspects.need.NeedAspect;
import stonering.enums.action.ActionTargetTypeEnum;
import stonering.enums.materials.MaterialMap;
import stonering.game.GameMvc;
import stonering.game.model.system.liquid.LiquidContainer;
import stonering.util.geometry.Position;

/**
 * Action for drinking water from tiles like brooks or ponds.
 * Creature will fully restore it's thirst.
 * Water tile can be on same z-level or 1 level below target.
 *
 * @author Alexander on 13.07.2020.
 */
public class DrinkFromTileAction extends Action {

    public DrinkFromTileAction(Position waterPosition) {
        super(new PositionActionTarget(waterPosition, ActionTargetTypeEnum.ANY));

        startCondition = () -> {
            if (!task.performer.has(HealthAspect.class)) return FAIL;
            int waterId = MaterialMap.getId("water");
            return Stream.of(target.getPosition(), Position.add(target.getPosition(), 0, 0, -1))
                    .map(GameMvc.model().get(LiquidContainer.class)::getTile)
                    .filter(Objects::nonNull)
                    .anyMatch(tile -> tile.liquid == waterId) ? OK : FAIL;
        };

        // decrease thirst
        progressConsumer = progress -> task.performer.get(NeedAspect.class).needs.get(WATER).changeValue(-0.01f);

        // finish when thirst quenched
        finishCondition = () -> task.performer.get(NeedAspect.class).needs.get(WATER).current() <= 0;
    }
}
