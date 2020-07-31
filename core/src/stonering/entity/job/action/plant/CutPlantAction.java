package stonering.entity.job.action.plant;

import static stonering.entity.job.action.ActionConditionStatusEnum.*;

import stonering.entity.item.Item;
import stonering.entity.item.selectors.ItemSelector;
import stonering.entity.item.selectors.ToolWithActionItemSelector;
import stonering.entity.job.action.Action;
import stonering.entity.job.action.ActionConditionStatusEnum;
import stonering.entity.job.action.equipment.use.EquipToolItemAction;
import stonering.entity.job.action.target.EntityActionTarget;
import stonering.entity.job.action.target.PositionActionTarget;
import stonering.entity.job.designation.Designation;
import stonering.entity.plant.AbstractPlant;
import stonering.entity.plant.Plant;
import stonering.entity.plant.aspects.PlantGrowthAspect;
import stonering.entity.unit.aspects.equipment.EquipmentAspect;
import stonering.enums.action.ActionTargetTypeEnum;
import stonering.game.GameMvc;
import stonering.game.model.system.item.ItemContainer;
import stonering.game.model.system.plant.PlantContainer;
import stonering.util.logging.Logger;

/**
 * @author Alexander on 31.07.2020.
 */
public class CutPlantAction extends Action {
    private final Plant plant;
    ItemSelector toolItemSelector = new ToolWithActionItemSelector("cut");
    private PlantContainer plantContainer;

    public CutPlantAction(Plant plant) {
        super(new EntityActionTarget(plant, ActionTargetTypeEnum.ANY), "plant_harvesting");
        this.plant = plant;

        startCondition = () -> {
            Logger.TASKS.logDebug("Checking " + this);
            if (!checkPlant()) return FAIL;
            
            EquipmentAspect aspect = task.performer.get(EquipmentAspect.class);
            if (aspect == null) return FAIL; // no aspect on performer
            if (!toolItemSelector.checkItems(aspect.items)) // check tool
                return createActionForGettingTool();
            
            return OK;
        };

        onStart = () -> {
            speed = 1 + skill().speed * performerLevel() + performance();
            maxProgress = 400; // 480 for wall to floor in marble
        };
        
        onFinish = () -> {
            if (!checkPlant()) return;
            Logger.TASKS.logDebug("plant cutting finished at " + target.getPosition() + " by " + task.performer);
            GameMvc.model().get(PlantContainer.class).remove(plant, true);
        };
    }

    private boolean checkPlant() {
        return GameMvc.model().optional(PlantContainer.class)
                .map(container -> container.objects.contains(plant)
                        && container.isPlantBlockExists(plant.position)
                        && container.getPlantBlock(plant.position).plant == plant)
                .filter(container -> !plant.get(PlantGrowthAspect.class).dead)
                .orElse(false);
    }

    private ActionConditionStatusEnum createActionForGettingTool() {
        Logger.TASKS.logDebug("No tool equipped by performer for cutPlantAction");
        Item target = GameMvc.model().get(ItemContainer.class).util.getItemAvailableBySelector(toolItemSelector, task.performer.position);
        if (target == null) return Logger.TASKS.logDebug("No tool item found for cutPlantAction", OK);
        return addPreAction(new EquipToolItemAction(target));
    }

    private PlantContainer plantContainer() {
        return plantContainer == null ? plantContainer = GameMvc.model().get(PlantContainer.class) : plantContainer;
    }
}
