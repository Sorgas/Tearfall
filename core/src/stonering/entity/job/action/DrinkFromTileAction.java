package stonering.entity.job.action;

import static stonering.entity.job.action.ActionConditionStatusEnum.FAIL;
import static stonering.entity.job.action.ActionConditionStatusEnum.OK;

import java.util.Optional;

import stonering.entity.job.action.target.PositionActionTarget;
import stonering.entity.unit.aspects.health.HealthAspect;
import stonering.enums.action.ActionTargetTypeEnum;
import stonering.enums.materials.MaterialMap;
import stonering.enums.unit.health.HealthParameterEnum;
import stonering.game.GameMvc;
import stonering.game.model.system.liquid.LiquidContainer;
import stonering.game.model.system.unit.UnitContainer;
import stonering.util.geometry.Position;

/**
 * Action for drinking water from tiles like brooks or ponds.
 * Creature will fully restore it's thirst.
 * 
 * @author Alexander on 13.07.2020.
 */
public class DrinkFromTileAction extends Action {

    public DrinkFromTileAction(Position waterPosition) {
        super(new PositionActionTarget(waterPosition, ActionTargetTypeEnum.NEAR));
        
        startCondition = () -> {
            if(!task.performer.has(HealthAspect.class)) return FAIL;
            return Optional.ofNullable(GameMvc.model().get(LiquidContainer.class).getTile(target.getPosition()))
                    .map(tile -> tile.liquid)
                    .filter(material -> material == MaterialMap.getId("water"))
                    .map(material -> OK).orElse(FAIL);
        };
        
        // set max progress to creature thirst
        onStart = () -> {
            maxProgress = task.performer.get(HealthAspect.class).parameters.get(HealthParameterEnum.THIRST).get();
        };
        
        // subtract max progress from creature thirst
        onFinish = () -> {
            GameMvc.model().get(UnitContainer.class).healthSystem.changeParameter(task.performer, HealthParameterEnum.THIRST, -maxProgress);
        };
    }
}
