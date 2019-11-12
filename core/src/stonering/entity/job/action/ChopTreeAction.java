package stonering.entity.job.action;

import stonering.entity.job.action.target.ActionTarget;
import stonering.entity.job.designation.Designation;
import stonering.entity.job.action.target.PositionActionTarget;
import stonering.entity.item.Item;
import stonering.entity.item.selectors.ItemSelector;
import stonering.entity.item.selectors.ToolWithActionItemSelector;
import stonering.entity.plants.AbstractPlant;
import stonering.entity.plants.PlantBlock;
import stonering.entity.unit.aspects.equipment.EquipmentAspect;
import stonering.game.GameMvc;
import stonering.game.model.system.item.ItemContainer;
import stonering.game.model.system.PlantContainer;
import stonering.util.global.Logger;

/**
 * Action for chopping trees.
 */
public class ChopTreeAction extends Action {
    private ItemSelector toolItemSelector;

    public ChopTreeAction(Designation designation) {
        super(new PositionActionTarget(designation.position, ActionTarget.NEAR));
        toolItemSelector = new ToolWithActionItemSelector("chop");
    }

    /**
     * Checks that tree exists on target position, fails if it doesn't.
     * Checks that performer has chopping tool, creates equipping action if needed.
     */
    @Override
    public int check() {
        Logger.TASKS.logDebug("Checking chopping action for " + actionTarget.getPosition());
        if (!task.performer.hasAspect(EquipmentAspect.class)) return FAIL; // check aspect
        PlantBlock block = GameMvc.instance().getModel().get(PlantContainer.class).getPlantBlock(actionTarget.getPosition());
        if (block == null || !block.getPlant().getType().isTree()) // check tree
            return Logger.TASKS.logDebug("No tree in target position", FAIL);
        if (!toolItemSelector.checkItems(task.performer.getAspect(EquipmentAspect.class).equippedItems)) // check tool
            return createActionForGettingTool();
        return OK;
    }

    private int createActionForGettingTool() {
        Logger.TASKS.logDebug("No tool equipped by performer for chopTreeAction");
        Item target = GameMvc.instance().getModel().get(ItemContainer.class).util.getItemAvailableBySelector(toolItemSelector, task.performer.position);
        if (target == null) Logger.TASKS.logDebug("No tool item found for chopTreeAction", FAIL);
        task.addFirstPreAction(new EquipItemAction(target, true));
        return NEW;
    }

    @Override
    public void performLogic() {
        Logger.TASKS.logDebug("tree chopping started at " + actionTarget.getPosition().toString() + " by " + task.performer.toString());
        if (check() != OK) return; // tree died during chopping. rare case
        PlantContainer container = GameMvc.instance().getModel().get(PlantContainer.class);
        AbstractPlant plant = container.getPlantInPosition(actionTarget.getPosition());
        if (plant.getType().isTree()) container.remove(plant, true);
    }

    @Override
    public String toString() {
        return "Chopping tree name";
    }
}
