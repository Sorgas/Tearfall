package stonering.entity.job.action;

import stonering.entity.job.action.target.PlantActionTarget;
import stonering.entity.local.item.Item;
import stonering.entity.local.item.selectors.ItemSelector;
import stonering.entity.local.item.selectors.ToolWithActionItemSelector;
import stonering.entity.local.plants.PlantBlock;
import stonering.entity.local.unit.aspects.equipment.EquipmentAspect;
import stonering.game.GameMvc;
import stonering.game.model.lists.ItemContainer;
import stonering.generators.items.PlantProductGenerator;
import stonering.util.global.Logger;

/**
 * @author Alexander on 15.01.2019.
 */
public class HarvestPlantAction extends Action {
    private ItemSelector toolItemSelector;

    public HarvestPlantAction(PlantBlock plantBlock) {
        super(new PlantActionTarget(plantBlock.getPlant()));
        toolItemSelector = new ToolWithActionItemSelector("harvest_plants");
    }

    @Override
    public boolean check() {
        EquipmentAspect aspect = task.getPerformer().getAspect(EquipmentAspect.class);
        if (aspect == null) return false;
        return toolItemSelector.check(aspect.getEquippedItems()) || addActionToTask();
    }

    @Override
    public void performLogic() {
        PlantBlock plantBlock = ((PlantActionTarget) actionTarget).getPlant().getBlock();
        Item item = new PlantProductGenerator().generateHarvestProduct(plantBlock);
        GameMvc.instance().getModel().get(ItemContainer.class).putItem(item, actionTarget.getPosition());
        Logger.TASKS.logDebug("harvesting plant finished at " + actionTarget.getPosition() + " by " + task.getPerformer());
    }

    private boolean addActionToTask() {
        Item target = GameMvc.instance().getModel().get(ItemContainer.class).getItemAvailableBySelector(toolItemSelector, task.getPerformer().getPosition());
        if (target == null) return false;
        EquipItemAction equipItemAction = new EquipItemAction(target, true);
        task.addFirstPreAction(equipItemAction);
        return true;
    }
}
