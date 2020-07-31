package stonering.entity.job.action.plant;

import stonering.entity.job.action.Action;
import stonering.entity.job.action.ActionConditionStatusEnum;
import stonering.entity.job.action.equipment.use.EquipToolItemAction;
import stonering.entity.job.action.target.PlantActionTarget;
import stonering.entity.item.Item;
import stonering.entity.item.selectors.ItemSelector;
import stonering.entity.item.selectors.ToolWithActionItemSelector;
import stonering.entity.plant.AbstractPlant;
import stonering.entity.plant.PlantBlock;
import stonering.entity.unit.aspects.equipment.EquipmentAspect;
import stonering.game.GameMvc;
import stonering.game.model.system.item.ItemContainer;
import stonering.game.model.system.plant.PlantContainer;
import stonering.generators.items.PlantProductGenerator;
import stonering.util.logging.Logger;

import static stonering.entity.job.action.ActionConditionStatusEnum.*;

/**
 * This action harvests products from harvestable plants.
 *
 */
public class PlantHarvestAction extends Action {
    private ItemSelector toolItemSelector;
    private AbstractPlant targetPlant;

    public PlantHarvestAction(AbstractPlant plant) {
        super(new PlantActionTarget(plant));
        targetPlant = plant;
        toolItemSelector = new ToolWithActionItemSelector("harvest_plants"); //TODO handle harvesting without tool.

        startCondition = () -> {
            PlantContainer container = GameMvc.model().get(PlantContainer.class);
            EquipmentAspect aspect = task.performer.get(EquipmentAspect.class);
            if (aspect == null) return FAIL; // performer has aspect
            if (container.getPlantInPosition(target.getPosition()) != targetPlant) return FAIL; // plant not present anymore
            if(toolItemSelector.checkItems(aspect.items)) return OK; // performer has tool
            return addActionToTask();
        };

        onFinish = () -> {
            //TODO add putting products into worn container
            Logger.PLANTS.logDebug("harvesting plant");
            PlantBlock block = GameMvc.model().get(PlantContainer.class).getPlantBlock(target.getPosition());
            Item item = new PlantProductGenerator().generateHarvestProduct(block);
            GameMvc.model().get(ItemContainer.class).onMapItemsSystem.addNewItemToMap(item, block.position);
        };
    }

    private ActionConditionStatusEnum addActionToTask() {
        Item target = GameMvc.model().get(ItemContainer.class).util.getItemAvailableBySelector(toolItemSelector, task.performer.position);
        if (target == null) return FAIL;
        EquipToolItemAction equipToolItemAction = new EquipToolItemAction(target);
        task.addFirstPreAction(equipToolItemAction);
        return NEW;
    }
}
