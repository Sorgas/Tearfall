package stonering.entity.job.action.plant;

import static stonering.entity.job.action.ActionConditionStatusEnum.*;

import stonering.entity.job.action.Action;
import stonering.entity.job.action.ActionConditionStatusEnum;
import stonering.entity.job.action.equipment.use.EquipToolItemAction;
import stonering.entity.job.action.target.ActionTarget;
import stonering.entity.item.Item;
import stonering.entity.item.selectors.ItemSelector;
import stonering.entity.item.selectors.ToolWithActionItemSelector;
import stonering.entity.job.action.target.PositionActionTarget;
import stonering.entity.job.designation.Designation;
import stonering.entity.unit.aspects.equipment.EquipmentAspect;
import stonering.enums.ZoneTypeEnum;
import stonering.enums.action.ActionTargetTypeEnum;
import stonering.enums.blocks.BlockTypeEnum;
import stonering.game.GameMvc;
import stonering.game.model.system.item.ItemContainer;
import stonering.game.model.system.ZoneContainer;
import stonering.game.model.local_map.LocalMap;
import stonering.util.geometry.Position;
import stonering.util.logging.Logger;

/**
 * Action for preparing farm soil for planting plants.
 *
 * @author Alexander on 20.03.2019.
 */
public class HoeingAction extends Action {
    
    public HoeingAction(Designation designation) {
        super(new PositionActionTarget(designation.position, ActionTargetTypeEnum.NEAR));

        // target tile is soil FLOOR
        // target tile is farm zone
        // performer has hoe tool
        // Creates sub name only for equipping hoe, other cases handled by player.
        startCondition = () -> {
            Position targetPosition = target.getPosition();
            if (!ZoneTypeEnum.FARM.VALIDATOR.apply(targetPosition)) return FAIL;
            if (GameMvc.model().get(ZoneContainer.class).getZone(targetPosition) == null) return FAIL;
            EquipmentAspect equipmentAspect = task.performer.get(EquipmentAspect.class);
            if(equipmentAspect.toolWithActionEquipped("hoe")) return OK;
            return tryCreateEquippingAction();
        };

        onStart = () -> maxProgress = 200;
        
        onFinish = () -> {
            Logger.TASKS.logDebug("Hoeing tile " + target.getPosition());
            LocalMap localMap = GameMvc.model().get(LocalMap.class);
            localMap.blockType.set(target.getPosition(), BlockTypeEnum.FARM.CODE);
        };
    }

    private ActionConditionStatusEnum tryCreateEquippingAction() {
        ItemSelector toolItemSelector = new ToolWithActionItemSelector("hoe");
        return GameMvc.model().optional(ItemContainer.class)
                .map(container -> container.util.getItemAvailableBySelector(toolItemSelector, target.getPosition()))
                .map(EquipToolItemAction::new)
                .map(this::addPreAction)
                .orElse(FAIL);
    }
}
