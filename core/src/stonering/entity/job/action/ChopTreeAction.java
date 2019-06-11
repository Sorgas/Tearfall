package stonering.entity.job.action;

import stonering.entity.job.designation.Designation;
import stonering.entity.job.action.target.PositionActionTarget;
import stonering.entity.local.items.Item;
import stonering.entity.local.items.selectors.ItemSelector;
import stonering.entity.local.items.selectors.ToolWithActionItemSelector;
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
        PlantBlock block = GameMvc.instance().getModel().get(PlantContainer.class).getPlantBlocks().get(actionTarget.getPosition());
        if (block == null) return false;
        if(toolItemSelector.check(aspect.getEquippedItems())) return true;

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
        PlantBlock block = GameMvc.instance().getModel().get(PlantContainer.class).getPlantBlocks().get(actionTarget.getPosition());
        AbstractPlant plant = block.getPlant();
        if (plant.getType().isTree()) {
            cutTree((Tree) plant);
        } else {
            cutPlant((Plant) plant);
        }
    }

    private void cutTree(Tree tree) {
        GameMvc.instance().getModel().get(PlantContainer.class).removeTree(tree, true);
    }

    private void cutPlant(Plant plant) {
        GameMvc.instance().getModel().get(PlantContainer.class).removePlant(plant);
    }

    private void logStart() {
        Logger.TASKS.logDebug("tree chopping started at " + actionTarget.getPosition().toString() + " by " + task.getPerformer().toString());
    }

    @Override
    public String toString() {
        return "Chopping tree action";
    }
}
