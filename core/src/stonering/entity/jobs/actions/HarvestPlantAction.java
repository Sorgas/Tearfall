package stonering.entity.jobs.actions;

import stonering.entity.jobs.actions.target.PlantActionTarget;
import stonering.entity.local.items.Item;
import stonering.entity.local.items.selectors.ItemSelector;
import stonering.entity.local.items.selectors.ToolWithActionItemSelector;
import stonering.entity.local.plants.PlantBlock;
import stonering.entity.local.unit.aspects.equipment.EquipmentAspect;
import stonering.game.core.model.lists.ItemContainer;
import stonering.generators.items.PlantProductGenerator;
import stonering.util.global.TagLoggersEnum;

import java.util.List;

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
        EquipmentAspect aspect = (EquipmentAspect) task.getPerformer().getAspects().get("equipment");
        if (aspect == null) return false;
        return toolItemSelector.check(aspect.getEquippedItems()) || addActionToTask();
    }

    @Override
    public void performLogic() {
        PlantBlock plantBlock = ((PlantActionTarget) actionTarget).getPlant().getBlock();
        List<Item> items = new PlantProductGenerator().generateHarvestProduct(plantBlock);
        items.forEach(item -> gameMvc.getModel().get(ItemContainer.class).putItem(item, actionTarget.getPosition()));
        TagLoggersEnum.TASKS.logDebug("harvesting plant finished at " + actionTarget.getPosition() + " by " + task.getPerformer());
    }

    private boolean addActionToTask() {
        Item target = gameMvc.getModel().get(ItemContainer.class).getItemAvailableBySelector(toolItemSelector, task.getPerformer().getPosition());
        if (target == null) return false;
        EquipItemAction equipItemAction = new EquipItemAction(target, true);
        task.addFirstPreAction(equipItemAction);
        return true;
    }
}
