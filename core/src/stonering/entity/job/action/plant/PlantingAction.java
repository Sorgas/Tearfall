package stonering.entity.job.action.plant;

import static stonering.entity.job.action.ActionConditionStatusEnum.*;

import java.util.Optional;

import stonering.entity.item.Item;
import stonering.entity.item.aspects.SeedAspect;
import stonering.entity.item.selectors.SeedItemSelector;
import stonering.entity.job.action.equipment.EquipmentAction;
import stonering.entity.job.action.equipment.obtain.ObtainItemAction;
import stonering.entity.job.action.target.PositionActionTarget;
import stonering.entity.job.designation.PlantingDesignation;
import stonering.entity.plant.Plant;
import stonering.enums.action.ActionTargetTypeEnum;
import stonering.game.GameMvc;
import stonering.game.model.system.item.ItemContainer;
import stonering.game.model.system.plant.PlantContainer;
import stonering.generators.plants.PlantGenerator;
import stonering.util.logging.Logger;

/**
 * Action for planting seed to a farm.
 * Planting always use single seed item.
 * Seed item should have {@link SeedAspect}
 */
public class PlantingAction extends EquipmentAction {
    private SeedItemSelector seedSelector;
    
    public PlantingAction(PlantingDesignation designation) {
        super(new PositionActionTarget(designation.position, ActionTargetTypeEnum.NEAR));
        seedSelector = new SeedItemSelector(designation.specimen);
        startCondition = () -> {
            if(seedSelector.checkItem(equipment().hauledItem)) return OK;
            return Optional.ofNullable(GameMvc.model().get(ItemContainer.class).util.getItemAvailableBySelector(seedSelector, task.performer.position))
                    .map(item -> addPreAction(new ObtainItemAction(item)))
                    .orElse(FAIL);
        };
        
        onFinish = () -> { 
            Logger.TASKS.logDebug("Planting seed of " + seedSelector.getSpecimen() + " to " + target.getPosition());
            createPlant(equipment().hauledItem);
            equipment().hauledItem = null;
        };
    }

    private void createPlant(Item seed) {
        PlantContainer plantContainer = GameMvc.model().get(PlantContainer.class);
        Plant plant = new PlantGenerator().generatePlant(seed.get(SeedAspect.class));
        plantContainer.add(plant, target.getPosition());
    }
}
