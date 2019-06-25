package stonering.entity.job.action;

import stonering.entity.job.designation.Designation;
import stonering.entity.job.action.target.PositionActionTarget;
import stonering.entity.local.item.Item;
import stonering.entity.local.item.selectors.ItemSelector;
import stonering.entity.local.item.selectors.ToolWithActionItemSelector;
import stonering.entity.local.plants.AbstractPlant;
import stonering.entity.local.plants.Plant;
import stonering.entity.local.plants.PlantBlock;
import stonering.entity.local.plants.Tree;
import stonering.entity.local.unit.aspects.equipment.EquipmentAspect;
import stonering.game.GameMvc;
import stonering.game.model.lists.ItemContainer;
import stonering.game.model.lists.PlantContainer;
import stonering.util.global.Logger;

public class ChopTreeAction extends Action {
    private ItemSelector toolItemSelector;

    public ChopTreeAction(Designation designation) {
        super(new PositionActionTarget(designation.getPosition(), false, true));
        toolItemSelector = new ToolWithActionItemSelector("chop");
    }

    @Override
    public boolean check() {
        EquipmentAspect aspect = task.getPerformer().getAspect(EquipmentAspect.class);
        if (aspect == null) return false;
        if (!GameMvc.instance().getModel().get(PlantContainer.class).isPlantBlockExists(actionTarget.getPosition()))
            return false;
        if (toolItemSelector.check(aspect.getEquippedItems())) return true;

        Logger.TASKS.logDebug("No tool equipped by performer for chopTreeAction");
        Item target = GameMvc.instance().getModel().get(ItemContainer.class).getItemAvailableBySelector(toolItemSelector, task.getPerformer().getPosition());
        if (target != null) return addActionToTask(target);
        Logger.TASKS.logDebug("No tool item found for chopTreeAction");
        return false;
    }

    private boolean addActionToTask(Item target) {
        task.addFirstPreAction(new EquipItemAction(target, true));
        return true;
    }

    @Override
    public void performLogic() {
        logStart();
        PlantContainer container = GameMvc.instance().getModel().get(PlantContainer.class);
        AbstractPlant plant = container.getPlantInPosition(actionTarget.getPosition());
        if (plant.getType().isTree()) container.remove(plant, true);
    }

    private void logStart() {
        Logger.TASKS.logDebug("tree chopping started at " + actionTarget.getPosition().toString() + " by " + task.getPerformer().toString());
    }

    @Override
    public String toString() {
        return "Chopping tree action";
    }
}
