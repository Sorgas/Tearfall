package stonering.entity.job.action;

import stonering.entity.job.action.equipment.EquipToolItemAction;
import stonering.entity.job.action.target.ActionTarget;
import stonering.entity.item.Item;
import stonering.entity.item.selectors.ItemSelector;
import stonering.entity.item.selectors.ToolWithActionItemSelector;
import stonering.entity.unit.aspects.equipment.EquipmentAspect;
import stonering.enums.ZoneTypesEnum;
import stonering.enums.blocks.BlockTypeEnum;
import stonering.game.GameMvc;
import stonering.game.model.system.item.ItemContainer;
import stonering.game.model.system.ZoneContainer;
import stonering.game.model.local_map.LocalMap;
import stonering.util.geometry.Position;
import stonering.util.global.Logger;

import static stonering.entity.job.action.ActionConditionStatusEnum.FAIL;
import static stonering.entity.job.action.ActionConditionStatusEnum.OK;

/**
 * @author Alexander on 20.03.2019.
 */
public class HoeingAction extends Action {

    public HoeingAction(ActionTarget actionTarget) {
        super(actionTarget);

        // target tile is soil FLOOR
        // target tile is farm zone
        // performer has hoe tool
        // Creates sub name only for equipping hoe, other cases handled by player.
        startCondition = () -> {
            //        Logger.TASKS.logDebug("Checking hoeing of " + actionTarget.getPosition());
            Position target = actionTarget.getPosition();
            LocalMap localMap = GameMvc.model().get(LocalMap.class);
            if (!ZoneTypesEnum.FARM.VALIDATOR.apply(target)) return FAIL; // 1
            if (GameMvc.model().get(ZoneContainer.class).getZone(target) == null) return FAIL; // 2
            EquipmentAspect equipmentAspect = task.performer.get(EquipmentAspect.class);
            if(equipmentAspect.toolWithActionEquipped("hoe")) return OK;
            return tryCreateEquippingAction();
        };

        onFinish = () -> {
            Logger.TASKS.logDebug("Hoeing tile " + actionTarget.getPosition());
            LocalMap localMap = GameMvc.model().get(LocalMap.class);
            localMap.blockType.set(actionTarget.getPosition(), BlockTypeEnum.FARM.CODE);
        };
    }

    private ActionConditionStatusEnum tryCreateEquippingAction() {
        Logger.TASKS.logDebug("Creating equipping action of hoe");
        ItemSelector toolItemSelector = new ToolWithActionItemSelector("hoe");
        ItemContainer itemContainer = GameMvc.model().get(ItemContainer.class);
        Item item = itemContainer.util.getItemAvailableBySelector(toolItemSelector, target.getPosition());
        if(item == null) return FAIL;
        task.addFirstPreAction(new EquipToolItemAction(item));
        return OK;
    }
}
