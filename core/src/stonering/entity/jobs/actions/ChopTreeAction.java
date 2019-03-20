package stonering.entity.jobs.actions;

import stonering.designations.Designation;
import stonering.entity.jobs.actions.target.PositionActionTarget;
import stonering.entity.local.items.Item;
import stonering.entity.local.items.selectors.ItemSelector;
import stonering.entity.local.items.selectors.ToolWithActionItemSelector;
import stonering.entity.local.plants.AbstractPlant;
import stonering.entity.local.plants.Plant;
import stonering.entity.local.plants.PlantBlock;
import stonering.entity.local.plants.Tree;
import stonering.entity.local.unit.aspects.equipment.EquipmentAspect;
import stonering.game.model.lists.ItemContainer;
import stonering.game.model.lists.PlantContainer;
import stonering.game.model.local_map.LocalMap;
import stonering.util.global.TagLoggersEnum;

public class ChopTreeAction extends Action {
    private ItemSelector toolItemSelector;

    public ChopTreeAction(Designation designation) {
        super(new PositionActionTarget(designation.getPosition(), false, true));
        toolItemSelector = new ToolWithActionItemSelector("chop");
    }

    @Override
    public boolean check() {
        EquipmentAspect aspect = (EquipmentAspect) task.getPerformer().getAspects().get("equipment");
        if (aspect == null) return false;
        PlantBlock block = gameMvc.getModel().get(LocalMap.class).getPlantBlock(actionTarget.getPosition());
        if (block == null) return false;
        return toolItemSelector.check(aspect.getEquippedItems()) || addActionToTask();
    }

    private boolean addActionToTask() {
        Item target = gameMvc.getModel().get(ItemContainer.class).getItemAvailableBySelector(toolItemSelector, task.getPerformer().getPosition());
        if (target == null) return false;

        EquipItemAction equipItemAction = new EquipItemAction(target, true);
        task.addFirstPreAction(equipItemAction);
        return true;
    }

    @Override
    public void performLogic() {
        logStart();
        PlantBlock block = gameMvc.getModel().get(LocalMap.class).getPlantBlock(actionTarget.getPosition());
        AbstractPlant plant = block.getPlant();
        if (plant.getType().isTree()) {
            cutTree((Tree) plant);
        } else {
            cutPlant((Plant) plant);
        }
    }

    private void cutTree(Tree tree) {
        gameMvc.getModel().get(PlantContainer.class).removeTree(tree);
    }

    private void cutPlant(Plant plant) {
        gameMvc.getModel().get(PlantContainer.class).removePlant(plant);
    }

    private void logStart() {
        TagLoggersEnum.TASKS.logDebug("tree chopping started at " + actionTarget.getPosition().toString() + " by " + task.getPerformer().toString());
    }

    @Override
    public String toString() {
        return "Chopping tree action";
    }
}
