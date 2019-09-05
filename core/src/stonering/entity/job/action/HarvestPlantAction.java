package stonering.entity.job.action;

import stonering.entity.job.action.target.PlantActionTarget;
import stonering.entity.item.Item;
import stonering.entity.item.selectors.ItemSelector;
import stonering.entity.item.selectors.ToolWithActionItemSelector;
import stonering.entity.plants.Plant;
import stonering.entity.plants.PlantBlock;
import stonering.entity.unit.aspects.equipment.EquipmentAspect;
import stonering.game.GameMvc;
import stonering.game.model.system.ItemContainer;
import stonering.generators.items.PlantProductGenerator;
import stonering.util.global.Logger;

/**
 * Action for harvesting wild single-tile plants.
 *
 * @author Alexander on 15.01.2019.
 */
public class HarvestPlantAction extends Action {
    private ItemSelector toolItemSelector;

    public HarvestPlantAction(PlantBlock plantBlock) {
        super(new PlantActionTarget(plantBlock.getPlant()));
        toolItemSelector = new ToolWithActionItemSelector("harvest_plants");
    }

    @Override
    public int check() {
        if (!(((PlantActionTarget) actionTarget).getPlant() instanceof Plant)) return FAIL;
        EquipmentAspect aspect = task.getPerformer().getAspect(EquipmentAspect.class);
        if (aspect == null) return FAIL;
        if(toolItemSelector.checkItems(aspect.getEquippedItems())) return OK;
        return addActionToTask();
    }

    @Override
    public void performLogic() {
        PlantBlock plantBlock = ((Plant) ((PlantActionTarget) actionTarget).getPlant()).getBlock();
        Item item = new PlantProductGenerator().generateHarvestProduct(plantBlock);
        item.setPosition(actionTarget.getPosition());
        GameMvc.instance().getModel().get(ItemContainer.class).addItem(item);
        Logger.TASKS.logDebug("harvesting plant finished at " + actionTarget.getPosition() + " by " + task.getPerformer());
    }

    private int addActionToTask() {
        Item target = GameMvc.instance().getModel().get(ItemContainer.class).getItemAvailableBySelector(toolItemSelector, task.getPerformer().position);
        if (target == null) return FAIL;
        EquipItemAction equipItemAction = new EquipItemAction(target, true);
        task.addFirstPreAction(equipItemAction);
        return NEW;
    }
}
