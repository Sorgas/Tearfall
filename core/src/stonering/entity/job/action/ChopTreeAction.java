package stonering.entity.job.action;

import stonering.entity.job.action.equipment.EquipToolItemAction;
import stonering.entity.job.designation.Designation;
import stonering.entity.job.action.target.PositionActionTarget;
import stonering.entity.item.Item;
import stonering.entity.item.selectors.ItemSelector;
import stonering.entity.item.selectors.ToolWithActionItemSelector;
import stonering.entity.plant.AbstractPlant;
import stonering.entity.plant.PlantBlock;
import stonering.entity.unit.aspects.equipment.EquipmentAspect;
import stonering.enums.action.ActionTargetTypeEnum;
import stonering.game.GameMvc;
import stonering.game.model.system.item.ItemContainer;
import stonering.game.model.system.plant.PlantContainer;
import stonering.util.logging.Logger;

import static stonering.entity.job.action.ActionConditionStatusEnum.*;

/**
 * Action for chopping trees. Trees leave logs for each trunk block.
 */
public class ChopTreeAction extends Action {
    private ItemSelector toolItemSelector;

    public ChopTreeAction(Designation designation) {
        super(new PositionActionTarget(designation.position, ActionTargetTypeEnum.NEAR));
        toolItemSelector = new ToolWithActionItemSelector("chop");


        // Checks that tree exists on target position, fails if it doesn't.
        // Checks that performer has chopping tool, creates equipping action if needed.
        startCondition = () -> {
            Logger.TASKS.logDebug("Checking " + this);
            EquipmentAspect aspect = task.performer.get(EquipmentAspect.class);
            if (aspect == null) return FAIL; // no aspect on performer
            if(!checkTree()) return FAIL;
            if (!toolItemSelector.checkItems(aspect.equippedItems)) // check tool
                return createActionForGettingTool();
            return OK;
        };

        onFinish = () -> {
            Logger.TASKS.logDebug("tree chopping started at " + target.getPosition().toString() + " by " + task.performer.toString());
            if (!checkTree()) return; // tree died during chopping. rare case // TODO move to progress acceptor
            PlantContainer container = GameMvc.model().get(PlantContainer.class);
            AbstractPlant plant = container.getPlantInPosition(target.getPosition());
            if (plant.type.isTree) container.remove(plant, true);
        };
    }

    /**
     * Checks that tree still exists.
     */
    public boolean checkTree() {
        PlantBlock block = GameMvc.model().get(PlantContainer.class).getPlantBlock(target.getPosition());
        if (block != null && block.getPlant().type.isTree) return true;
        Logger.TASKS.logDebug("No tree in target position");
        return false;
    }

    private ActionConditionStatusEnum createActionForGettingTool() {
        Logger.TASKS.logDebug("No tool equipped by performer for chopTreeAction");
        Item target = GameMvc.model().get(ItemContainer.class).util.getItemAvailableBySelector(toolItemSelector, task.performer.position);
        if (target == null) Logger.TASKS.logDebug("No tool item found for chopTreeAction", FAIL);
        task.addFirstPreAction(new EquipToolItemAction(target));
        return NEW;
    }

    @Override
    public String toString() {
        return "Chopping tree on " + target.getPosition();
    }
}
